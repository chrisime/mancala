package com.bol.mancala.controller

import com.bol.mancala.BeansInitializer
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(controllers = [GameController::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringJUnitWebConfig(initializers = [BeansInitializer::class])
class GameControllerTest(private val mockMvc: MockMvc) {

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
