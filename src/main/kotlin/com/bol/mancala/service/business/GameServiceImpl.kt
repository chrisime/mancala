package com.bol.mancala.service.business

import com.bol.mancala.dto.BoardDto
import com.bol.mancala.dto.PitDto
import com.bol.mancala.dto.Player
import com.bol.mancala.exception.GameNotFoundException
import com.bol.mancala.exception.InvalidPitException
import com.bol.mancala.exception.NoStonesException
import com.bol.mancala.exception.NumberOfPitsException
import com.bol.mancala.service.BIG_PIT_LEFT_ID
import com.bol.mancala.service.BIG_PIT_RIGHT_ID
import com.bol.mancala.service.NO_OF_PITS
import com.bol.mancala.service.business.dto.Board
import com.bol.mancala.service.business.dto.Pit
import com.bol.mancala.service.persistence.GamePersistenceService
import mu.KotlinLogging
import java.util.UUID

open class GameServiceImpl(private val stones: Int,
                           private val gamePersistenceService: GamePersistenceService) : GameService {

    override fun start(player: Player): UUID {
        val first = (1 until BIG_PIT_RIGHT_ID).map { index ->
            PitDto(index, stones)
        }

        val second = (BIG_PIT_RIGHT_ID + 1 until BIG_PIT_LEFT_ID).map { index ->
            PitDto(index, stones)
        }

        val bigPitRight = PitDto(BIG_PIT_RIGHT_ID, 0)
        val bigPitLeft = PitDto(BIG_PIT_LEFT_ID, 0)

        val board = BoardDto(pits = first + bigPitRight + second + bigPitLeft, nextPlayer = player, isGameOver = false)

        return gamePersistenceService.create(board)
    }

    override fun sow(uuid: UUID, pitIndex: Int): BoardDto {
        val board = gamePersistenceService.find(uuid)

        board ?: throw GameNotFoundException("game with id/$uuid not found")

        val resultBoard = sow(board, pitIndex)

        gamePersistenceService.update(uuid, resultBoard)

        return resultBoard
    }

    override fun sow(boardDto: BoardDto, pitIndex: Int): BoardDto {
        val numOfPits = boardDto.pits.size

        if (numOfPits < 14) {
            log.error("board doesn't have the correct amount of pits")

            throw NumberOfPitsException("board doesn't have the correct amount of pits, only $numOfPits pits provided")
        }

        val pits = boardDto.pits.map { Pit(index = it.index, stones = it.stones) }
        val board = Board(pits = pits, player = boardDto.nextPlayer)

        // get current pit where to start
        val pit = board.getPit(pitIndex)

        // Corner case 1: check whether somebody or if pitIndex is one of the two big pits
        if (!board.isMyTurnOk(pitIndex) || pit.isRightBigPit() || pit.isLeftBigPit()) {
            log.error("pit index $pitIndex for player ${board.player} is not allowed")

            throw InvalidPitException("player ${board.player} is not allowed to take stones from pit index $pitIndex")
        }

        // Corner case 2: check if pit has no stones
        if (pit.hasNoStones()) {
            log.error("no stones inside pit $pitIndex")

            throw NoStonesException("no stones inside pit $pitIndex")
        }

        startSowing(board, pit, pitIndex)

        val isGameOver = board.hasEmptyPits(0, BIG_PIT_RIGHT_ID - 1)
                         || board.hasEmptyPits(BIG_PIT_RIGHT_ID, NO_OF_PITS - 1)

        val pitDtos = board.pits.map { PitDto(index = it.index, stones = it.stones) }

        return BoardDto(pits = pitDtos, nextPlayer = board.player, isGameOver = isGameOver)
    }

    fun startSowing(board: Board, pit: Pit, pitIndex: Int) {
        // get stones before emptying it for next move
        val stones = pit.stones
        pit.removeAllStones()

        var currentIndex = pitIndex

        val isPlayerOne = board.player == Player.ONE

        (0 until stones).forEach { _ ->
            // increase index to move to next pit
            currentIndex = currentIndex % NO_OF_PITS + 1

            // rule 1: player one mustn't sow player's two big pit and vice-versa -> increase index
            val currentPit = board.getPit(currentIndex)
            if (isPlayerOne && currentPit.isLeftBigPit() || !isPlayerOne && currentPit.isRightBigPit()) {
                currentIndex = currentIndex % NO_OF_PITS + 1
            }

            // get next pit to start sowing
            val nextPit = board.getPit(currentIndex)

            // rule 2: if the actual stone goes into one of the big pits sow and proceed with next stone
            if (nextPit.isLeftBigPit() || nextPit.isRightBigPit()) {
                nextPit.sow()
                return@forEach
            }

            // rule 3: current stone goes into an empty pit and the pit on the other side is filled with stones
            val pitIndexOnTheOtherSide = NO_OF_PITS - currentIndex
            val pitOnTheOtherSide = board.getPit(pitIndexOnTheOtherSide)
            // check if we can steal the stones from the pit on the other side
            if (nextPit.hasNoStones() && !pitOnTheOtherSide.hasNoStones()) {
                val otherStones = pitOnTheOtherSide.stones
                board.removeAllStonesFromPit(pitIndexOnTheOtherSide)

                val bigPitIndex = if (isPlayerOne) BIG_PIT_RIGHT_ID else BIG_PIT_LEFT_ID

                // add stones from other side including the current pit's one
                board.addStonesAtPit(bigPitIndex, otherStones + 1)

                return@forEach
            }

            nextPit.sow()
        }

        val actualPit = board.getPit(currentIndex)
        if (!actualPit.isLeftBigPit() && !actualPit.isRightBigPit()) {
            board.player = if (isPlayerOne) Player.TWO else Player.ONE
        }
    }

    private companion object {
        @JvmStatic private val log = KotlinLogging.logger { }
    }

}
