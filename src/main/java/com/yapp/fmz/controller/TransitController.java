package com.yapp.fmz.controller;


import com.yapp.fmz.service.TransitService;
import com.yapp.fmz.service.ZoneService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation(value = "교통 정보", notes = "요청 위치와 ZONE 사이 대중교통 정보를 제공합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "교통 정보를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "해당 조건의 경로 정보가 제공되지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @GetMapping("/transit")
    public HashMap<String, Object> Transit(@ApiParam(value = "위치 lat 좌표", required = true, example = "37.572500") @RequestParam("lat") Double lat,
                                        @ApiParam(value = "위치 lng 좌표", required = true, example = "126.820454")@RequestParam("lng") Double lng,
                                        @ApiParam(value = "zone ID", required = true, example = "3774") @RequestParam("zoneId") Long zondId) throws Exception {
        HashMap<String,Object> result = new HashMap<>();

        ArrayList data = transitService.findTransit(lat,lng,zondId);

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
