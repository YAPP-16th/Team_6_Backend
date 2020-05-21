package com.yapp.fmz.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Location {
    Double lat;
    Double lng;

    public Location(Double x, Double y){
        lat = x;
        lng = y;
    }
}
