package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.dto.PaymentLinkResponse;
import com.project.raiserbuddy.exceptions.OrderException;
import com.project.raiserbuddy.repository.CartItemRepository;
import com.project.raiserbuddy.repository.CartRepository;
import com.project.raiserbuddy.repository.OrderRepository;
import com.project.raiserbuddy.repository.UsersRepository;
import com.project.raiserbuddy.service.InvoiceService;
import com.project.raiserbuddy.service.OrderService;
import com.project.raiserbuddy.service.PaymentService;
import com.project.raiserbuddy.service.UsersManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

	private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

	@Value("${razorpay.api.key}")
	private String apiKey;

	@Value("${razorpay.api.secret}")
	private String apiSecret;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

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

	@Autowired
	private UsersRepository userRepository;


	@PostMapping("/payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse> createPaymentLink(
			@PathVariable Integer orderId,
			@RequestParam(defaultValue = "false") boolean useWallet
	) throws Exception {

		PaymentLinkResponse response = paymentService.processPayment(orderId, useWallet);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

//	@PostMapping("/payments/{orderId}")
//	@Transactional
//	public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Integer orderId,@RequestParam(defaultValue = "false") boolean useWallet,
//																 @RequestHeader("Authorization") String jwt)
//			throws RazorpayException, UserException, OrderException, JSONException {
//
//		Order order = orderService.findOrderById(orderId);
//		OurUsers user = order.getUser();
//
//		double orderAmount = order.getTotalDiscountedPrice();
//
//		double walletUsed = order.getWalletUsedAmount(); // existing
//
//		if (useWallet && walletUsed == 0) {
//			double walletBalance = user.getWalletBalance();
//			walletUsed = Math.min(walletBalance, orderAmount);
//
//			user.setWalletBalance(walletBalance - walletUsed);
//			userRepository.save(user);
//
//			order.setWalletUsedAmount(walletUsed);
//		}
//
//		double finalAmount = orderAmount - walletUsed;
//
//		order.setFinalPayableAmount(finalAmount);
//		orderRepository.save(order);
//
//		//wallet only
//		if (finalAmount == 0) {
//			order.setPaymentStatus(PaymentStatus.COMPLETED);
//			order.setOrderStatus(OrderStatus.PLACED);
//			orderRepository.save(order);
//
//			return new ResponseEntity<>(
//					new PaymentLinkResponse(null, "WALLET_SUCCESS"),
//					HttpStatus.ACCEPTED
//			);
//		}
//		try {
//			// Instantiate a Razorpay client with your key ID and secret
//			RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
//
//			// Create a JSON object with the payment link request parameters
//			JSONObject paymentLinkRequest = new JSONObject();
//			paymentLinkRequest.put("amount", finalAmount * 100);
//			paymentLinkRequest.put("currency", "INR");
////		      paymentLinkRequest.put("expire_by",1691097057);
//		      paymentLinkRequest.put("reference_id",orderId.toString());
//
//
//			// Create a JSON object with the customer details
//			JSONObject customer = new JSONObject();
//			customer.put("name", order.getUser().getName());
//			customer.put("contact", order.getUser().getMobile_number());
//			customer.put("email", order.getUser().getEmail());
//			paymentLinkRequest.put("customer", customer);
//
//			// Create a JSON object with the notification settings
//			JSONObject notify = new JSONObject();
//			notify.put("sms", true);
//			notify.put("email", true);
//			paymentLinkRequest.put("notify", notify);
//
//			// Set the reminder settings
//			paymentLinkRequest.put("reminder_enable", true);
//
//			// Set the callback URL and method
//			paymentLinkRequest.put("callback_url", "https://raiser-buddy-app.vercel.app/payment/" + orderId);
//			paymentLinkRequest.put("callback_method", "get");
//
//			// Create the payment link using the paymentLink.create() method
//			PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
//
//			String paymentLinkId = payment.get("id");
//			String paymentLinkUrl = payment.get("short_url");
//
//			PaymentLinkResponse res = new PaymentLinkResponse(paymentLinkUrl, paymentLinkId);
//
//
//			order.setPaymentGatewayOrderId(payment.get("order_id"));
//			orderRepository.save(order);
//
//
//			// Print the payment link ID and URL
//			log.info("Payment link ID: " + paymentLinkId);
//			log.info("Payment link URL: " + paymentLinkUrl);
//			log.info("Order Id : " + payment.get("order_id") + payment);
//
//			return new ResponseEntity<PaymentLinkResponse>(res, HttpStatus.ACCEPTED);
//
//		} catch (RazorpayException e) {
//
//			log.error("Error creating payment link: " + e.getMessage());
//			throw new RazorpayException(e.getMessage());
//		}
//
//
//	}

//	@GetMapping("/payments")
//	public ResponseEntity<APIResponse> redirect(@RequestParam(name = "payment_id") String paymentId, @RequestParam("order_id") Integer orderId) throws RazorpayException, OrderException {
//		RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
//		Order order = orderService.findOrderById(orderId);
//
//		try {
//
//			Payment payment = razorpay.payments.fetch(paymentId);
//			log.info("payment details --- " + payment + payment.get("status"));
//
//			if ("captured".equals(payment.get("status"))) {
//				log.info("payment details --- " + payment + payment.get("status"));
//
//				if (order.getPaymentStatus() == PaymentStatus.COMPLETED) {
//					return ResponseEntity.ok(new APIResponse("Already processed", true));
//				}
//
//				if (order.getPaymentDetails() == null) {
//					order.setPaymentDetails(new PaymentDetails());
//				}
//				order.getPaymentDetails().setPaymentId(paymentId);
//				order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
//				order.setPaymentStatus(PaymentStatus.COMPLETED);
//				order.setOrderStatus(OrderStatus.PLACED);
//				order.setPaymentGatewayOrderId(payment.get("order_id"));
//				log.info(order.getPaymentDetails().getStatus() + "payment status ");
//				orderRepository.save(order);
//
//				Cart givenCart = cartRepository.findByUserId(order.getUser().getId());
//				//to empty cart items after ordering
//				order.getOrderItems().forEach((item) -> {
//
//					cartItemRepository.deleteCartItemByProductIdAndCartId(givenCart.getCartId(), item.getProduct().getProductId());
//				});
//
//				// invoice generation
//				Invoice invoice = new Invoice();
//				invoice.setCustomerName(order.getUser().getName());
//				invoice.setStatus("PAID");
//				invoice.setDueDate(calculateDueDate());
//				invoice.setInvoiceDate(new Date());
//				invoice.setAmount(order.getFinalPayableAmount());
////				invoice.setItems(order.getOrderItems());
//				invoiceService.saveInvoice(invoice);
//			}
//			APIResponse res = new APIResponse("your order get placed", true);
//			return new ResponseEntity<>(res, HttpStatus.OK);
//
//		} catch (Exception e) {
//			log.error("error payment -------- "+e.getMessage());
//			new RedirectView("https://raiser-buddy-app.vercel.app/payment/failed");
//			throw new RazorpayException(e.getMessage());
//		}
//
//	}

@GetMapping("/payments")
public ResponseEntity<?> handlePayment(
		@RequestParam String payment_id,
		@RequestParam Integer order_id
) {
	try {
		System.out.println("CALLBACK HIT");
		paymentService.handlePaymentCallback(payment_id, order_id);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Payment updated");

		return ResponseEntity.ok(response);
	} catch (Exception e) {
		e.printStackTrace();
		return ResponseEntity.status(500).body("Error: " + e.getMessage());
	}
}

	@PostMapping("/payments/failure")
	public ResponseEntity<?> handleFailure(
			@RequestParam Integer orderId
	) throws OrderException {

		paymentService.handlePaymentFailure(orderId);

		return ResponseEntity.ok("Payment failed updated");
	}

	}
