package com.bol.mancala.controller

import com.bol.mancala.Beans
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ContextConfiguration(classes = [Beans::class])
@TestPropertySource("classpath:/application_test.yml")
@WebMvcTest(controllers = [GameController::class])
class GameControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `create a new game`() {
        mockMvc.perform(post("/mancala").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.uuid").exists())
            .andExpect(jsonPath("$.uuid").isString)
    }

}
