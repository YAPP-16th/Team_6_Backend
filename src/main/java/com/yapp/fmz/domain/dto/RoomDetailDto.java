package com.yapp.fmz.domain.dto;

import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.Room;
import lombok.Data;
@Data
public class RoomDetailDto{


    private Long id;
    private String name;
    private Long zipcode;
    private String address;
    private String roomType;
    private String img;
    private String buildingType;
    private String loanType;
    private Long deposit;
    private Long registerId;
    private Long monthlyPayment;
    private Location location;

    public RoomDetailDto(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.zipcode = room.getZipcode();
        this.address = room.getAddress();
        this.img = room.getImg();
        this.buildingType = room.getBuildingType();
        this.roomType = room.getRoomType();
        this.loanType = room.getLoanType();
        this.deposit = room.getDeposit();
        this.registerId = room.getRegisterId();
        this.monthlyPayment = room.getMonthlyPayment();
        this.location = room.getLocation();
    }
}