package com.yapp.fmz.domain.dto;

import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.Room;
import lombok.Data;

import javax.persistence.Embedded;
@Data
public class RoomDto {
    private Long id;
    private Long zipcode;
    private String address;
    private Location location;

    public RoomDto(Room room) {
        this.id = room.getId();
        this.zipcode = room.getZipcode();
        this.address = room.getAddress();
        this.location = room.getLocation();
    }
}
