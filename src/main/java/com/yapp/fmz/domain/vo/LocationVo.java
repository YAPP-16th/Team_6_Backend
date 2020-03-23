package com.yapp.fmz.domain.vo;

import lombok.Data;

@Data
public class LocationVo {
    private String x;
    private String y;
    private Long time;

    public LocationVo(String x, String y, Long time) {
        this.x = x;
        this.y = y;
        this.time = time;
    }
}
