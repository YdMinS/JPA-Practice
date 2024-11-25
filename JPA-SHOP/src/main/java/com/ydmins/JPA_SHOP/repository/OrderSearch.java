package com.ydmins.JPA_SHOP.repository;

import com.ydmins.JPA_SHOP.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
