package com.project.raiserbuddy.service;

import com.project.raiserbuddy.repository.OrderRepository;
import com.project.raiserbuddy.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.project.raiserbuddy.dto.PaymentLinkResponse;
import com.project.raiserbuddy.entity.*;
import com.project.raiserbuddy.enums.OrderStatus;
import com.project.raiserbuddy.enums.PaymentStatus;
import com.project.raiserbuddy.exceptions.OrderException;
import com.project.raiserbuddy.repository.CartItemRepository;
import com.project.raiserbuddy.repository.CartRepository;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import jakarta.transaction.Transactional;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * PaymentService
 *
 * @author Romyb
 * @since 17/04/2026
 */
@Slf4j
@Service
public class PaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UsersRepository userRepository;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Value("${app.payment.callback-url}")
    private String callbackUrl;

    @Autowired
    private UsersManagementService userService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CartItemRepository cartItemRepository;


    @Transactional
    public PaymentLinkResponse processPayment(Integer orderId, boolean useWallet) throws Exception {

        Order order = orderService.findOrderById(orderId);

        // Amount already calculated during order creation
        double finalAmount = order.getFinalPayableAmount();

        // Wallet-only payment
        if (finalAmount == 0) {
            order.setPaymentStatus(PaymentStatus.COMPLETED);
            order.setOrderStatus(OrderStatus.PLACED);
            orderRepository.save(order);

            clearCart(order);

            return new PaymentLinkResponse(null, "WALLET_SUCCESS");
        }

        // Only create Razorpay link
        return createRazorpayLink(order, finalAmount);
    }

    private PaymentLinkResponse createRazorpayLink(Order order, double amount) throws Exception {

        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

        // STEP 1: REUSE EXISTING LINK (IMPORTANT)
        if (order.getPaymentLinkId() != null) {

            try {
                PaymentLink existing = razorpay.paymentLink.fetch(order.getPaymentLinkId());

                String status = existing.get("status");

                // reuse only if still valid
                if (!"paid".equals(status) && !"cancelled".equals(status)) {
                    return new PaymentLinkResponse(
                            existing.get("short_url"),
                            existing.get("id")
                    );
                }

            } catch (Exception e) {
                // if fetch fails → create new link
                System.out.println("Existing link fetch failed, creating new one");
            }
        }

        // STEP 2: CREATE NEW LINK
        JSONObject req = new JSONObject();
        req.put("amount", amount * 100);
        req.put("currency", "INR");

        req.put(
                "reference_id",
                order.getId().toString() + "_" + System.currentTimeMillis()
        );

        JSONObject customer = new JSONObject();
        customer.put("name", order.getUser().getName());
        customer.put("contact", order.getUser().getMobile_number());
        customer.put("email", order.getUser().getEmail());
        req.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("sms", true);
        notify.put("email", true);
        req.put("notify", notify);

        req.put("reminder_enable", true);

        req.put("callback_url", callbackUrl + "/" + order.getId());
        req.put("callback_method", "get");

      
        PaymentLink payment = razorpay.paymentLink.create(req);

        // SAVE LINK ID FOR REUSE
        order.setPaymentGatewayOrderId(payment.get("order_id"));
        order.setPaymentLinkId(payment.get("id"));

        orderRepository.save(order);

        return new PaymentLinkResponse(
                payment.get("short_url"),
                payment.get("id")
        );
    }

    public void handlePaymentCallback(String paymentId, Integer orderId) throws Exception {

        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

        Order order = orderService.findOrderById(orderId);

        // Prevent duplicate processing
        if (order.getPaymentStatus() == PaymentStatus.COMPLETED) {
            return;
        }

        Payment payment = razorpay.payments.fetch(paymentId);
        String status = (String) payment.get("status");
System.out.println("Payment callback received for orderId: {}, paymentId: {}, status: {}"+ orderId + paymentId + status);
        if ("captured".equals(status)) {
            // STEP 1: Update order (CRITICAL - must not fail)
            updateOrderAfterPayment(order, paymentId, payment);

            // STEP 2: Side operations (should NEVER break flow)
            try {
                clearCart(order);
            } catch (Exception e) {
                System.out.println("Cart clear failed: " + e.getMessage());
            }

            try {
                generateInvoice(order);
            } catch (Exception e) {
                System.out.println("Invoice generation failed: " + e.getMessage());
            }
        }
    }

    @Transactional
    public void updateOrderAfterPayment(Order order, String paymentId, Payment payment) {

        // Ensure paymentDetails exists
        if (order.getPaymentDetails() == null) {
            order.setPaymentDetails(new PaymentDetails());
        }

        order.getPaymentDetails().setPaymentId(paymentId);
        order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);

        order.setPaymentStatus(PaymentStatus.COMPLETED);
        order.setOrderStatus(OrderStatus.PLACED);

        // Razorpay order id (optional tracking)
        order.setPaymentGatewayOrderId(payment.get("order_id"));

        orderRepository.save(order);

        System.out.println("Order updated successfully to PLACED");
    }


    protected void clearCart(Order order) {
        System.out.println("Clearing cart for user: " + order.getUser().getId());
        Cart cart = cartRepository.findByUserId(order.getUser().getId());

        if (cart == null) {
            System.out.println("Cart not found for user");
            return;
        }

        order.getOrderItems().forEach(item -> {

            if (item.getProduct() == null) {
                System.out.println("Product null in order item");
                return;
            }

            cartItemRepository.deleteCartItemByProductIdAndCartId(
                    cart.getCartId(),
                    item.getProduct().getProductId()
            );
        });
    }

    private void generateInvoice(Order order) {

        Invoice invoice = new Invoice();

        invoice.setCustomerName(order.getUser().getName());
        invoice.setStatus("PAID");
        invoice.setInvoiceDate(new Date());
        invoice.setDueDate(calculateDueDate());

        // Always safe value
        double amount = order.getFinalPayableAmount() != null
                ? order.getFinalPayableAmount()
                : order.getFinalPrice();

        invoice.setAmount(amount);

        invoiceService.saveInvoice(invoice);
    }

    private Date calculateDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        return calendar.getTime(); }

    @Transactional
    public void handlePaymentFailure(Integer orderId) throws OrderException {

        Order order = orderService.findOrderById(orderId);

        // Prevent overriding success
        if (order.getPaymentStatus() == PaymentStatus.COMPLETED) {
            return;
        }

        // Only update if still pending
        if (order.getPaymentStatus() == PaymentStatus.PENDING) {
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setOrderStatus(OrderStatus.PENDING);

            orderRepository.save(order);
        }
    }
}
