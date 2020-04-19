package com.yapp.fmz.controller;

import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.dto.ZoneDto;
import com.yapp.fmz.service.ZoneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;

@SpringBootTest
class ZoneControllerTest {

    @Autowired
    private ZoneService zoneService;

    @Test
    public void testSimpleZoneApi(){
        long start = System.currentTimeMillis();

        String tempAddress1 = "서울특별시 강남구 역삼동 테헤란로48길 10";
        String tempAddress2 = "경기도 용인시 수지구 포은대로 313번길 7-10";
        String tag = "회사";
        List<String> travelMode = new ArrayList<>();
        travelMode.add("transit");
        Long transferLimit = 0L;
        Long minTime = 40L;
        Long maxTime = 50L;

        ArrayList<Zone> zones = zoneService.findZones(tempAddress1, tag, travelMode, transferLimit, minTime, maxTime);

        long apiTotalTime = (System.currentTimeMillis() - start) / 1000;
        System.out.println("전체 검색 시간: " + apiTotalTime + "초");

        assertTrue("api 전체 시간이 15초보다 작아야 한다", apiTotalTime <= 15);
        List<ZoneDto> collect = zones.stream().map(ZoneDto::new).collect(Collectors.toList());
        System.out.println(collect.toString());
    }

}