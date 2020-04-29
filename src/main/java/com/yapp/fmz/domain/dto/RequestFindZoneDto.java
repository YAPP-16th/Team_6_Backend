package com.yapp.fmz.domain.dto;

import lombok.Data;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Data
public class RequestFindZoneDto {
    private String address;
    private String addressTag;
    private List<String> transitMode;
    private Long transferLimit;
    private Long minTime;
    private Long maxTime;
}
