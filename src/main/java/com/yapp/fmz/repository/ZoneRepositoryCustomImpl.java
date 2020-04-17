package com.yapp.fmz.repository;

import com.yapp.fmz.domain.Room;
import com.yapp.fmz.domain.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class ZoneRepositoryCustomImpl implements ZoneRepositoryCustom {

    @Override
    @Transactional
    public void initialData() {

    }
}
