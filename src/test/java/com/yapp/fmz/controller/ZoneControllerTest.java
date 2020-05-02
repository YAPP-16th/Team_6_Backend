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

//    @Test
//    public void testSimpleZoneApi(){
//        long start = System.currentTimeMillis();
//
////        String tempAddress = "서울특별시 강남구 역삼동 테헤란로48길 10";
//        String tempAddress = "경기도 용인시 수지구 포은대로 313번길 7-10";
//        String tag = "회사";
//        List<String> transitMode = new ArrayList<>();
//        transitMode.add("bus");
//        transitMode.add("subway");
//        Long transferLimit = 3L;
//        Long minTime = 0L;
//        Long maxTime = 50L;
//
//        List<Zone> zones = zoneService.findZones(tempAddress, tag, transitMode, transferLimit, minTime, maxTime);
//        System.out.println("최종 추천 기초구역 : " + zones.size());
//
//        long apiTotalTime = (System.currentTimeMillis() - start) / 1000;
//        System.out.println("전체 검색 시간: " + apiTotalTime + "초");
//
//        assertTrue("api 전체 시간이 15초보다 작아야 한다", apiTotalTime <= 30);
//        List<ZoneDto> collect = zones.stream().map(ZoneDto::new).collect(Collectors.toList());
//
//    }

}