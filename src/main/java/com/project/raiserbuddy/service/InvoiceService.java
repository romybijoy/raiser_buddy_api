package com.project.raiserbuddy.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.project.raiserbuddy.entity.*;
import com.project.raiserbuddy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;

	public List<Invoice> getAllInvoices() {
		return invoiceRepository.findAll();
	}

	public Optional<Invoice> getInvoiceById(Integer id) {
		return invoiceRepository.findById(id);
	}

	public Invoice saveInvoice(Invoice invoice) {
		return invoiceRepository.save(invoice);
	}

	public void deleteInvoice(Integer id) {

		invoiceRepository.deleteById(id);
	}

	// Method to generate and save invoice file
	public File getInvoiceFile(Integer id) throws Exception {
		Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);
		if (invoiceOpt.isPresent()) {
			Invoice invoice = invoiceOpt.get();
// Generate the file (For example, as a PDF)
			File file = new File("invoice_" + invoice.getId() + ".pdf");
			PdfWriter writer = new PdfWriter(new FileOutputStream(file));
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);

			// Add content to PDF
			document.add(new Paragraph("Invoice").setFontSize(20).setBold());
			document.add(new Paragraph("Invoice ID: " + invoice.getId()));
			document.add(new Paragraph("Customer Name: " + invoice.getCustomerName()));
			document.add(new Paragraph("Invoice Date: " + invoice.getInvoiceDate()));
			document.add(new Paragraph("Due Date: " + invoice.getDueDate()));
			document.add(new Paragraph("Status: " + invoice.getStatus()));

			// Add a table for items
			Table table = new Table(new float[]{1, 2, 1, 1});
			table.addHeaderCell(new Cell().add(new Paragraph("ID")));
			table.addHeaderCell(new Cell().add(new Paragraph("Product")));
			table.addHeaderCell(new Cell().add(new Paragraph("Quantity")));
			table.addHeaderCell(new Cell().add(new Paragraph("Unit Price")));

//			for (OrderItem item : invoice.getItems()) {
//				table.addCell(new Cell().add(new Paragraph(item.getId().toString())));
//				table.addCell(new Cell().add(new Paragraph(item.getProduct().getName())));
//				table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
//				table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getPrice()))));
//			}
//					document.add(table);

			// Add the total amount

//			BigDecimal totalAmount = invoice.getItems().stream() .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
//					.reduce(BigDecimal.ZERO, BigDecimal::add);

			document.add(new Paragraph("Total Amount: " + invoice.getAmount()));
			System.out.print("Hello");
			document.close();
			return file;
		} else {
			throw new Exception("Invoice not found");
		}
	}
}