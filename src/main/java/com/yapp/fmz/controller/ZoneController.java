package com.yapp.fmz.controller;

import com.yapp.fmz.domain.Address;
import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.Search;
import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.dto.ZoneDto;
import com.yapp.fmz.domain.vo.LocationVo;
import com.yapp.fmz.service.ZoneService;
import org.omg.PortableServer.ServantActivatorHelper;
import org.osgeo.proj4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.beans.beancontext.BeanContextChild;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
public class ZoneController {
    @Autowired
    ZoneService zoneService;

    @GetMapping("/init")
    public String test(){
        zoneService.initialData();
        return "DB INITIALIZED SUCCESS";
    }

    @GetMapping("/zones")
    public List<ZoneDto> recommendZone(@RequestParam("address") String address,
                                       @RequestParam("addressTag") String tag,
                                       @RequestParam("travelMode") List<String> travelMode,
                                       @RequestParam("transferLimit") Long transferLimit,
                                       @RequestParam("minTime") Long minTime,
                                       @RequestParam("maxTime") Long maxTime){

        List<Zone> zones = zoneService.findZones(address, tag, travelMode, transferLimit, minTime, maxTime);

        return zones.stream()
                .map(ZoneDto::new)
                .collect(Collectors.toList());
    }
}
