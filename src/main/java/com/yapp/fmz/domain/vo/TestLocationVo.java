package com.yapp.fmz.domain.vo;

import lombok.Data;

@Data
public class TestLocationVo {
    private Double x;
    private Double y;

    public TestLocationVo(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}