package com.BiddingApplication.Repository;

import com.BiddingApplication.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByBuyer_BuyerId(int buyerId);  // Get transactions by buyer ID
    List<Transaction> findBySeller_SellerId(int sellerId);  // Get transactions by seller ID
}
