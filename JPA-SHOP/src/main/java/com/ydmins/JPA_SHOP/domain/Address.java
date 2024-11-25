package com.ydmins.JPA_SHOP.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
