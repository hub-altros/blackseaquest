package com.mathew.blackseaquest.game

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathew.blackseaquest.data.GameContent
import com.mathew.blackseaquest.model.AnswerOption
import com.mathew.blackseaquest.model.Station
import com.mathew.blackseaquest.model.StationType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class Player(
    val name: String,
    val color: Color,
    val position: Int = 0,
    val skipTurns: Int = 0,
    val score: Int = 0
)

data class GameUiState(
    val players: List<Player>,
    val currentPlayerIndex: Int = 0,
    val dice: Int? = null,
    val isRolling: Boolean = false,
    val card: Station? = null,
    val message: String = "",
    val winner: String? = null
)

class GameViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        GameUiState(players = listOf(
            Player("Игрок 1", Color(0xFFE53935)),
            Player("Игрок 2", Color(0xFF1E88E5))
        ))
    )
    val state = _state.asStateFlow()

    fun roll() {
        val s = _state.value
        if (s.isRolling || s.card != null || s.winner != null) return

        val player = s.players[s.currentPlayerIndex]
        if (player.skipTurns > 0) {
            _state.update { st ->
                val ps = st.players.toMutableList()
                ps[st.currentPlayerIndex] = player.copy(skipTurns = player.skipTurns - 1)
                st.copy(players = ps, message = "${player.name} пропускает ход")
            }
            nextPlayer()
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isRolling = true, message = "") }
            repeat(10) { i ->
                _state.update { it.copy(dice = Random.nextInt(1, 5)) }
                delay(60L + i * 15L)
            }
            val dice = Random.nextInt(1, 5)
            _state.update { st ->
                val ps = st.players.toMutableList()
                val current = ps[st.currentPlayerIndex]
                ps[st.currentPlayerIndex] = current.copy(score = current.score + dice)
                st.copy(dice = dice, isRolling = false, players = ps)
            }
            move(dice)
        }
    }

    private fun move(dice: Int) {
        val s = _state.value
        val p = s.players[s.currentPlayerIndex]
        val target = (p.position + dice).coerceAtMost(GameContent.LAST_INDEX)
        val ps = s.players.toMutableList()
        ps[s.currentPlayerIndex] = p.copy(position = target)
        _state.update {
            it.copy(players = ps, card = GameContent.stations[target].let { st ->
                st.copy(options = st.options.shuffled())
            })
        }
    }

    fun answer(option: AnswerOption?) {
        val s = _state.value
        val card = s.card ?: return
        val p = s.players[s.currentPlayerIndex]
        val ps = s.players.toMutableList()

        when {
            card.type == StationType.PENALTY -> {
                ps[s.currentPlayerIndex] = p.copy(skipTurns = 1)
                _state.update { it.copy(players = ps, card = null, message = card.penaltyText) }
                nextPlayer()
            }
            card.type == StationType.BONUS -> {
                _state.update { it.copy(card = null, message = card.penaltyText) }
            }
            option == null -> {
                if (p.position == GameContent.LAST_INDEX) {
                    ps[s.currentPlayerIndex] = p.copy(position = GameContent.LAST_INDEX - 1, skipTurns = 1)
                } else {
                    ps[s.currentPlayerIndex] = p.copy(skipTurns = 1)
                }
                _state.update { it.copy(players = ps, card = null, message = "Без ответа — пропуск хода") }
                nextPlayer()
            }
            option.isCorrect -> {
                _state.update { it.copy(card = null, message = "Верно! ${card.explanation}") }
                if (p.position == GameContent.LAST_INDEX) {
                    _state.update { it.copy(winner = p.name) }
                } else nextPlayer()
            }
            else -> {
                if (p.position == GameContent.LAST_INDEX) {
                    ps[s.currentPlayerIndex] = p.copy(position = GameContent.LAST_INDEX - 1, skipTurns = 1)
                } else {
                    ps[s.currentPlayerIndex] = p.copy(skipTurns = 1)
                }
                _state.update {
                    it.copy(players = ps, card = null,
                        message = "Неверно. ${card.explanation} Пропуск хода.")
                }
                nextPlayer()
            }
        }
    }

    fun closeCard() {
        val s = _state.value
        if (s.card != null) {
            answer(null)
        }
    }

    private fun nextPlayer() = _state.update {
        it.copy(currentPlayerIndex = (it.currentPlayerIndex + 1) % it.players.size)
    }

    fun restartGame() {
        _state.value = GameUiState(players = listOf(
            Player("Игрок 1", Color(0xFFE53935)),
            Player("Игрок 2", Color(0xFF1E88E5))
        ))
    }
}
