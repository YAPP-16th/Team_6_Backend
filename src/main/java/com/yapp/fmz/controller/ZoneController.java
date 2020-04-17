package com.yapp.fmz.controller;

import com.yapp.fmz.domain.Address;
import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.Search;
import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.vo.LocationVo;
import com.yapp.fmz.service.ZoneService;
import kr.hyosang.coordinate.CoordPoint;
import kr.hyosang.coordinate.TransCoord;
import org.omg.PortableServer.ServantActivatorHelper;
import org.osgeo.proj4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.beans.beancontext.BeanContextChild;
import java.util.ArrayList;

@RestController
public class ZoneController {
    @Autowired
    ZoneService zoneService;

    @GetMapping("/init")
    public String test(){
        zoneService.initialData();
        return "dd";
    }

    @GetMapping("/")
    public String testRecommendZone(){
        long start = System.currentTimeMillis();

//        String tempAddress = "서울특별시 강남구 역삼동 테헤란로48길 10";
        String tempAddress = "경기도 용인시 수지구 포은대로 313번길 7-10";
        String tag = "회사";
        String transportation = "버스";
        Long transferLimit = 1L;
        Long minTime = 40L;
        Long maxTime = 50L;

        ArrayList<Zone> zones = zoneService.findZones(tempAddress, tag, transportation, transferLimit, minTime, maxTime);

        System.out.println("전체 검색 시간: " + (System.currentTimeMillis() - start)/1000 + "초");

        String data = "";
        for (Zone zone :
                zones) {
            data += zone.getZipcode();
            data += ",";
        }

        return data;
    }

    @GetMapping("/zones")
    public String recommendZone(@RequestParam("address") String address,
                                @RequestParam("addressTag") String tag,
                                @RequestParam("transportation") String transportation,
                                @RequestParam("transferLimit") Long transferLimit,
                                @RequestParam("minTime") Long minTime,
                                @RequestParam("maxTime") Long maxTime){

        String tempAddress = "서울특별시 강남구 역삼동 테헤란로48길 10";
        ArrayList<Zone> zones = zoneService.findZones(tempAddress, tag, transportation, transferLimit, minTime, maxTime);

        String data = "";

        return data;
    }
}
