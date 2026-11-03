package com.mathew.blackseaquest

import kotlinx.browser.window
import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement

class AudioManager {
    private var audioContext: dynamic = null
    private var diceBuffer: dynamic = null
    private var seaAudio: HTMLAudioElement? = null
    private var isInitialized = false
    private var isDiceLoaded = false

    fun initialize() {
        if (isInitialized) return
        
        val contextClass = window.asDynamic().AudioContext ?: window.asDynamic().webkitAudioContext
        if (contextClass != null) {
            audioContext = js("new contextClass({ latencyHint: 'interactive' })")
        }
        
        loadDiceBuffer()
        
        seaAudio = (document.createElement("audio") as HTMLAudioElement).apply {
            src = "resources/sea_noise.mp3"
            preload = "auto"
            volume = 0.3
            loop = true
            load()
        }
        document.body?.appendChild(seaAudio!!)
        
        isInitialized = true
    }

    private fun loadDiceBuffer() {
        if (isDiceLoaded || audioContext == null) return
        window.asDynamic().fetch("resources/dice_roll.wav")
            .then { response: dynamic -> response.arrayBuffer() }
            .then { arrayBuffer: dynamic ->
                audioContext.decodeAudioData(arrayBuffer) { buffer: dynamic ->
                    diceBuffer = buffer
                    isDiceLoaded = true
                }
            }
    }

    fun playDiceSound() {
        if (!isInitialized) initialize()
        
        if (!isDiceLoaded || diceBuffer == null || audioContext == null) {
            val fallback = document.createElement("audio") as HTMLAudioElement
            fallback.src = "resources/dice_roll.wav"
            fallback.volume = 0.7
            fallback.play()
            return
        }

        val source = audioContext.createBufferSource()
        source.buffer = diceBuffer
        source.connect(audioContext.destination)
        source.start(0)
    }

    fun startSeaNoise() {
        if (!isInitialized) initialize()
        seaAudio?.play()
    }
}
