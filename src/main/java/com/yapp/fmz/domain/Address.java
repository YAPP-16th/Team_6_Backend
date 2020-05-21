package com.yapp.fmz.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Address {
    private String address;
    private String sido;
    private String sigungu;
    private String dong;
    private Double x;
    private Double y;
}
