package com.dio.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ParkingNotfoundException extends RuntimeException{
    public ParkingNotfoundException(String id) {
        super("Parking not found with Id: " + id);
    }
}
