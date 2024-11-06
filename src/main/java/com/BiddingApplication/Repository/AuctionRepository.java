package com.BiddingApplication.Repository;

import com.BiddingApplication.Model.Auction;
import com.BiddingApplication.Model.Auction.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    List<Auction> findByAuctionStatus(AuctionStatus auctionStatus);
    List<Auction> findBySellerId(int sellerId);
}
