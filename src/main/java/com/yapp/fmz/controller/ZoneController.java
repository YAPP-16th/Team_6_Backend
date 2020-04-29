package com.yapp.fmz.controller;

import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.dto.RequestFindZoneDto;
import com.yapp.fmz.domain.dto.ZoneDto;
import com.yapp.fmz.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
public class ZoneController {
    @Autowired
    ZoneService zoneService;

    @GetMapping("/init")
    public String test(){
        zoneService.initialZoneToRoomData();
        zoneService.initialAddressData();
        return "DB INITIALIZED SUCCESS";
    }

    @ResponseBody
    @GetMapping("/zones")
    public HashMap<String, Object> recommendZone(@RequestBody RequestFindZoneDto requestZone) {

        long time = System.currentTimeMillis();

        HashMap<String, Object> response = new HashMap<>();
        List<Zone> zones = zoneService.findZones(requestZone.getAddress(), requestZone.getAddressTag(), requestZone.getTransitMode(), requestZone.getTransferLimit(), requestZone.getMinTime()-2, requestZone.getMaxTime()+2);
        List<ZoneDto> data = zones.stream().map(ZoneDto::new).sorted().collect(toList());
        if(data.size() > 0){
            response.put("code", 200);
            response.put("message", "정상");
            response.put("data", data);

        }else{
            response.put("code", 404);
            response.put("message", "해당 조건의 기초구역이 존재하지 않습니다.");
        }
        System.out.println("전체 api 수행시간: " + (System.currentTimeMillis() - time) / 1000 + "초");
        return response;
    }

    @ResponseBody
    @GetMapping("/test")
    public HashMap<String, Object> testServerRecommendZone(@RequestBody RequestFindZoneDto requestZone) {

        HashMap<String, Object> response = new HashMap<>();
        List<Zone> zones = zoneService.findOnlyRecommendZones(requestZone.getAddress(), requestZone.getAddressTag(), requestZone.getTransitMode(), requestZone.getTransferLimit(), requestZone.getMinTime()-2, requestZone.getMaxTime()+2);
        List<ZoneDto> data = zones.stream().map(ZoneDto::new).sorted().collect(toList());
        if(data.size() > 0){
            response.put("code", 200);
            response.put("message", "정상");
            response.put("data", data);

        }else{
            response.put("code", 404);
            response.put("message", "해당 조건의 기초구역이 존재하지 않습니다.");
        }
        return response;
    }

    @ResponseBody
    @GetMapping("/test/zones")
    public HashMap<String, Object> testRecommendZone(@RequestBody RequestFindZoneDto requestZone) {

        HashMap<String, Object> response = new HashMap<>();
        List<Zone> zones = zoneService.findTestZones();
        List<ZoneDto> data = zones.stream().map(ZoneDto::new).sorted().collect(toList());
        if(data.size() > 0){
            response.put("code", 200);
            response.put("message", "정상");
            response.put("data", data);

        }else{
            response.put("code", 404);
            response.put("message", "해당 조건의 기초구역이 존재하지 않습니다.");
        }
        return response;
    }
}
