package com.yapp.fmz.domain;

import javax.persistence.*;

@Entity
@Table(name = "search")
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long id;

    @Embedded
    private Address address;

    @Embedded
    private Location location;

    private String tag;
    private String transportation;
    private Long transferLimit;
    private Long minTime;
    private Long maxTime;
    private String data;

    public Search() {
    }

    public Search(Address address, Location location, String tag, String transportation, Long transferLimit, Long minTime, Long maxTime){
        this.address = address;
        this.location = location;
        this.tag = tag;
        this.transportation = transportation;
        this.transferLimit = transferLimit;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

}
