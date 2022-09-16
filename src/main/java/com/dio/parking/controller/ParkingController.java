package com.dio.parking.controller;

import com.dio.parking.dto.ParkingCreateDTO;
import com.dio.parking.dto.ParkingDTO;
import com.dio.parking.model.Parking;
import com.dio.parking.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "PARKING")
@RequestMapping("/parkings")
public class ParkingController {

    private ParkingService service;

    @GetMapping
    @Operation(summary = "Listar", description = "Listar todos os parkings.")
    public ResponseEntity<List<ParkingDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por id", description = "Buscar um parking por Id.")
    public ResponseEntity<ParkingDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Salvar", description = "Salvar um parking no banco de dados.")
    public ResponseEntity<ParkingDTO> create(@RequestBody ParkingCreateDTO parkingCreateDTO) {
        var parking = service.create(parkingCreateDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(parking.getId()).toUri();
        return ResponseEntity.created(uri).body(parking);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir", description = "Excluir um parking informando o id.")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Alterar", description = "Alterar parking informando os dados e o id do parking a ser alterado.")
    public ResponseEntity<ParkingDTO> update(@PathVariable String id, @RequestBody ParkingCreateDTO parkingCreteDTO) {
        return ResponseEntity.ok(service.update(id, parkingCreteDTO));
    }

    @PostMapping("/{id}")
    @Operation(summary = "Checkout", description = "salvar checkout.")
    public ResponseEntity<ParkingDTO> checkOut(@PathVariable String id) {
        ParkingDTO parking = service.checkOut(id);
        return ResponseEntity.ok(parking);
    }
}
