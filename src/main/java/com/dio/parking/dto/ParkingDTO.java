package com.dio.parking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingDTO {

    private String id;
    private String licence;
    private String state;
    private String model;
    private String color;
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    private Double bill;

    public ParkingDTO(String id, String licence, String state, String model, String color) {
        this.id = id;
        this.licence = licence;
        this.state = state;
        this.model = model;
        this.color = color;
    }
}

