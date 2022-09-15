package com.dio.parking.controller;

import com.dio.parking.dto.ParkingCreateDTO;
import com.dio.parking.dto.ParkingDTO;
import com.dio.parking.model.Parking;
import com.dio.parking.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/parkings")
public class ParkingController {

    private ParkingService service;

    @GetMapping
    public ResponseEntity<List<ParkingDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingDTO> findById(@PathVariable String id) {
        ParkingDTO parking = service.findById(id);
        return ResponseEntity.ok(parking);
    }

    @PostMapping
    public ResponseEntity<ParkingDTO> create(@RequestBody ParkingCreateDTO parkingCreateDTO) {
        var parking = service.create(parkingCreateDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(parking.getId()).toUri();
        return ResponseEntity.created(uri).body(parking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingDTO> update(@PathVariable String id, @RequestBody ParkingCreateDTO parkingCreteDTO) {
        return ResponseEntity.ok(service.update(id, parkingCreteDTO));
    }
}