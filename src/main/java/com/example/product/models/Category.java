package com.example.product.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity

public class Category extends BaseModel {

    private String name;
}
