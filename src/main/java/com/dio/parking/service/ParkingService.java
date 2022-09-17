package com.dio.parking.service;

import com.dio.parking.controller.mapper.ParkingMapper;
import com.dio.parking.dto.ParkingCreateDTO;
import com.dio.parking.dto.ParkingDTO;
import com.dio.parking.exception.ParkingNotFoundException;
import com.dio.parking.model.Parking;
import com.dio.parking.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingRepository repository;
    private final ParkingMapper parkingMapper;

/*    private static Map<String, Parking> parkingMap = new HashMap<>();
    static{
        var id = getUUID();
        Parking parking = new Parking(id,"DMS-111","SC","Celta","Preto");
        parkingMap.put(id, parking);
    }*/


    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

/*    public List<ParkingDTO> findAll(){
        List<Parking>  parkingList =  parkingMap.values().stream().collect(Collectors.toList());
        return parkingMapper.toParkingDTOList(parkingList);
    }*/

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<ParkingDTO> findAll() {
        return parkingMapper.toParkingDTOList(repository.findAll());
    }

    public ParkingDTO findById(String id) {
        Parking parking = repository.findById(id).orElseThrow(() ->
                new ParkingNotFoundException(id));
        return parkingMapper.toParkingDTO(parking);
    }

    public ParkingDTO create(ParkingCreateDTO parkingCreateDTO) {
        var parkingCreate = parkingMapper.toParkingCreate(parkingCreateDTO);
        parkingCreate.setId(getUUID());
        parkingCreate.setEntryDate(LocalDateTime.now());
        parkingCreate = repository.save(parkingCreate);
        return parkingMapper.toParkingDTO(parkingCreate);
    }

    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }

    public ParkingDTO update(String id, ParkingCreateDTO parkingCreateDTO) {
        ParkingDTO parkingDTO = findById(id);
        Parking parking = parkingMapper.toParking(parkingDTO);
        parking.setColor(parkingCreateDTO.getColor());
        parking.setState(parkingCreateDTO.getState());
        parking.setModel(parkingCreateDTO.getModel());
        parking.setLicense(parkingCreateDTO.getLicense());
        return parkingMapper.toParkingDTO(repository.save(parking));
    }

    @Transactional
    public ParkingDTO checkOut(String id) {
        ParkingDTO parkingDTO = findById(id);
        Parking parking = parkingMapper.toParking(parkingDTO);
        parking.setExitDate(LocalDateTime.now());
        parking.setBill(ParkingCheckOut.getBill(parking));
        return parkingMapper.toParkingDTO(repository.save(parking));
    }

}
