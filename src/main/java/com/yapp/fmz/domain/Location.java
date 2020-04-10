package com.yapp.fmz.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Location {
    Float lat;
    Float lng;
}
