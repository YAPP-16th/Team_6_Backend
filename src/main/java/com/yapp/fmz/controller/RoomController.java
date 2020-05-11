package com.yapp.fmz.controller;

import com.yapp.fmz.domain.Room;
import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.dto.RoomDto;
import com.yapp.fmz.domain.dto.ZoneDto;
import com.yapp.fmz.repository.RoomRepository;
import com.yapp.fmz.service.RoomService;
import com.yapp.fmz.service.ZoneService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController
@Api(value = "RoomController V1")
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    RoomRepository roomRepository;

    @ApiOperation(value = "가격순 매물정보", notes = "ZONE ID 기준 가격순 매물 정보입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "매물 데이터를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "해당 조건의 매물이 존재하지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @GetMapping("/rooms/byPrice/")
    public HashMap<String, Object> RoomsByMonthlyPayment(@ApiParam(value = "ZONE ID", required = true, example = "3776") @RequestParam("zoneId") Long zone_id) throws Exception{
        HashMap<String, Object> response = new HashMap<String, Object>();

        List<Room> roomList = roomService.findRoomsByMonthlyPayment(zone_id);
        List<RoomDto> data = roomList.stream().map(name -> new RoomDto(name)).collect(Collectors.toList());
        if (roomList.size() ==0){
            response.put("code", 300);
            response.put("message", "해당 조건의 매물이 존재하지 않습니다.");
        }else {
            response.put("code", 200);
            response.put("message", "매물 데이터를 정상적으로 가져왔습니다.");
            response.put("data", data);
        }


        return response;

    }

    @ApiOperation(value = "등록번호순 매물정보", notes = "ZONE ID 기준 등록번호순 매물 정보입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "매물 데이터를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "해당 조건의 매물이 존재하지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @GetMapping("/rooms/byRegistration")
    public HashMap<String, Object> RoomsByRegisterdId(@ApiParam(value = "ZONE ID", required = true, example = "3776") @RequestParam("zoneId") Long zone_id){
        HashMap<String, Object> response = new HashMap<String, Object>();

        List<Room> roomList = roomService.findRoomsByRegisterdId(zone_id);
        List<RoomDto> data = roomList.stream().map(name -> new RoomDto(name)).collect(Collectors.toList());
        if (roomList.size() == 0) {
            response.put("code", 300);
            response.put("message", "해당 조건의 매물이 존재하지 않습니다.");
        } else {
            response.put("code", 200);
            response.put("message", "매물 데이터를 정상적으로 가져왔습니다.");
            response.put("data", data);
        }

        return response;

    }





}
