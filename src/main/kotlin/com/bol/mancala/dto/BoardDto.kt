package com.bol.mancala.dto

data class BoardDto(val pits: List<PitDto>,
                    val nextPlayer: Player,
                    val isGameOver: Boolean? = null,
                    val scorePlayer1: Int? = null,
                    val scorePlayer2: Int? = null)
