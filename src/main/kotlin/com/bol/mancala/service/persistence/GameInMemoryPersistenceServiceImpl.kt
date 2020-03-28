package com.bol.mancala.service.persistence

import com.bol.mancala.dto.BoardDto
import java.util.UUID

open class GameInMemoryPersistenceServiceImpl : GamePersistenceService {

    private val map: MutableMap<UUID, BoardDto> = mutableMapOf()

    override fun create(board: BoardDto): UUID = UUID.randomUUID().apply {
        map[this] = board
    }

    override fun update(uuid: UUID, board: BoardDto) {
        map[uuid] = board
    }

    override fun find(uuid: UUID): BoardDto? = map[uuid]
}
