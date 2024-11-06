package com.BiddingApplication.Controller;

import com.BiddingApplication.Model.Transaction;
import com.BiddingApplication.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Create a new transaction (usually after an auction ends)
    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@RequestParam int auctionId,
                                                         @RequestParam int buyerId,
                                                         @RequestParam BigDecimal amount) {
        Transaction transaction = transactionService.createTransaction(auctionId, buyerId, amount);
        return ResponseEntity.ok(transaction);
    }

    // Update transaction status (e.g., when payment is completed)
    @PutMapping("/{transactionId}/status")
    public ResponseEntity<Transaction> updateTransactionStatus(@PathVariable int transactionId,
                                                               @RequestParam Transaction.TransactionStatus status) {
        Transaction updatedTransaction = transactionService.updateTransactionStatus(transactionId, status);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Get transactions for a buyer
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Transaction>> getTransactionsForBuyer(@PathVariable int buyerId) {
        List<Transaction> transactions = transactionService.getTransactionsForBuyer(buyerId);
        return ResponseEntity.ok(transactions);
    }

    // Get transactions for a seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Transaction>> getTransactionsForSeller(@PathVariable int sellerId) {
        List<Transaction> transactions = transactionService.getTransactionsForSeller(sellerId);
        return ResponseEntity.ok(transactions);
    }
}

