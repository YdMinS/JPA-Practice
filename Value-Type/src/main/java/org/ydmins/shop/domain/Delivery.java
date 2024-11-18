package org.ydmins.shop.domain;

import javax.persistence.*;

@Entity
public class Delivery extends BaseEntity{

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;
    
    private DeliveryStatus status;

    @OneToOne(mappedBy = "delivery")
    private Order order;
}
