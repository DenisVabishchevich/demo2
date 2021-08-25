package com.example.app.simple.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SimpleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private SimpleController simpleController;

    @Test
    void getSimpleDataRestTest() throws Exception {

        String key = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/v1/data")
            .header("Idempotency-Key", key))
            .andDo(print())
            .andExpect(jsonPath("$.data", CoreMatchers.notNullValue()))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/data")
            .header("Idempotency-Key", key))
            .andDo(print())
            .andExpect(jsonPath("$.data", CoreMatchers.notNullValue()))
            .andExpect(status().isOk());

        Mockito.verify(simpleController).getSomeData();

    }

    @Test
    void getSimpleDataRestDifferentKeysTest() throws Exception {


        mockMvc.perform(get("/api/v1/data")
            .header("Idempotency-Key", UUID.randomUUID().toString()))
            .andDo(print())
            .andExpect(jsonPath("$.data", CoreMatchers.notNullValue()))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/data")
            .header("Idempotency-Key", UUID.randomUUID().toString()))
            .andDo(print())
            .andExpect(jsonPath("$.data", CoreMatchers.notNullValue()))
            .andExpect(status().isOk());

        Mockito.verify(simpleController, Mockito.times(2)).getSomeData();

    }

    @Test
    void getSimpleErrorRestDifferentKeysTest() throws Exception {

        mockMvc.perform(get("/api/v1/errors")
            .header("Idempotency-Key", UUID.randomUUID().toString()))
            .andDo(print())
            .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/errors")
            .header("Idempotency-Key", UUID.randomUUID().toString()))
            .andDo(print())
            .andExpect(status().isBadRequest());

        Mockito.verify(simpleController, Mockito.times(2)).getSomeError();

    }

    @Test
    void getSimpleErrorRestTest() throws Exception {

        String key = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/v1/errors")
            .header("Idempotency-Key", key))
            .andDo(print())
            .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/errors")
            .header("Idempotency-Key", key))
            .andDo(print())
            .andExpect(status().isBadRequest());

        Mockito.verify(simpleController).getSomeError();

    }

}