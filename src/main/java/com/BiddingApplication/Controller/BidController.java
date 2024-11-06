package com.BiddingApplication.Controller;


import com.BiddingApplication.Model.Bid;
import com.BiddingApplication.Service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    // Place a new bid
    @PostMapping("/auction/{auctionId}/buyer/{buyerId}")
    public ResponseEntity<Bid> placeBid(@PathVariable int auctionId,
                                        @PathVariable int buyerId,
                                        @RequestParam BigDecimal bidAmount) {
        Bid newBid = bidService.placeBid(auctionId, buyerId, bidAmount);
        return ResponseEntity.ok(newBid);
    }

    // Get all bids for an auction
    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<Bid>> getBidsForAuction(@PathVariable int auctionId) {
        List<Bid> bids = bidService.getBidsForAuction(auctionId);
        return ResponseEntity.ok(bids);
    }

    // Get the highest bid for an auction
    @GetMapping("/auction/{auctionId}/highest")
    public ResponseEntity<Bid> getHighestBidForAuction(@PathVariable int auctionId) {
        Optional<Bid> highestBid = bidService.getHighestBidForAuction(auctionId);
        return highestBid.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

