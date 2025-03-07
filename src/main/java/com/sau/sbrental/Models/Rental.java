package com.sau.sbrental.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "rental")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Temporal(TemporalType.DATE)
    @Column(name = "rent_date", nullable = false)
    private Date rentDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "return_date")
    private Date returnDate;

    @Column(name = "is_returned", nullable = false)
    private boolean isReturned = false;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "package_type", nullable = false)
    private String packageType;

    @Column(name = "price", nullable = false)
    private double price;
}