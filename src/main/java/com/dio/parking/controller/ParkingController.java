package com.dio.parking.controller;

import com.dio.parking.dto.ParkingDTO;
import com.dio.parking.model.Parking;
import com.dio.parking.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/parkings")
public class ParkingController {

    private ParkingService service;

    @GetMapping
    public ResponseEntity<List<ParkingDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingDTO> findById(@PathVariable String id) {
        ParkingDTO parking = service.findById(id);
        return ResponseEntity.ok(parking);
    }
}
