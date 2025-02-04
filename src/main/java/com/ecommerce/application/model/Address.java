package com.ecommerce.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters")
    @Column(name = "street")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 characters")
    @Column(name = "building_name")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "City name must be at least 4 characters")
    @Column(name = "city")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must be at least 2 characters")
    @Column(name = "state")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters")
    @Column(name = "country")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pin Code name must be at least 6 characters")
    @Column(name = "pin_code")
    private String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
