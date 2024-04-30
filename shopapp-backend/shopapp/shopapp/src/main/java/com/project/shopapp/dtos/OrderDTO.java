package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 0, message = "User ID must be >= 0")
    private Long userId;

    @JsonProperty("fullname")
    @NotBlank(message = "Full Name is required")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, message = "Phone number at least 10 numbers")
    private String phoneNumber;

    @NotBlank(message = "Your address is required")
    private String address;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be > 0")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate ShippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;
}
