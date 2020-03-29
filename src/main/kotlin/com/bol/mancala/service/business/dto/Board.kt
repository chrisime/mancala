package com.bol.mancala.service.business.dto

import com.bol.mancala.dto.Player
import com.bol.mancala.service.BIG_PIT_LEFT_ID
import com.bol.mancala.service.BIG_PIT_RIGHT_ID
import com.bol.mancala.service.NO_OF_PITS

data class Board(val pits: List<Pit>, var player: Player) {

    fun isMyTurnOk(pitIndex: Int): Boolean {
        return this.player == Player.ONE && pitIndex < BIG_PIT_RIGHT_ID
               || this.player == Player.TWO && pitIndex > BIG_PIT_RIGHT_ID
    }

    fun getScores(): Pair<Int, Int> {
        val player1 = pits[BIG_PIT_RIGHT_ID - 1].stones
        val player2 = pits[BIG_PIT_LEFT_ID - 1].stones

        return Pair(player1, player2)
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

    fun getOppositePit(index: Int): Pit {
        val pitIndexOnTheOtherSide = NO_OF_PITS - index


        return getPit(pitIndexOnTheOtherSide)
    }

    fun removeAllStonesFromOppositePit(index: Int) {
        val pitIndexOnTheOtherSide = NO_OF_PITS - index

        getPit(pitIndexOnTheOtherSide).removeAllStones()
    }

    fun addStonesAtPit(index: Int, stones: Int) {
        getPit(index).addStones(stones)
    }

}
