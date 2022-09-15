package com.dio.parking.service;

import com.dio.parking.controller.mapper.ParkingMapper;
import com.dio.parking.dto.ParkingDTO;
import com.dio.parking.model.Parking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingMapper parkingMapper;

    private static Map<String, Parking> parkingMap = new HashMap<>();
    static{
        var id = getUUID();
        Parking parking = new Parking(id,"DMS-111","SC","Celta","Preto");
        parkingMap.put(id, parking);
    }


    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public List<ParkingDTO> findAll(){
        List<Parking>  parkingList =  parkingMap.values().stream().collect(Collectors.toList());
        return parkingMapper.toParkingDTOList(parkingList);
    }
}
