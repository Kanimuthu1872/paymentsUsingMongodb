package com.example.payments.repository;

import com.example.payments.model.Payment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByStatus(String status);
    Payment findByInvoicenumber(String invoiceNumber);
    List<Payment> findByPaymentdateAndStatus(String paymentDate, String status);


    @Query(value = "{}", fields = "{ 'amount' : 1 }")
    List<Payment> findAllAmounts();
    List<Payment> findByPaymentdate(String paymentDate);

}
