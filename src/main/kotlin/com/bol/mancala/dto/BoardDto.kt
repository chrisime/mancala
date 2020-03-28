package com.bol.mancala.dto

import java.util.UUID

data class BoardDto(val pits: List<PitDto>,
                    val nextPlayer: Player,
                    val isGameOver: Boolean? = null)
