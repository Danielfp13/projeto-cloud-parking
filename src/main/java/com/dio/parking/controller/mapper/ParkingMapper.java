package com.dio.parking.controller.mapper;

import com.dio.parking.dto.ParkingCreateDTO;
import com.dio.parking.dto.ParkingDTO;
import com.dio.parking.model.Parking;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParkingMapper {

    private static final ModelMapper modellMapper = new ModelMapper();

    public ParkingDTO parkingDTO(Parking parking){
        return modellMapper.map(parking, ParkingDTO.class);
    }

    public List<ParkingDTO> toParkingDTOList(List<Parking> parkingList) {
        return parkingList.stream().map(this::parkingDTO).collect(Collectors.toList());
    }

    public ParkingDTO toParkingDTO(Parking parking) {
        return modellMapper.map(parking, ParkingDTO.class);
    }

    public Parking toParkingCreate(ParkingCreateDTO dto) {
        return modellMapper.map(dto, Parking.class);
    }

    public Parking toParking(ParkingDTO dto) {
        return modellMapper.map(dto, Parking.class);
    }
}
