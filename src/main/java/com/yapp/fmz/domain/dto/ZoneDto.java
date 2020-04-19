package com.yapp.fmz.domain.dto;

import com.yapp.fmz.domain.Address;
import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.Room;
import com.yapp.fmz.domain.Zone;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class ZoneDto {

    private Long id;
    private Long zipcode;
    private Address address;
    private Location location;
    private String polygon;
    private List<RoomDto> rooms = new ArrayList<>();

    public ZoneDto(Zone zone) {
        this.id = zone.getId();
        this.zipcode = zone.getZipcode();
        this.address = zone.getAddress();
        this.location = zone.getLocation();
        this.polygon = zone.getPolygon();
        this.rooms = zone.getRooms().stream()
                .map(RoomDto::new)
                .collect(toList());
    }
}
