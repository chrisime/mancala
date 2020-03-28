package com.bol.mancala.service.persistence

import com.bol.mancala.dto.BoardDto
import java.util.UUID

interface GamePersistenceService {

    fun create(board: BoardDto): UUID

    fun update(uuid: UUID, board: BoardDto)

    fun find(uuid: UUID): BoardDto?

}
