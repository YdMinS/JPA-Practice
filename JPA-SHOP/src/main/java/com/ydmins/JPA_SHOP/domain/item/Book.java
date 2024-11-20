package com.ydmins.JPA_SHOP.domain.item;

import jakarta.persistence.DiscriminatorColumn;
import lombok.Getter;
import lombok.Setter;

@DiscriminatorColumn(name = "B")
@Getter
@Setter
public class Book extends Item {
    private String author;
    private String isbn;
}
