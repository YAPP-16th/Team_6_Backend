package com.yapp.fmz.domain.vo;

import lombok.Data;

@Data
public class LocationVo {
    private Double x;
    private Double y;
    private Long time;

    public LocationVo(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public void convertWGS(){
        int D = (int)(x/100);
        double M = (x - D*100)/60;
        double degree = D + M;

        int D2 = (int)(y/100);
        double M2 = (y - D2*100)/60;
        double degree2 = D2 + M2;

        x = degree;
        y = degree2;
    }
}
