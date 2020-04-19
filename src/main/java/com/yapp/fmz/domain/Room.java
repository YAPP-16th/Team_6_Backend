package com.yapp.fmz.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(exclude = "zone")
@Table(name = "room")
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    // 매물 이름
    private String name;

    // 기초구역번호(우편번호)
    private Long zipcode;

     //매물 주소
    private String address;

    // 매물 좌표
    @Embedded
    private Location location;

    // 방 종류(원룸, 투름...)
    private String roomType;

    // 매물 종류(월세, 전세, 매매)
    private String loanType;

    // 보증금(전세일경우 전세보증금, 월세일경우 일반 보증금)
    private Long deposit;

    // 월세, 전세일 경우 0
    private Long monthlyPayment;

    private Long registerId;

    // zone이 모두 db에 쌓이면 우편번호별로 매물을 미리 넣어놓기 위한 변수
    // 크롤링할때는 신경 안쓰셔도 됩니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
