package com.bol.mancala.service.business

import com.bol.mancala.dto.BoardDto
import com.bol.mancala.dto.Player
import java.util.UUID

interface GameService {

    fun start(player: Player = Player.ONE): UUID

    fun sow(boardDto: BoardDto, pitIndex: Int): BoardDto

    fun sow(uuid: UUID, pitIndex: Int): BoardDto

}
