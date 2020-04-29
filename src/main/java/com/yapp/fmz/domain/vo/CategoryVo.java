package com.yapp.fmz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryVo {
    private String categoryName;
    private Long size;
    private List<PlaceVo> placeVoList;
}
