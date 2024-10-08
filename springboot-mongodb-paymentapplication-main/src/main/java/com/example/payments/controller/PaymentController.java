package com.example.payments.controller;

import com.example.payments.dto.Paymentdto;
import com.example.payments.model.Payment;
import com.example.payments.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Endpoint to initiate a single payment
    @PostMapping("/initiate")
    public ResponseEntity<Payment> initiatePayment(@RequestBody @Valid Paymentdto paymentDto) {
        Payment createdPayment = paymentService.initiatePayment(paymentDto);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    // Endpoint to initiate multiple payments
    @PostMapping("/bulk-initiate")
    public ResponseEntity<List<Payment>> initiatePayments(@RequestBody @Valid List<Paymentdto> paymentDtos) {
        List<Payment> createdPayments = paymentService.initiatePayments(paymentDtos);
        return new ResponseEntity<>(createdPayments, HttpStatus.CREATED);
    }

    // Endpoint to retrieve pending payments
    @GetMapping("/pending")
    public ResponseEntity<List<Payment>> findPendingPayments() {
        List<Payment> pendingPayments = paymentService.findPaymentsByStatus("PENDING");
        return new ResponseEntity<>(pendingPayments, HttpStatus.OK);
    }

    // Endpoint to retrieve total amount of all payments
    @GetMapping("/total-amount")
    public ResponseEntity<Double> getTotalAmount() {
        Double totalAmount = paymentService.getTotalAmount();
        return new ResponseEntity<>(totalAmount, HttpStatus.OK);
    }

    // Endpoint to retrieve payment by invoice number
    @GetMapping("/amount/{invoiceNumber}")
    public ResponseEntity<Double> getAmountByInvoiceNumber(@PathVariable String invoiceNumber) {
        Payment payment = paymentService.findPaymentByInvoiceNumber(invoiceNumber);
        return new ResponseEntity<>(payment.getAmount(), HttpStatus.OK);
    }

    // Endpoint to retrieve payments by payment date and status
    @GetMapping("/status-by-date/{paymentDate}/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatusAndDate(
            @PathVariable String paymentDate, @PathVariable String status) {
        List<Payment> payments = paymentService.findPaymentsByDateAndStatus(paymentDate, status);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<Payment> editPayment(@PathVariable String id, @RequestBody Paymentdto paymentdto) {
        return new ResponseEntity<>(paymentService.editPayment(id, paymentdto), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable String id) {
        paymentService.deletePayment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/total-net-amount")
    public ResponseEntity<Double> getTotalNetAmount() {
        Double totalNetAmount = paymentService.getTotalNetAmount();
        return new ResponseEntity<>(totalNetAmount, HttpStatus.OK);
    }
    @GetMapping("/status/{paymentDate}")
    public ResponseEntity<Map<String, List<Payment>>> getPaymentsByDate(@PathVariable String paymentDate) {
        List<Payment> allPayments = paymentService.findPaymentsByPaymentDate(paymentDate);

        Map<String, List<Payment>> categorizedPayments = allPayments.stream()
                .collect(Collectors.groupingBy(Payment::getStatus));

        return new ResponseEntity<>(categorizedPayments, HttpStatus.OK);
    }

}
