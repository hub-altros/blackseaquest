package com.mathew.blackseaquest

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

actual class SoundManager actual constructor() {
    private var soundPool: SoundPool? = null
    private var diceSoundId: Int = 0

    fun initialize(context: Context) {
        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
        diceSoundId = soundPool?.load(context, com.mathew.blackseaquest.R.raw.dice_roll, 1) ?: 0
    }

    actual fun playDiceSound() {
        soundPool?.play(diceSoundId, 1f, 1f, 1, 0, 1f)
    }
}
