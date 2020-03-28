package com.bol.mancala.controller

import com.bol.mancala.dto.BoardDto
import com.bol.mancala.service.business.GameService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/mancala")
class GameController(private val gameService: GameService) {

    @PostMapping
    fun startGame(/*@RequestParam player: Player*/): ResponseEntity<String> {
        val uuid = gameService.start()

        return ResponseEntity.ok(uuid.toString())
    }

    @PostMapping("/sow/{pitIndex}")
    fun sow(@PathVariable("pitIndex") pitIndex: Int,
            @RequestBody board: BoardDto): ResponseEntity<BoardDto> {
        val boardDto = gameService.sow(board, pitIndex)

        return ResponseEntity.ok(boardDto)
    }

    @PostMapping("/sow/uuid/{pitIndex}")
    fun sow(@PathVariable("pitIndex") pitIndex: Int, @RequestBody uuid: String): ResponseEntity<BoardDto> {
        val uuidFromString = UUID.fromString(uuid)
        val boardDto = gameService.sow(uuidFromString, pitIndex)

        return ResponseEntity.ok(boardDto)
    }

}
