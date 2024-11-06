package com.BiddingApplication.Service;

import com.BiddingApplication.Model.Auction;
import com.BiddingApplication.Model.Buyer;
import com.BiddingApplication.Model.Seller;
import com.BiddingApplication.Model.Transaction;
import com.BiddingApplication.Repository.AuctionRepository;
import com.BiddingApplication.Repository.BuyerRepository;
import com.BiddingApplication.Repository.SellerRepository;
import com.BiddingApplication.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    // Create a new transaction (after an auction is closed and a winner is declared)
    public Transaction createTransaction(int auctionId, int buyerId, BigDecimal amount) {
        Optional<Auction> auctionOptional = auctionRepository.findById(auctionId);
        Optional<Buyer> buyerOptional = buyerRepository.findById(buyerId);

        if (auctionOptional.isEmpty()) {
            throw new RuntimeException("Auction not found");
        }
        if (buyerOptional.isEmpty()) {
            throw new RuntimeException("Buyer not found");
        }

        Auction auction = auctionOptional.get();
        Buyer buyer = buyerOptional.get();
        Seller seller = auction.getSeller();

        // Create a new transaction
        Transaction transaction = new Transaction();
        transaction.setAuction(auction);
        transaction.setBuyer(buyer);
        transaction.setSeller(seller);
        transaction.setTransactionAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionStatus(Transaction.TransactionStatus.PENDING);

        return transactionRepository.save(transaction);
    }

    // Update the transaction status (for example, after payment completion)
    public Transaction updateTransactionStatus(int transactionId, Transaction.TransactionStatus status) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isEmpty()) {
            throw new RuntimeException("Transaction not found");
        }

        Transaction transaction = transactionOptional.get();
        transaction.setTransactionStatus(status);
        return transactionRepository.save(transaction);
    }

    // Get transactions for a buyer
    public List<Transaction> getTransactionsForBuyer(int buyerId) {
        return transactionRepository.findByBuyer_BuyerId(buyerId);
    }

    // Get transactions for a seller
    public List<Transaction> getTransactionsForSeller(int sellerId) {
        return transactionRepository.findBySeller_SellerId(sellerId);
    }
}

