package com.mathew.blackseaquest

import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import com.mathew.blackseaquest.game.GameViewModel
import com.mathew.blackseaquest.game.GameUiState
import com.mathew.blackseaquest.model.StationType

fun main() {
    val root = (document.getElementById("ComposeTarget") as? HTMLDivElement)
        ?: (document.createElement("div") as HTMLDivElement).also {
            it.id = "ComposeTarget"
            document.body?.appendChild(it)
        }

    root.style.apply {
        width = "100vw"
        height = "100vh"
        margin = "0"
        padding = "0"
        display = "flex"
        flexDirection = "column"
        overflowX = "hidden"
        overflowY = "hidden"
        fontFamily = "sans-serif"
    }

    val viewModel = GameViewModel()
    val audioManager = AudioManager()

    fun render(state: GameUiState) {
        root.innerHTML = ""

        // --- 1. ВЕРХНЯЯ ПАНЕЛЬ ---
        val topPanel = document.createElement("div") as HTMLDivElement
        topPanel.style.apply {
            display = "flex"
            justifyContent = "space-between"
            padding = "10px 20px"
            backgroundColor = "rgba(255,255,255,0.8)"
            zIndex = "10"
        }
        
        state.players.forEachIndexed { i, player ->
            val playerContainer = document.createElement("div") as HTMLDivElement
            playerContainer.style.apply {
                display = "flex"
                alignItems = "center"
                backgroundColor = if (i == state.currentPlayerIndex) "#0D47A1" else "#B0BEC5"
                color = "white"
                padding = "8px 16px"
                borderRadius = "20px"
                fontWeight = "bold"
            }
            
            val nameSpan = document.createElement("span") as HTMLElement
            nameSpan.innerText = player.name
            nameSpan.style.marginRight = "10px"
            playerContainer.appendChild(nameSpan)

            val scoreSpan = document.createElement("span") as HTMLElement
            scoreSpan.innerText = "★ ${player.score}"
            scoreSpan.style.apply {
                backgroundColor = "#FFC107"
                color = "#000"
                padding = "2px 8px"
                borderRadius = "10px"
                fontSize = "14px"
            }
            playerContainer.appendChild(scoreSpan)

            topPanel.appendChild(playerContainer)
        }
        root.appendChild(topPanel)

        // --- 2. ИГРОВОЕ ПОЛЕ ---
        val gameBoard = document.createElement("div") as HTMLDivElement
        gameBoard.style.apply {
            position = "relative"
            flex = "1"
            width = "100%"
            overflowX = "hidden"
            overflowY = "hidden"
        }

        val background = document.createElement("img") as HTMLImageElement
        background.src = "composeResources/blackseaquest.shared.generated.resources/drawable/map_black_sea.png"
        background.style.apply {
            position = "absolute"
            top = "0"
            left = "0"
            width = "100%"
            height = "100%"
            objectFit = "cover"
        }
        gameBoard.appendChild(background)

        val positions = listOf(
            15 to 15, 25 to 10, 35 to 15, 40 to 25, 45 to 35, 40 to 45, 35 to 55, 25 to 65,
            20 to 75, 25 to 85, 40 to 90, 55 to 85, 65 to 75, 75 to 65, 80 to 50, 85 to 40,
            85 to 25, 75 to 15, 60 to 10, 50 to 15
        )
        positions.forEachIndexed { i, (x, y) ->
            val stationDiv = document.createElement("div") as HTMLDivElement
            stationDiv.innerText = "${i + 1}"
            stationDiv.style.apply {
                position = "absolute"
                left = "${x}%"
                top = "${y}%"
                width = "30px"
                height = "30px"
                borderRadius = "50%"
                backgroundColor = "#0D47A1"
                color = "white"
                display = "flex"
                alignItems = "center"
                justifyContent = "center"
                fontWeight = "bold"
                fontSize = "12px"
                border = "3px solid white"
                transform = "translate(-50%, -50%)"
            }
            gameBoard.appendChild(stationDiv)

            state.players.forEachIndexed { pi, p ->
                if (p.position == i) {
                    val piece = document.createElement("div") as HTMLDivElement
                    piece.style.apply {
                        position = "absolute"
                        val offsetX = if (pi == 0) -12 else 12
                        left = "calc(${x}% + ${offsetX}px)"
                        top = "${y}%"
                        width = "16px"
                        height = "16px"
                        borderRadius = "50%"
                        backgroundColor = if (pi == 0) "#E53935" else "#1E88E5"
                        border = "2px solid white"
                        transform = "translate(-50%, 20px)"
                    }
                    gameBoard.appendChild(piece)
                }
            }
        }
        root.appendChild(gameBoard)

        // --- 3. НИЖНЯЯ ПАНЕЛЬ ---
        val bottomPanel = document.createElement("div") as HTMLDivElement
        bottomPanel.style.apply {
            padding = "15px 20px"
            backgroundColor = "#F3E9D2"
            display = "flex"
            flexDirection = "column"
            alignItems = "center"
            zIndex = "10"
        }

        if (state.message.isNotBlank()) {
            val msg = document.createElement("p") as HTMLElement
            msg.innerText = state.message
            msg.style.apply { 
                margin = "0 0 10px 0"
                textAlign = "center"
                fontSize = "14px"
                maxHeight = "40px"
                overflowY = "auto"
            }
            bottomPanel.appendChild(msg)
        }

        val diceAndButtonRow = document.createElement("div") as HTMLDivElement
        diceAndButtonRow.style.apply {
            display = "flex"
            alignItems = "center"
            justifyContent = "center"
            width = "100%"
        }

        val diceText = document.createElement("div") as HTMLElement
        diceText.innerText = state.dice?.toString() ?: "🎲"
        diceText.style.apply { fontSize = "40px"; fontWeight = "bold"; marginRight = "20px" }
        diceAndButtonRow.appendChild(diceText)

        val rollButton = document.createElement("button") as HTMLButtonElement
        rollButton.innerText = if (state.isRolling) "Бросаем…" else "Бросить кубик"
        rollButton.disabled = state.isRolling || state.card != null
        rollButton.style.apply {
            padding = "12px 30px"
            fontSize = "18px"
            backgroundColor = if (rollButton.disabled) "#B0BEC5" else "#0D47A1"
            color = "white"
            border = "none"
            borderRadius = "10px"
            cursor = "pointer"
            flex = "1"
            maxWidth = "250px"
        }
        rollButton.onclick = {
            audioManager.playDiceSound()
            audioManager.startSeaNoise()
            viewModel.roll()
        }
        diceAndButtonRow.appendChild(rollButton)
        bottomPanel.appendChild(diceAndButtonRow)
        root.appendChild(bottomPanel)

        // --- 4. МОДАЛЬНОЕ ОКНО ВОПРОСА ---
        state.card?.let { card ->
            val dialogOverlay = document.createElement("div") as HTMLDivElement
            dialogOverlay.style.apply {
                position = "fixed"
                top = "0"
                left = "0"
                width = "100%"
                height = "100%"
                backgroundColor = "rgba(0,0,0,0.5)"
                display = "flex"
                alignItems = "center"
                justifyContent = "center"
                zIndex = "20"
            }

            val dialog = document.createElement("div") as HTMLDivElement
            dialog.style.apply {
                backgroundColor = "white"
                padding = "20px"
                borderRadius = "15px"
                width = "90%"
                maxWidth = "400px"
                maxHeight = "80%"
                overflowY = "auto"
            }
            
            dialog.innerHTML = "<h2 style='margin-top:0; font-size:20px;'>${card.title}</h2><p style='font-size:16px;'>${card.question}</p>"

            var isAnswering = false

            if (card.type == StationType.PENALTY || card.type == StationType.BONUS) {
                val closeBtn = document.createElement("button") as HTMLButtonElement
                closeBtn.innerText = "Понятно"
                closeBtn.style.apply { width = "100%"; padding = "10px"; marginTop = "10px"; backgroundColor = "#0D47A1"; color = "white"; border = "none"; borderRadius = "5px"; fontSize="16px" }
                closeBtn.onclick = { viewModel.answer(null); viewModel.closeCard() }
                dialog.appendChild(closeBtn)
            } else {
                card.options.forEach { opt ->
                    val btn = document.createElement("button") as HTMLButtonElement
                    btn.innerText = opt.text
                    btn.style.apply {
                        display = "block"
                        width = "100%"
                        margin = "8px 0"
                        padding = "12px"
                        backgroundColor = "#F3E9D2"
                        border = "1px solid #ccc"
                        borderRadius = "8px"
                        fontSize = "15px"
                        cursor = "pointer"
                        textAlign = "center"
                    }
                    btn.onclick = {
                        if (!isAnswering) {
                            isAnswering = true
                            
                            if (opt.isCorrect) {
                                btn.style.backgroundColor = "#4CAF50"
                                btn.style.color = "white"
                            } else {
                                btn.style.backgroundColor = "#F44336"
                                btn.style.color = "white"
                            }

                            val buttons = dialog.querySelectorAll("button")
                            for (i in 0 until buttons.length) {
                                val b = buttons.item(i) as HTMLButtonElement
                                b.disabled = true
                                b.style.opacity = "0.7"
                            }

                            kotlinx.browser.window.setTimeout({
                                viewModel.answer(opt)
                            }, 1500)
                        }
                    }
                    dialog.appendChild(btn)
                }
                val skipBtn = document.createElement("button") as HTMLButtonElement
                skipBtn.innerText = "Не знаю (пропустить ход)"
                skipBtn.style.apply { width = "100%"; padding = "10px"; marginTop = "10px"; backgroundColor = "#E53935"; color = "white"; border = "none"; borderRadius = "5px"; fontSize="14px" }
                skipBtn.onclick = {
                    if (!isAnswering) {
                        isAnswering = true
                        viewModel.answer(null)
                    }
                }
                dialog.appendChild(skipBtn)
            }
            
            dialogOverlay.appendChild(dialog)
            root.appendChild(dialogOverlay)
        }

        // --- 5. ОКНО ПОБЕДЫ ---
        state.winner?.let { winner ->
            val winOverlay = document.createElement("div") as HTMLDivElement
            winOverlay.style.apply {
                position = "fixed"
                top = "0"
                left = "0"
                width = "100%"
                height = "100%"
                backgroundColor = "rgba(0,0,0,0.8)"
                display = "flex"
                alignItems = "center"
                justifyContent = "center"
                zIndex = "30"
            }

            val winDialog = document.createElement("div") as HTMLDivElement
            winDialog.style.apply {
                backgroundColor = "white"
                padding = "30px"
                borderRadius = "20px"
                textAlign = "center"
                maxWidth = "80%"
            }

            val title = document.createElement("h1") as HTMLElement
            title.innerText = "🎉 Победа! 🎉"
            title.style.color = "#0D47A1"
            winDialog.appendChild(title)

            val text = document.createElement("p") as HTMLElement
            text.innerText = "$winner дошел до финиша! Спасибо за игру."
            text.style.apply { fontSize = "18px"; margin = "20px 0" }
            winDialog.appendChild(text)

            val restartBtn = document.createElement("button") as HTMLButtonElement
            restartBtn.innerText = "Играть снова!"
            restartBtn.style.apply {
                padding = "15px 30px"
                fontSize = "18px"
                backgroundColor = "#4CAF50"
                color = "white"
                border = "none"
                borderRadius = "10px"
                cursor = "pointer"
                width = "100%"
            }
            restartBtn.onclick = {
                viewModel.restartGame()
            }
            winDialog.appendChild(restartBtn)

            winOverlay.appendChild(winDialog)
            root.appendChild(winOverlay)
        }
    }

    val scope = MainScope()
    scope.launch {
        viewModel.state.collect { state ->
            render(state)
        }
    }
}
