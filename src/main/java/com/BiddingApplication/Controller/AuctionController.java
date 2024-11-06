package com.BiddingApplication.Controller;


import com.BiddingApplication.Model.Auction;
import com.BiddingApplication.Service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    // Create an auction
    @PostMapping("/seller/{sellerId}")
    public ResponseEntity<Auction> createAuction(@PathVariable int sellerId, @RequestBody Auction auction) {
        Auction createdAuction = auctionService.createAuction(sellerId, auction);
        return ResponseEntity.ok(createdAuction);
    }

    // Get all open auctions
    @GetMapping("/open")
    public ResponseEntity<List<Auction>> getAllOpenAuctions() {
        List<Auction> openAuctions = auctionService.getAllOpenAuctions();
        return ResponseEntity.ok(openAuctions);
    }

    // Get auction by ID
    @GetMapping("/{auctionId}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable int auctionId) {
        Optional<Auction> auction = auctionService.getAuctionById(auctionId);
        return auction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Close an auction
    @PutMapping("/close/{auctionId}")
    public ResponseEntity<Auction> closeAuction(@PathVariable int auctionId) {
        Auction closedAuction = auctionService.closeAuction(auctionId);
        return ResponseEntity.ok(closedAuction);
    }

    // Get auctions by seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Auction>> getAuctionsBySeller(@PathVariable int sellerId) {
        List<Auction> sellerAuctions = auctionService.getAuctionsBySeller(sellerId);
        return ResponseEntity.ok(sellerAuctions);
    }
}

