package com.eventos.api.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.eventos.api.domain.event.Event;
import com.eventos.api.dto.event.EventRequestDTO;
import com.eventos.api.service.EventService;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
public class EventControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private EventService eventService;

    private Event existingEvent;

    private final List<Event> eventsToCleanup = new ArrayList<>();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        EventRequestDTO dto = new EventRequestDTO(
                "Evento Teste " + System.currentTimeMillis(),
                "Descrição teste",
                LocalDateTime.now().plusDays(1),
                "São Paulo", "SP",
                true, "http://teste.com", null);

        existingEvent = eventService.createEvent(dto);
        eventsToCleanup.add(existingEvent);
    }

    @AfterEach
    public void cleanup() {
        for (Event e : eventsToCleanup) {
            try {
                eventService.deleteEvent(e.getId());
            } catch (Exception ignored) {
            }
        }
        eventsToCleanup.clear();
    }

    @Test
    public void testGetAllEvents() throws Exception {
        mockMvc.perform(get("/api/event/all")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetEventDetails() throws Exception {
        mockMvc.perform(get("/api/event/{id}", existingEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingEvent.getId().toString()))
                .andExpect(jsonPath("$.title").value(existingEvent.getTitle()));
    }

    @Test
    public void testCreateEvent() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "image-content".getBytes());

        mockMvc.perform(multipart("/api/event")
                .file(image)
                .param("title", "Evento Criado " + System.currentTimeMillis())
                .param("description", "Descrição criada")
                .param("date", LocalDateTime.now().plusDays(2).toString())
                .param("city", "Rio de Janeiro")
                .param("uf", "RJ")
                .param("remote", "true")
                .param("eventUrl", "http://evento.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andDo(result -> {
                    String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
                    Event e = new Event();
                    e.setId(UUID.fromString(id));
                    eventsToCleanup.add(e);
                });
    }

    @Test
    public void testDeleteEvent() throws Exception {
        mockMvc.perform(delete("/api/event/{id}", existingEvent.getId()))
                .andExpect(status().isNoContent());
        eventsToCleanup.remove(existingEvent);
    }

    @Test
    public void testGetEventCoupons() throws Exception {
        mockMvc.perform(get("/api/event/{id}/coupons", existingEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
