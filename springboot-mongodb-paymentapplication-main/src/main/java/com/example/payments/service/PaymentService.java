package com.example.payments.service;

import com.example.payments.dto.Paymentdto;
import com.example.payments.model.Payment;
import com.example.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    // Method to initiate a single payment
    public Payment initiatePayment(Paymentdto paymentDto) {
        Payment payment = Payment.builder()
                .amount(paymentDto.getAmount())
                .currency(paymentDto.getCurrency())
                .username(paymentDto.getUsername())
                .ponumber(paymentDto.getPonumber())
                .invoicenumber(paymentDto.getInvoicenumber())
                .targetBankAccount(paymentDto.getTargetBankAccount())
                .tds(paymentDto.getTds())
                .sourceBankAccount(paymentDto.getSourceBankAccount())
                .status(paymentDto.getStatus())
                .paymentdate(paymentDto.getPaymentdate())
                .build();
        return paymentRepository.save(payment);
    }

    // Method to initiate multiple payments
    public List<Payment> initiatePayments(List<Paymentdto> paymentDtos) {
        List<Payment> payments = paymentDtos.stream().map(dto -> Payment.builder()
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .username(dto.getUsername())
                .ponumber(dto.getPonumber())
                .invoicenumber(dto.getInvoicenumber())
                .targetBankAccount(dto.getTargetBankAccount())
                .tds(dto.getTds())
                .sourceBankAccount(dto.getSourceBankAccount())
                .status(dto.getStatus())
                .paymentdate(dto.getPaymentdate())
                .build()).collect(Collectors.toList());

        return paymentRepository.saveAll(payments);
    }

    // Method to get the total amount of all payments
    public Double getTotalAmount() {
        List<Payment> payments = paymentRepository.findAllAmounts();
        return payments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    // Method to find payments by status
    public List<Payment> findPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    // Method to find a payment by invoice number
    public Payment findPaymentByInvoiceNumber(String invoiceNumber) {
        return paymentRepository.findByInvoicenumber(invoiceNumber);
    }

    public List<Payment> findPaymentsByDateAndStatus(String paymentDate, String status) {
        return paymentRepository.findByPaymentdateAndStatus(paymentDate, status);
    }
    public Payment editPayment(String id, Paymentdto paymentdto) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setAmount(paymentdto.getAmount());
            payment.setCurrency(paymentdto.getCurrency());
            payment.setUsername(paymentdto.getUsername());
            payment.setPonumber(paymentdto.getPonumber());
            payment.setInvoicenumber(paymentdto.getInvoicenumber());
            payment.setTargetBankAccount(paymentdto.getTargetBankAccount());
            payment.setSourceBankAccount(paymentdto.getSourceBankAccount());
            payment.setTds(paymentdto.getTds());
            payment.setStatus(paymentdto.getStatus());
            payment.setPaymentdate(paymentdto.getPaymentdate());
            return paymentRepository.save(payment);
        }
        throw new RuntimeException("Payment not found");
    }
    public void deletePayment(String id) {
        paymentRepository.deleteById(id);
    }
    public Double getTotalNetAmount() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .mapToDouble(Payment::calculateNetAmount)
                .sum(); // Calculate the sum of net amounts
    }
    public List<Payment> findPaymentsByPaymentDate(String paymentDate) {
        return paymentRepository.findByPaymentdate(paymentDate);
    }

}
