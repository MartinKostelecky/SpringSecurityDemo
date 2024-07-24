package cz.martinkostelecky.springsecurity.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsStringIgnoringCase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//an available port will be picked at random each time your test runs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//enables and configures auto-configuration of MockMvc
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc api;

    @Test
    void anyoneCanReachThisPublicEndpoint() throws Exception {
        api.perform(get("/api/v1/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsStringIgnoringCase("This")));
    }

    @Test
    void notLoggedIn_shouldNotReachSecuredEndpoint() throws Exception {
        api.perform(get("/api/v1/secured"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void loggedIn_shouldReachSecuredEndpoint() throws Exception {
        api.perform(get("/api/v1/secured"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsStringIgnoringCase("your ID: 1")));;
    }
}