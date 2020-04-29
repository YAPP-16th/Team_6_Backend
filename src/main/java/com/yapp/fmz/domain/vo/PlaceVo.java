package com.yapp.fmz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceVo {
    private String categoryName;
    private String placeName;
    private String address;
    private String distance;
}
