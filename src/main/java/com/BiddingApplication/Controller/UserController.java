package com.BiddingApplication.Controller;

import com.BiddingApplication.Model.Buyer;
import com.BiddingApplication.Model.LogIn.*;
import com.BiddingApplication.Model.Enum.Role;
import com.BiddingApplication.Model.Seller;
import com.BiddingApplication.Model.User;
import com.BiddingApplication.Register.UserRegistrationDto;
import com.BiddingApplication.Repository.BuyerRepository;
import com.BiddingApplication.Repository.SellerRepository;
import com.BiddingApplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (user != null) {
            String message = "Login successful";
            String role = user.getRole().name();

            switch (role) {
                case "BUYER":
                    return ResponseEntity.ok(new LoginResponse(message, "Redirect to Buyer Dashboard"));
                case "SELLER":
                    return ResponseEntity.ok(new LoginResponse(message, "Redirect to Seller Dashboard"));
                case "ADMIN":
                    return ResponseEntity.ok(new LoginResponse(message, "Redirect to Admin Dashboard"));
                default:
                    return ResponseEntity.ok(new LoginResponse(message, "Redirect to User Home"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid credentials", ""));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userDto) {
        if (userService.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered");
        }

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setEmail(userDto.getEmail());
        newUser.setPhoneNumber(userDto.getPhoneNumber());
        newUser.setPassword(userService.encodePassword(userDto.getPassword()));
        newUser.setRole(userDto.getRole());
        newUser.setIsActive(true);

        userService.save(newUser);

        if (userDto.getRole() == Role.BUYER) {
            Buyer newBuyer = new Buyer();
            newBuyer.setUser(newUser);
            newBuyer.setPaymentMethod("Default Method"); // Set default payment method
            newBuyer.setBuyerRating(BigDecimal.ZERO); // Default rating
            buyerRepository.save(newBuyer);
        }

        if (userDto.getRole() == Role.SELLER) {
            Seller newSeller = new Seller();
            newSeller.setUser(newUser);
            newSeller.setCompanyName("Default Company"); // Set default company name
            newSeller.setTotalSales(0); // Initialize total sales
            newSeller.setVerified(false); // Not verified by default
            newSeller.setSellerRating(BigDecimal.ZERO); // Default rating
            sellerRepository.save(newSeller);
        }

        return ResponseEntity.ok("User registered successfully");
    }
}
