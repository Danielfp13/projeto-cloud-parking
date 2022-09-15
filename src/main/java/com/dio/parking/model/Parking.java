package com.dio.parking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Parking {

    @Id
    private String id;
    private String licence;
    private String state;
    private String model;
    private String color;
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    private Double bill;

    public Parking(String id, String licence, String state, String model, String color) {
        this.id = id;
        this.licence = licence;
        this.state = state;
        this.model = model;
        this.color = color;
    }
}
