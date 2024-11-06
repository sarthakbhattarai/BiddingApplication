package com.BiddingApplication.Service;

import com.BiddingApplication.Model.Auction;
import com.BiddingApplication.Model.Seller;
import com.BiddingApplication.Repository.AuctionRepository;
import com.BiddingApplication.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private SellerRepository sellerRepository;

    // Create a new auction
    public Auction createAuction(int sellerId, Auction auction) {
        Optional<Seller> seller = sellerRepository.findById(sellerId);
        if (seller.isPresent()) {
            auction.setSeller(seller.get());
            auction.setAuctionStatus(Auction.AuctionStatus.OPEN);
            auction.setStartTime(LocalDateTime.now());
            return auctionRepository.save(auction);
        }
        throw new RuntimeException("Seller not found");
    }

    // Get all open auctions
    public List<Auction> getAllOpenAuctions() {
        return auctionRepository.findByAuctionStatus(Auction.AuctionStatus.OPEN);
    }

    // Get auction by ID
    public Optional<Auction> getAuctionById(int auctionId) {
        return auctionRepository.findById(auctionId);
    }

    // Close auction
    public Auction closeAuction(int auctionId) {
        Optional<Auction> auctionOptional = auctionRepository.findById(auctionId);
        if (auctionOptional.isPresent()) {
            Auction auction = auctionOptional.get();
            auction.setAuctionStatus(Auction.AuctionStatus.CLOSED);
            auction.setEndTime(LocalDateTime.now());
            return auctionRepository.save(auction);
        }
        throw new RuntimeException("Auction not found");
    }

    // Get auctions by seller
    public List<Auction> getAuctionsBySeller(int sellerId) {
        return auctionRepository.findBySellerId(sellerId);
    }
}
