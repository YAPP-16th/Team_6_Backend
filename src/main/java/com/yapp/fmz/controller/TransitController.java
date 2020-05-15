package com.yapp.fmz.controller;


import com.yapp.fmz.service.TransitService;
import com.yapp.fmz.service.ZoneService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Api(value = "TransitController V1")
public class TransitController {

    @Autowired
    TransitService transitService;

    @Autowired
    ZoneService zoneService;

    @CrossOrigin("*")
    @ApiOperation(value = "교통 정보", notes = "요청 위치와 ZONE 사이 대중교통 정보를 제공합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "교통 정보를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "해당 조건의 경로 정보가 제공되지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @GetMapping("/transit/LocationToZone")
    public HashMap<String, Object> TransitLocationToZoneId(@ApiParam(value = "출발지 lat 좌표", required = false, example = "37.572500") @RequestParam("startLat") Double startLat,
                                                           @ApiParam(value = "출발지 lng 좌표", required = false, example = "126.820454")@RequestParam("startLng") Double startLng,
                                                           @ApiParam(value = "Destination zone ID", required = false, example = "3774") @RequestParam("destinationZoneId") Long destinationZondId) throws Exception {
        HashMap<String,Object> result = new HashMap<>();

        ArrayList data = transitService.findTransitbyDestinationZoneId(startLat, startLng, destinationZondId);

        if (data.size()>0){
            result.put("code",200);
            result.put("message", "교통 정보를 정상적으로 가져왔습니다.");
            result.put("data",data);

        }else {
            result.put("code",300);
            result.put("message", "해당 조건의 경로 정보가 제공되지 않습니다.");
        }

        return result;
    }



    @CrossOrigin("*")
    @ApiOperation(value = "교통 정보", notes = "요청 위치와 위치 사이 대중교통 정보를 제공합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "교통 정보를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "해당 조건의 경로 정보가 제공되지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @GetMapping("/transit/LocationToLocation")
    public HashMap<String, Object> TransitLocationToLocation(@ApiParam(value = "출발지 lat 좌표", required = false, example = "37.572500") @RequestParam("startLat") Double startLat,
                                                             @ApiParam(value = "출발지 lng 좌표", required = false, example = "126.820454")@RequestParam("startLng") Double startLng,
                                                             @ApiParam(value = "목적지 lng 좌표", required = false, example = "37.672500")@RequestParam("destinationLat") Double destinationLat,
                                                             @ApiParam(value = "목적지 lng 좌표", required = false, example = "126.720454")@RequestParam("destinationLng") Double destinationLng) throws Exception {
        HashMap<String,Object> result = new HashMap<>();

        ArrayList data = transitService.findTransitbyLocation(startLat, startLng, destinationLat,destinationLng);

        if (data.size()>0){
            result.put("code",200);
            result.put("message", "교통 정보를 정상적으로 가져왔습니다.");
            result.put("data",data);

        }else {
            result.put("code",300);
            result.put("message", "해당 조건의 경로 정보가 제공되지 않습니다.");
        }

        return result;
    }


    @CrossOrigin("*")
    @ApiOperation(value = "교통 정보", notes = "요청 ZONE과 위치 사이 대중교통 정보를 제공합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "교통 정보를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "해당 조건의 경로 정보가 제공되지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @GetMapping("/transit/ZoneToLocation")
    public HashMap<String, Object> TransitZoneIdToLocation(@ApiParam(value = "Start zone ID", required = false, example = "3774") @RequestParam("startZoneId") Long startZondId,
                                                           @ApiParam(value = "목적지 lat 좌표", required = false, example = "37.572500")@RequestParam("destinationLat") Double destinationLat,
                                                           @ApiParam(value = "목적지 lng 좌표", required = false, example = "126.820454")@RequestParam("destinationLng") Double destinationLng) throws Exception {
        HashMap<String,Object> result = new HashMap<>();

        ArrayList data = transitService.findTransitbyStartZoneId(startZondId, destinationLat,destinationLng);

        if (data.size()>0){
            result.put("code",200);
            result.put("message", "교통 정보를 정상적으로 가져왔습니다.");
            result.put("data",data);

        }else {
            result.put("code",300);
            result.put("message", "해당 조건의 경로 정보가 제공되지 않습니다.");
        }

        return result;
    }
}