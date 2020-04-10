package com.yapp.fmz.domain;

import javax.persistence.*;

@Entity
@Table(name = "zone")
public class Zone {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Long id;

    private Long zipcode;

    private Address address;

    @Embedded
    private Location location;
}
