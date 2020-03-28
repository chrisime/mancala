package com.bol.mancala.service.business.dto

import com.bol.mancala.service.BIG_PIT_LEFT_ID
import com.bol.mancala.service.BIG_PIT_RIGHT_ID

data class Pit(var index: Int, var stones: Int) {

    fun sow() {
        this.stones++;
    }

    fun addStones(stones: Int) {
        this.stones += stones
    }

    fun removeAllStones() {
        this.stones = 0
    }

    fun hasNoStones(): Boolean = this.stones == 0

    fun isRightBigPit() = index == BIG_PIT_RIGHT_ID

    fun isLeftBigPit() =  index == BIG_PIT_LEFT_ID

}
