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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "매물정보", notes = "기초구역 기준 매물 정보입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "매물 데이터를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "해당 조건의 매물이 존재하지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @GetMapping("/rooms")
    public HashMap<String, Object> test11(@ApiParam(value = "기초구역번호", required = true, example = "3776") @RequestParam("zoneId") Long zone_id){
        HashMap<String, Object> response = new HashMap<String, Object>();
        List<Room> roomList = roomService.findRooms(zone_id);
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
}
