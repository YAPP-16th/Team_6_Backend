package com.yapp.fmz.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String address;
    private String sido;
    private String sigungu;
    private String dong;
}
