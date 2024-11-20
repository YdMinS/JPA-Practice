package com.ydmins.JPA_SHOP.domain.item;

import jakarta.persistence.DiscriminatorColumn;
import lombok.Getter;
import lombok.Setter;

@DiscriminatorColumn(name = "M")
@Getter
@Setter
public class Movie extends Item {

    private String director;
    private String actor;
}
