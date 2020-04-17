package com.yapp.fmz.repository;

import com.yapp.fmz.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
