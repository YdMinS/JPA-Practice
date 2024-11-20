package com.ydmins.JPA_SHOP.domain.item;

import jakarta.persistence.DiscriminatorColumn;
import lombok.Getter;
import lombok.Setter;

@DiscriminatorColumn(name = "A")
@Getter
@Setter
public class Album extends Item {
    private String artist;
    private String etc;
}
