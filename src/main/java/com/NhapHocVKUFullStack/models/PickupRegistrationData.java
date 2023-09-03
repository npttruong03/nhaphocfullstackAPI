package com.NhapHocVKUFullStack.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickupRegistrationData {
    private String name;
    private String email;
    private String phoneNumber;
    private String pickupDate;
    private String pickupTime;
    
    // Getters and setters
    //...
}

