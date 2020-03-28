package com.bol.mancala.service

import com.bol.mancala.dto.BoardDto
import com.bol.mancala.dto.PitDto
import com.bol.mancala.dto.Player
import com.bol.mancala.exception.InvalidPitException
import com.bol.mancala.exception.NoStonesException
import com.bol.mancala.exception.NumberOfPitsException
import com.bol.mancala.service.business.GameService
import com.bol.mancala.service.business.GameServiceImpl
import com.bol.mancala.service.persistence.GameInMemoryPersistenceServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID

@ExtendWith(SpringExtension::class)
class GameServiceTest {

    @SpyBean
    private lateinit var gamePersistenceService: GameInMemoryPersistenceServiceImpl

    private lateinit var gameService: GameService

    private val uuid = UUID.fromString("15593210-4162-4570-ad50-0db4d8788e70")

    private val boardSetup = BoardDto(
        pits = listOf(
            PitDto(1, 6),
            PitDto(2, 6),
            PitDto(3, 6),
            PitDto(4, 6),
            PitDto(5, 6),
            PitDto(6, 6),
            PitDto(7, 0),
            PitDto(8, 6),
            PitDto(9, 6),
            PitDto(10, 6),
            PitDto(11, 6),
            PitDto(12, 6),
            PitDto(13, 6),
            PitDto(14, 0)
        ),
        nextPlayer = Player.ONE,
        isGameOver = false
    )

    @BeforeEach
    fun prepare() {
        given(gamePersistenceService.find(uuid)).willReturn(boardSetup)
        given(gamePersistenceService.update(uuid, boardSetup)).willAnswer { null }

        gameService = GameServiceImpl(6, gamePersistenceService)
    }

    @Test
    fun `when a player wants to start a game an identifier will be returned`() {
        val uuid = gameService.start(Player.ONE)

        assertNotNull(uuid)
    }

    @Test
    fun `check for correct number of pits`() {
        val board = BoardDto(pits = listOf(PitDto(1, 5)), nextPlayer = Player.ONE)

        assertThrows(NumberOfPitsException::class.java) {
            gameService.sow(board, 1)
        }
    }

    @Test
    fun `when a player tries to sow the opponent's stones an exception will occur`() {
        assertThrows(InvalidPitException::class.java) {
            gameService.sow(boardSetup, 8)
        }
    }

    @Test
    fun `when a player sows stones from big pit an exception will occur`() {
        assertThrows(InvalidPitException::class.java) {
            gameService.sow(boardSetup, BIG_PIT_RIGHT_ID)
        }
    }

    @Test
    fun `when a player sows from an empty pit an exception will occur`() {
        val board = BoardDto(
            pits = listOf(
                PitDto(1, 0),
                PitDto(2, 7),
                PitDto(3, 7),
                PitDto(4, 7),
                PitDto(5, 7),
                PitDto(6, 7),
                PitDto(7, 1),
                PitDto(8, 6),
                PitDto(9, 6),
                PitDto(10, 6),
                PitDto(11, 6),
                PitDto(12, 6),
                PitDto(13, 6),
                PitDto(14, 0)
            ),
            nextPlayer = Player.ONE,
            isGameOver = false
        )

        assertThrows(NoStonesException::class.java) {
            gameService.sow(board, 1)
        }
    }

    @Test
    fun `when a player sows the persistence service's find and update methods should be called`() {
        val expectedBoard = BoardDto(
            pits = listOf(
                PitDto(1, 0),
                PitDto(2, 7),
                PitDto(3, 7),
                PitDto(4, 7),
                PitDto(5, 7),
                PitDto(6, 7),
                PitDto(7, 1),
                PitDto(8, 6),
                PitDto(9, 6),
                PitDto(10, 6),
                PitDto(11, 6),
                PitDto(12, 6),
                PitDto(13, 6),
                PitDto(14, 0)
            ),
            nextPlayer = Player.ONE,
            isGameOver = false
        )

        gameService.sow(uuid, 1)

        verify(gamePersistenceService).find(uuid)
        verify(gamePersistenceService).update(uuid, expectedBoard)
    }

    @Test
    fun `when player sows stones on initial board from pit 1 he can make another move`() {
        val actualBoard = gameService.sow(boardSetup, 1)

        val expectedBoard = BoardDto(
            pits = listOf(
                PitDto(1, 0),
                PitDto(2, 7),
                PitDto(3, 7),
                PitDto(4, 7),
                PitDto(5, 7),
                PitDto(6, 7),
                PitDto(7, 1),
                PitDto(8, 6),
                PitDto(9, 6),
                PitDto(10, 6),
                PitDto(11, 6),
                PitDto(12, 6),
                PitDto(13, 6),
                PitDto(14, 0)
            ),
            nextPlayer = Player.ONE,
            isGameOver = false
        )

        assertEquals(expectedBoard, actualBoard)
    }

    @Test
    fun `when a player sows stones the opponents big pit should be skipped`() {
        val boardSetup = BoardDto(
            pits = listOf(
                PitDto(1, 1),
                PitDto(2, 2),
                PitDto(3, 3),
                PitDto(4, 7),
                PitDto(5, 14),
                PitDto(6, 8),
                PitDto(7, 1),
                PitDto(8, 6),
                PitDto(9, 6),
                PitDto(10, 6),
                PitDto(11, 6),
                PitDto(12, 6),
                PitDto(13, 6),
                PitDto(14, 0)
            ),
            nextPlayer = Player.ONE
        )

        val actualBoard = gameService.sow(boardSetup, 6)

        val expectedBoard = BoardDto(
            pits = listOf(
                PitDto(1, 2),
                PitDto(2, 2),
                PitDto(3, 3),
                PitDto(4, 7),
                PitDto(5, 14),
                PitDto(6, 0),
                PitDto(7, 2),
                PitDto(8, 7),
                PitDto(9, 7),
                PitDto(10, 7),
                PitDto(11, 7),
                PitDto(12, 7),
                PitDto(13, 7),
                PitDto(14, 0)
            ),
            nextPlayer = Player.TWO,
            isGameOver = false
        )

        assertEquals(expectedBoard, actualBoard)
    }

    @Test
    fun `when player sows last stone not falling into a big pit it's the other player's turn`() {
        val boardSetup = BoardDto(
            pits = listOf(
                PitDto(1, 1),
                PitDto(2, 1),
                PitDto(3, 7),
                PitDto(4, 8),
                PitDto(5, 8),
                PitDto(6, 8),
                PitDto(7, 2),
                PitDto(8, 0),
                PitDto(9, 8),
                PitDto(10, 7),
                PitDto(11, 7),
                PitDto(12, 7),
                PitDto(13, 7),
                PitDto(14, 1)
            ),
            nextPlayer = Player.ONE
        )

        val actualBoard = gameService.sow(boardSetup, 1)

        val expectedBoard = BoardDto(
            pits = listOf(
                PitDto(1, 0),
                PitDto(2, 2),
                PitDto(3, 7),
                PitDto(4, 8),
                PitDto(5, 8),
                PitDto(6, 8),
                PitDto(7, 2),
                PitDto(8, 0),
                PitDto(9, 8),
                PitDto(10, 7),
                PitDto(11, 7),
                PitDto(12, 7),
                PitDto(13, 7),
                PitDto(14, 1)
            ),
            nextPlayer = Player.TWO,
            isGameOver = false
        )

        assertEquals(expectedBoard, actualBoard)
    }

    @Test
    fun `player can steal stones from the opposite pit if the next pit is empty and the current one only has one stone`() {
        val boardSetup = BoardDto(
            pits = listOf(
                PitDto(1, 1),
                PitDto(2, 0),
                PitDto(3, 8),
                PitDto(4, 8),
                PitDto(5, 8),
                PitDto(6, 8),
                PitDto(7, 2),
                PitDto(8, 0),
                PitDto(9, 8),
                PitDto(10, 7),
                PitDto(11, 7),
                PitDto(12, 7),
                PitDto(13, 7),
                PitDto(14, 1)
            ),
            nextPlayer = Player.ONE
        )

        val actualBoard = gameService.sow(boardSetup, 1)

        val expectedBoard = BoardDto(
            pits = listOf(
                PitDto(1, 0),
                PitDto(2, 0),
                PitDto(3, 8),
                PitDto(4, 8),
                PitDto(5, 8),
                PitDto(6, 8),
                PitDto(7, 10),
                PitDto(8, 0),
                PitDto(9, 8),
                PitDto(10, 7),
                PitDto(11, 7),
                PitDto(12, 0),
                PitDto(13, 7),
                PitDto(14, 1)
            ),
            nextPlayer = Player.TWO,
            isGameOver = false
        )

        assertEquals(expectedBoard, actualBoard)
    }

    @Test
    fun `if there are no more stones on either side the game is over`() {
        val boardSetup = BoardDto(
            pits = listOf(
                PitDto(1, 0),
                PitDto(2, 0),
                PitDto(3, 0),
                PitDto(4, 0),
                PitDto(5, 0),
                PitDto(6, 1),
                PitDto(7, 29),
                PitDto(8, 0),
                PitDto(9, 8),
                PitDto(10, 8),
                PitDto(11, 8),
                PitDto(12, 8),
                PitDto(13, 8),
                PitDto(14, 2)
            ),
            nextPlayer = Player.ONE
        )

        val expectedBoard = BoardDto(
            pits = listOf(
                PitDto(1, 0),
                PitDto(2, 0),
                PitDto(3, 0),
                PitDto(4, 0),
                PitDto(5, 0),
                PitDto(6, 0),
                PitDto(7, 30),
                PitDto(8, 0),
                PitDto(9, 8),
                PitDto(10, 8),
                PitDto(11, 8),
                PitDto(12, 8),
                PitDto(13, 8),
                PitDto(14, 2)
            ),
            nextPlayer = Player.ONE,
            isGameOver = true
        )

        val actualBoard = gameService.sow(boardSetup, 6)

        assertEquals(expectedBoard, actualBoard)
    }

}
