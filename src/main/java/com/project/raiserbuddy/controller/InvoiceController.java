package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.dto.OrderDTO;
import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.entity.Invoice;
import com.project.raiserbuddy.entity.Order;
import com.project.raiserbuddy.exceptions.OrderException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.service.InvoiceService;
import com.project.raiserbuddy.service.OrderService;
import com.project.raiserbuddy.service.UsersManagementService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

	@Autowired 
	private InvoiceService invoiceService;
	@GetMapping 
	public List<Invoice> getAllInvoices() {
		return invoiceService.getAllInvoices();
	} 
	@GetMapping("/{id}")
	public ResponseEntity<Invoice> getInvoiceById(@PathVariable Integer id) {
	Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
	return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); }

	@PostMapping
	public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
	Invoice savedInvoice = invoiceService.saveInvoice(invoice);
	return ResponseEntity.status(HttpStatus.CREATED).body(savedInvoice);
}
@PutMapping("/{id}")
public ResponseEntity<Invoice> updateInvoice(@PathVariable Integer id, @RequestBody Invoice invoiceDetails) {
	Optional<Invoice> existingInvoice = invoiceService.getInvoiceById(id);
	if (existingInvoice.isPresent()) {
		Invoice invoice = existingInvoice.get();
		invoice.setCustomerName(invoiceDetails.getCustomerName());
		invoice.setInvoiceDate(invoiceDetails.getInvoiceDate());
		invoice.setDueDate(invoiceDetails.getDueDate());
		invoice.setStatus(invoiceDetails.getStatus());
//		invoice.setItems(invoiceDetails.getItems());
		Invoice updatedInvoice = invoiceService.saveInvoice(invoice);
		return ResponseEntity.ok(updatedInvoice);
	} else {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteInvoice(@PathVariable Integer id) {
	if (invoiceService.getInvoiceById(id).isPresent()) {
		invoiceService.deleteInvoice(id); return ResponseEntity.noContent().build();
	} else {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	} }
	@GetMapping("/{id}/download") public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable Integer id) {
	try {

		System.out.println("Hello");
		File file = invoiceService.getInvoiceFile(id);
		// Implement this method to retrieve the invoice file
		return ResponseEntity.ok() .contentType(MediaType.APPLICATION_PDF)
		// Assume the invoice is a PDF
		.body(new InputStreamResource(new FileInputStream(file))); }
		catch (Exception e) {
		 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		 }
		}

		}
