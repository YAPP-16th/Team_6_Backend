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

    public RequestFindZoneDto() {
    }

    public RequestFindZoneDto(String address, String addressTag, List<String> transitMode, Long transferLimit, Long minTime, Long maxTime) {
        this.address = address;
        this.addressTag = addressTag;
        this.transitMode = transitMode;
        this.transferLimit = transferLimit;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }
}
