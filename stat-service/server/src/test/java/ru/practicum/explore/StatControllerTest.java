package ru.practicum.explore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explore.controller.StatController;
import ru.practicum.explore.service.StatService;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatController.class)
@AutoConfigureMockMvc
public class StatControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private StatService statService;

    @Autowired
    private MockMvc mockMvc;

    private HitDto hitDto;

    @BeforeEach
    void setup() {
        hitDto = HitDto.builder()
                .ip("192.168.0.1")
                .app("app")
                .uri("/hint")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void addHitTest() throws Exception {
        when(statService.saveHit(any())).thenReturn(hitDto);

        mockMvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(hitDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ip", is(hitDto.getIp())));

        verify(statService, times(1)).saveHit(any());
    }

    @Test
    void getStatWithTest() throws Exception {
        when(statService.saveHit(any())).thenReturn(hitDto);

        mockMvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(hitDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ip", is(hitDto.getIp())));

        verify(statService, times(1)).saveHit(any());
    }
}
