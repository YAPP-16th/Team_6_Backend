package com.yapp.fmz.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "rooms")
@Table(name = "zone")
public class Zone {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Long id;

    private Long zipcode;

    @Embedded
    private Address address;

    @Embedded
    private Location location;

    private String type;

    private String polygon;

    private String polygonJson;

    @OneToMany(mappedBy = "zone")
    private List<Room> rooms = new ArrayList<>();

    @Transient
    private Long time;

    @Transient
    private Long distance;

    public void setFullAddress(String address){
        Address address1 = this.getAddress();
        address1.setAddress(address);
        this.setAddress(address1);
    }

    public void setConvertLocation(Double x, Double y){
        this.address.setX(x);
        this.address.setY(y);
    }
}
