package com.BiddingApplication.Register;

import com.BiddingApplication.Model.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role; // Assuming you have a Role enum
}
