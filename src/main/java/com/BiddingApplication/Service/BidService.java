package com.BiddingApplication.Service;

import com.BiddingApplication.Model.Auction;
import com.BiddingApplication.Model.Bid;
import com.BiddingApplication.Model.Buyer;
import com.BiddingApplication.Repository.AuctionRepository;
import com.BiddingApplication.Repository.BidRepository;
import com.BiddingApplication.Repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    // Place a new bid
    public Bid placeBid(int auctionId, int buyerId, BigDecimal bidAmount) {
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

        // Check if the auction is open
        if (!auction.getAuctionStatus().equals(Auction.AuctionStatus.OPEN)) {
            throw new RuntimeException("Auction is not open for bidding");
        }

        // Get the current highest bid
        List<Bid> currentBids = bidRepository.findByAuction_AuctionIdOrderByBidAmountDesc(auctionId);
        BigDecimal highestBid = currentBids.isEmpty() ? auction.getStartBid() : currentBids.get(0).getBidAmount();

        // Check if the new bid meets the minimum increment
        if (bidAmount.compareTo(highestBid.add(auction.getMinBidIncrement())) < 0) {
            throw new RuntimeException("Bid amount must be higher than the current highest bid by at least the minimum increment");
        }

        // Save the new bid
        Bid newBid = new Bid();
        newBid.setAuction(auction);
        newBid.setBuyer(buyer);
        newBid.setBidAmount(bidAmount);
        newBid.setBidTime(LocalDateTime.now());

        return bidRepository.save(newBid);
    }

    // Get all bids for an auction
    public List<Bid> getBidsForAuction(int auctionId) {
        return bidRepository.findByAuction_AuctionIdOrderByBidAmountDesc(auctionId);
    }

    // Get the highest bid for an auction
    public Optional<Bid> getHighestBidForAuction(int auctionId) {
        List<Bid> bids = bidRepository.findByAuction_AuctionIdOrderByBidAmountDesc(auctionId);
        return bids.isEmpty() ? Optional.empty() : Optional.of(bids.get(0));
    }
}
