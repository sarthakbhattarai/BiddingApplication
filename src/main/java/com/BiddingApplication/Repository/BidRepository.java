package com.BiddingApplication.Repository;

import com.BiddingApplication.Model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {
    List<Bid> findByAuction_AuctionIdOrderByBidAmountDesc(int auctionId);  // Get bids by auction, ordered by amount
}
