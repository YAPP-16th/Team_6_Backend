package com.yapp.fmz.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Location {
    Double lat;
    Double lng;
}
