package com.bol.mancala.controller

import com.bol.mancala.BeansInitializer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ContextConfiguration(initializers = [BeansInitializer::class])
@WebMvcTest(controllers = [GameController::class])
class GameControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `create a new game`() {
        mockMvc.post("/mancala") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isOk
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
            }
            jsonPath("$.uuid") {
                exists()
                isString
            }
        }
    }

}
