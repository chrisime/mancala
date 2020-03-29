package com.bol.mancala.service.business.dto

import com.bol.mancala.service.BIG_PIT_RIGHT_ID
import com.bol.mancala.service.NO_OF_PITS
import com.bol.mancala.dto.Player

data class Board(val pits: List<Pit>, var player: Player) {

    fun isMyTurnOk(pitIndex: Int): Boolean {
        return this.player == Player.ONE && pitIndex < BIG_PIT_RIGHT_ID
               || this.player == Player.TWO && pitIndex > BIG_PIT_RIGHT_ID
    }

    fun hasEmptyPits(start: Int, end: Int): Boolean {
        if (start < 0 || start > end || end >= NO_OF_PITS)
            throw IllegalArgumentException("invalid input")

        return pits.subList(start, end).fold(true) { last, pit ->
            last && pit.hasNoStones()
        }
    }

    fun getPit(index: Int): Pit {
        if (index > NO_OF_PITS)
            throw IllegalArgumentException("invalid input: $index > $NO_OF_PITS")

        return pits[index - 1]
    }

    fun removeAllStonesFromPit(index: Int) {
        getPit(index).removeAllStones()
    }

    fun addStonesAtPit(index: Int, stones: Int) {
        getPit(index).addStones(stones)
    }

}
