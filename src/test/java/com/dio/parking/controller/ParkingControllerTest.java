package com.dio.parking.controller;

import com.dio.parking.CloundParkingApplication;
import com.dio.parking.config.SecurityConfiguration;
import com.dio.parking.dto.ParkingCreateDTO;
import com.dio.parking.dto.ParkingDTO;
import com.dio.parking.service.ParkingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
//@AutoConfigureMockMvc
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
    public void createBookTest() throws Exception {

        ParkingCreateDTO parkingCreateDTO = parkingCreateDTO();
        ParkingDTO parkingDTO = new ParkingDTO("5bba37a03b4547efabedd104de9fb280","license", "state",
                "model", "Color", LocalDateTime.now().minusDays(1), LocalDateTime.now(),23.0);

        BDDMockito.given(parkingService.create(Mockito.any(ParkingCreateDTO.class))).willReturn(parkingDTO);

        String json = new ObjectMapper().writeValueAsString(parkingCreateDTO);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request.with(httpBasic("user","password")))
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

    ParkingCreateDTO parkingCreateDTO() {
        return new ParkingCreateDTO("licese 1", " state 1", "model", "color");
    }
}
