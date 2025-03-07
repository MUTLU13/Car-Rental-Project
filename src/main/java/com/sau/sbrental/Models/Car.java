package com.sau.sbrental.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;

@Entity
@Table(name = "car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false, unique = true)
    private String plate;

    @Column(nullable = false)
    private boolean available = true;

    @Lob
    @Column(nullable = true, columnDefinition = "LONGBLOB")
    private byte[] image; // Araç resmi

    @Transient // Veritabanına kaydedilmez, sadece gösterim için
    private String imageBase64;

    public String getImageBase64() {
        return (image != null) ? Base64.getEncoder().encodeToString(image) : null;
    }
}
