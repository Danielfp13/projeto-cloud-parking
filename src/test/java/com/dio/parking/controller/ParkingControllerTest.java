package com.dio.parking.controller;

import com.dio.parking.CloundParkingApplication;
import com.dio.parking.config.SecurityConfiguration;
import com.dio.parking.dto.ParkingCreateDTO;
import com.dio.parking.dto.ParkingDTO;
import com.dio.parking.exception.ParkingNotFoundException;
import com.dio.parking.service.ParkingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ParkingController.class)
@ContextConfiguration(classes = {CloundParkingApplication.class, SecurityConfiguration.class})
public class ParkingControllerTest {

    static String BOOK_API = "/parkings";

    @Autowired
    MockMvc mvc;

    @MockBean
    private ParkingService parkingService;

    @Test
    // @WithMockUser(username = "user", password = "$2a$10$5CIdivekmhGur24bViGn0OA6XlYyLmzG6NI6JKVa4H/yohe0/gY6K", roles = "USER")
    @DisplayName("Deve criar um parking com sucesso.")
    public void createParkingTest() throws Exception {

        ParkingCreateDTO parkingCreateDTO = parkingCreateDTO();
        ParkingDTO parkingDTO = new ParkingDTO("5bba37a03b4547efabedd104de9fb280", "license", "state",
                "model", "Color", LocalDateTime.now().minusDays(1), LocalDateTime.now(), 23.0);

        BDDMockito.given(parkingService.create(Mockito.any(ParkingCreateDTO.class))).willReturn(parkingDTO);

        String json = new ObjectMapper().writeValueAsString(parkingCreateDTO);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request.with(httpBasic("user", "password")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value("5bba37a03b4547efabedd104de9fb280"))
                .andExpect(jsonPath("license").value(parkingDTO.getLicense()))
                .andExpect(jsonPath("state").value(parkingDTO.getState()))
                .andExpect(jsonPath("model").value(parkingDTO.getModel()))
                .andExpect(jsonPath("color").value(parkingDTO.getColor()))
                .andExpect(jsonPath("bill").value(parkingDTO.getBill()))
        ;
        Mockito.verify(parkingService, Mockito.times(0)).create(parkingCreateDTO);
    }

    @Test
    @DisplayName("Deve obter as informa��es de um parking por id")
    public void getParkingDetailsTest() throws Exception {

        ParkingDTO parkingDTO = new ParkingDTO("5bba37a03b4547efabedd104de9fb280", "license", "state",
                "model", "Color", LocalDateTime.now().minusDays(1), LocalDateTime.now(), 23.0);

        BDDMockito.given(parkingService.findById(parkingDTO.getId())).willReturn(parkingDTO);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + parkingDTO.getId()))
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(request.with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("5bba37a03b4547efabedd104de9fb280"))
                .andExpect(jsonPath("license").value(parkingDTO.getLicense()))
                .andExpect(jsonPath("state").value(parkingDTO.getState()))
                .andExpect(jsonPath("model").value(parkingDTO.getModel()))
                .andExpect(jsonPath("color").value(parkingDTO.getColor()))
                .andExpect(jsonPath("bill").value(parkingDTO.getBill()))
        ;
    }

    @Test
    @DisplayName("Deve retornar um not found se n�o existe parking com id informado.")
    public void parkingNotFound() throws Exception {
        String id = "1";

        BDDMockito.given(parkingService.findById(Mockito.anyString()))
                .willThrow(new ParkingNotFoundException(id));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request.with(httpBasic("user", "password")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve obter uma lista com todos os parking")
    public void getParkingsTest() throws Exception {

        List<ParkingDTO> list = new ArrayList<>();
        list.add(new ParkingDTO("5bba37a03b4547efabedd104de9fb280", "license", "state",
                "model", "Color", LocalDateTime.now().minusDays(1), LocalDateTime.now(), 23.0));

        BDDMockito.given(parkingService.findAll()).willReturn(list);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API)
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic("user", "password"));


        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(list.get(0).getId()))
                .andExpect(jsonPath("[0].license").value(list.get(0).getLicense()))
                .andExpect(jsonPath("[0].state").value(list.get(0).getState()))
                .andExpect(jsonPath("[0].model").value(list.get(0).getModel()))
                .andExpect(jsonPath("[0].color").value(list.get(0).getColor()))
                .andExpect(jsonPath("[0].bill").value(list.get(0).getBill()))
        ;
    }

    @Test
    @DisplayName("Deve atualizar um parking")
    public void updateParkingTest() throws Exception {
        String id = "1L";
        String json = new ObjectMapper().writeValueAsString(parkingCreateDTO());

        BDDMockito.given(parkingService.update(Mockito.anyString(), Mockito.any(ParkingCreateDTO.class)))
                .willReturn(parkingDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("user", "password"));

        mvc
                .perform(request)
                .andExpect(status().isNoContent())
                ;
    }

    @Test
    @DisplayName("Deve deletar um parking")
    public void deleteBookTest() throws Exception {
        String id = "5bba37a03b4547efabedd104de9fb280";
        ParkingDTO parkingDTO = this.parkingDTO();
        BDDMockito.given(parkingService.findById(Mockito.anyString())).willReturn(parkingDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic("user", "password"));

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    ParkingCreateDTO parkingCreateDTO() {
        return new ParkingCreateDTO("licese 1", " state 1", "model", "color");
    }

    ParkingDTO parkingDTO() {
        return new ParkingDTO("5bba37a03b4547efabedd104de9fb280", "license", "state",
                "model", "Color", LocalDateTime.now().minusDays(1), LocalDateTime.now(), 23.0);
    }
}
