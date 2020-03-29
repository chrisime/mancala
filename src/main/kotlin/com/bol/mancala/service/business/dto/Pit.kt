package com.bol.mancala.service.business.dto

import com.bol.mancala.service.BIG_PIT_LEFT_ID
import com.bol.mancala.service.BIG_PIT_RIGHT_ID

data class Pit(private var _index: Int, private var _stones: Int) {

    var stones: Int = _stones
        private set

    var index: Int = _index
        private set

    fun sow() {
        this.stones++
    }

    fun addStones(stones: Int) {
        this.stones += stones
    }

    fun removeAllStones() {
        this.stones = 0
    }

    fun hasNoStones(): Boolean = this.stones == 0

    fun isRightBigPit() = index == BIG_PIT_RIGHT_ID

    fun isLeftBigPit() = index == BIG_PIT_LEFT_ID

}
