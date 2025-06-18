package com.example.ecommerce_project.model;

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
@NoArgsConstructor
@Data
@Table(name = "addresses")
@AllArgsConstructor
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5,message = "Street must be filled with 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5,message = "Building must be filled with 5 characters")
    private String building;

    @NotBlank
    @Size(min = 5,message = "City must be filled with 5 characters")
    private String city;

    @NotBlank
    @Size(min = 2,message = "State must be filled with 2 characters")
    private String state;

    @NotBlank
    @Size(min = 2,message = "Country must be filled with 2 characters")
    private String country;

    @NotBlank
    @Size(min = 5,message = "pincode must be filled with 5 characters")
    private String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String building, String city, String state, String country, String pincode) {
        this.street = street;
        this.building = building;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
