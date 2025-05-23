package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.dto.APIResponse;
import com.project.raiserbuddy.dto.PaymentLinkResponse;
import com.project.raiserbuddy.entity.Cart;
import com.project.raiserbuddy.entity.Invoice;
import com.project.raiserbuddy.entity.Order;
import com.project.raiserbuddy.enums.OrderStatus;
import com.project.raiserbuddy.enums.PaymentStatus;
import com.project.raiserbuddy.exceptions.OrderException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.repository.CartItemRepository;
import com.project.raiserbuddy.repository.CartRepository;
import com.project.raiserbuddy.repository.OrderRepository;
import com.project.raiserbuddy.service.InvoiceService;
import com.project.raiserbuddy.service.OrderService;
import com.project.raiserbuddy.service.UsersManagementService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Calendar;
import java.util.Date;

@RestController
public class PaymentController {

	@Value("${razorpay.api.key}")
	private String apiKey;

	@Value("${razorpay.api.secret}")
	private String apiSecret;

	@Autowired
	private OrderService orderService;
	@Autowired
	private UsersManagementService userService;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private InvoiceService invoiceService;
	@Autowired
	private CartItemRepository cartItemRepository;

	@PostMapping("/payments/{orderId}/{amount}")
	@Transactional
	public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Integer orderId,@PathVariable double amount,
																 @RequestHeader("Authorization") String jwt)
			throws RazorpayException, UserException, OrderException, JSONException {

		Order order = orderService.findOrderById(orderId);
		try {
			// Instantiate a Razorpay client with your key ID and secret
			RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

			// Create a JSON object with the payment link request parameters
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", amount * 100);
			paymentLinkRequest.put("currency", "INR");
//		      paymentLinkRequest.put("expire_by",1691097057);
//		      paymentLinkRequest.put("reference_id",order.getId().toString());


			// Create a JSON object with the customer details
			JSONObject customer = new JSONObject();
			customer.put("name", order.getUser().getName());
			customer.put("contact", order.getUser().getMobile_number());
			customer.put("email", order.getUser().getEmail());
			paymentLinkRequest.put("customer", customer);

			// Create a JSON object with the notification settings
			JSONObject notify = new JSONObject();
			notify.put("sms", true);
			notify.put("email", true);
			paymentLinkRequest.put("notify", notify);

			// Set the reminder settings
			paymentLinkRequest.put("reminder_enable", true);

			// Set the callback URL and method
			paymentLinkRequest.put("callback_url", "https://app-raiserbuddy-com.vercel.app/payment/" + orderId);
			paymentLinkRequest.put("callback_method", "get");

			// Create the payment link using the paymentLink.create() method
			PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

			String paymentLinkId = payment.get("id");
			String paymentLinkUrl = payment.get("short_url");

			PaymentLinkResponse res = new PaymentLinkResponse(paymentLinkUrl, paymentLinkId);

			PaymentLink fetchedPayment = razorpay.paymentLink.fetch(paymentLinkId);

			order.setOrderId(fetchedPayment.get("order_id"));
			orderRepository.save(order);


			//to empty cart items after ordering
			order.getOrderItems().forEach((item) -> {
				Cart givenCart = cartRepository.findByUserId(order.getUser().getId());
				System.out.println(item.getProduct().getProductId());

				System.out.println(givenCart.getCartId());
				cartItemRepository.deleteCartItemByProductIdAndCartId(givenCart.getCartId(), item.getProduct().getProductId());
			});

			// Print the payment link ID and URL
			System.out.println("Payment link ID: " + paymentLinkId);
			System.out.println("Payment link URL: " + paymentLinkUrl);
			System.out.println("Order Id : " + fetchedPayment.get("order_id") + fetchedPayment);

			return new ResponseEntity<PaymentLinkResponse>(res, HttpStatus.ACCEPTED);

		} catch (RazorpayException e) {

			System.out.println("Error creating payment link: " + e.getMessage());
			throw new RazorpayException(e.getMessage());
		}


//		order_id
	}

	@GetMapping("/payments")
	public ResponseEntity<APIResponse> redirect(@RequestParam(name = "payment_id") String paymentId, @RequestParam("order_id") Integer orderId) throws RazorpayException, OrderException {
		RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
		Order order = orderService.findOrderById(orderId);

		try {


			Payment payment = razorpay.payments.fetch(paymentId);
			System.out.println("payment details --- " + payment + payment.get("status"));

			if (payment.get("status").equals("captured")) {
				System.out.println("payment details --- " + payment + payment.get("status"));

				order.getPaymentDetails().setPaymentId(paymentId);
				order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
				order.setOrderStatus(OrderStatus.PLACED);
				order.setOrderId(payment.get("order_id"));
//			order.setOrderItems(order.getOrderItems());
				System.out.println(order.getPaymentDetails().getStatus() + "payment status ");
				orderRepository.save(order);

				// invoice generation
				Invoice invoice = new Invoice();
				invoice.setCustomerName(order.getUser().getName());
				invoice.setStatus("PAID");
				invoice.setDueDate(calculateDueDate());
				invoice.setInvoiceDate(new Date());
				invoice.setAmount(order.getTotalDiscountedPrice());
//				invoice.setItems(order.getOrderItems());
				invoiceService.saveInvoice(invoice);
			}
			APIResponse res = new APIResponse("your order get placed", true);
			return new ResponseEntity<APIResponse>(res, HttpStatus.OK);

		} catch (Exception e) {
			System.out.println("errrr payment -------- ");
			new RedirectView("https://shopwithzosh.vercel.app/payment/failed");
			throw new RazorpayException(e.getMessage());
		}

	}

	private Date calculateDueDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 30); // Due in 30 days
		return calendar.getTime(); }

	}
