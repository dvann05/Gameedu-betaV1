package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.data.model.AppLanguage
import java.util.Locale
import kotlin.concurrent.thread
import kotlin.math.sin

enum class SfxType {
    CLICK,
    CORRECT,
    WRONG,
    LEVEL_UP,
    COIN_COLLECT,
    BUBBLE_POP,
    FANFARE,
    CARD_FLIP,
    STARS_EARNED,
    XYLOPHONE_NOTE
}

class AudioEngine(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false

    var isMusicEnabled = true
    var isSfxEnabled = true
    var isNarrationEnabled = true

    var musicVolume = 0.8f
    var sfxVolume = 1.0f
    var narrationVolume = 1.0f

    private var isMusicPlaying = false
    private var musicThread: Thread? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("AudioEngine", "TTS init error: ${e.message}")
        }
        startBackgroundMusic()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            setLanguage("id")
        }
    }

    fun setLanguage(languageCode: String) {
        if (!isTtsInitialized || tts == null) return
        val locale = when (languageCode.lowercase()) {
            "id" -> Locale("id", "ID")
            "en" -> Locale.US
            "ar" -> Locale("ar")
            "ja" -> Locale.JAPAN
            "ko" -> Locale.KOREA
            "zh" -> Locale.CHINA
            "fr" -> Locale.FRANCE
            "de" -> Locale.GERMANY
            "es" -> Locale("es", "ES")
            "pt" -> Locale("pt", "BR")
            "ru" -> Locale("ru", "RU")
            "hi" -> Locale("hi", "IN")
            else -> Locale.US
        }
        try {
            tts?.language = locale
        } catch (e: Exception) {
            Log.e("AudioEngine", "Error setting locale $locale: ${e.message}")
        }
    }

    fun speak(text: String, languageCode: String = "id") {
        if (!isNarrationEnabled || !isTtsInitialized || text.isBlank()) return
        setLanguage(languageCode)
        try {
            tts?.setSpeechRate(0.95f)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "UtteranceId_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            Log.e("AudioEngine", "TTS Speak error: ${e.message}")
        }
    }

    fun playSfx(type: SfxType, pitchFreq: Float = 0f) {
        if (!isSfxEnabled) return
        thread {
            try {
                generateAndPlayTone(type, pitchFreq)
            } catch (e: Exception) {
                Log.e("AudioEngine", "SFX Play error: ${e.message}")
            }
        }
    }

    private fun generateAndPlayTone(type: SfxType, customFreq: Float = 0f) {
        val sampleRate = 22050
        val durationMs = when (type) {
            SfxType.CLICK -> 60
            SfxType.CARD_FLIP -> 70
            SfxType.BUBBLE_POP -> 100
            SfxType.COIN_COLLECT -> 180
            SfxType.CORRECT -> 350
            SfxType.WRONG -> 250
            SfxType.LEVEL_UP -> 600
            SfxType.FANFARE -> 800
            SfxType.STARS_EARNED -> 500
            SfxType.XYLOPHONE_NOTE -> 300
        }

        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val sample = DoubleArray(numSamples)
        val buffer = ShortArray(numSamples)

        when (type) {
            SfxType.CLICK -> {
                val freq = 800.0
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val env = 1.0 - (i.toDouble() / numSamples)
                    sample[i] = sin(2 * Math.PI * freq * t) * env
                }
            }
            SfxType.CARD_FLIP -> {
                val freq = 600.0
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val env = sin(Math.PI * i / numSamples)
                    sample[i] = sin(2 * Math.PI * freq * t) * env
                }
            }
            SfxType.BUBBLE_POP -> {
                for (i in 0 until numSamples) {
                    val progress = i.toDouble() / numSamples
                    val freq = 400.0 + progress * 800.0
                    val t = i.toDouble() / sampleRate
                    val env = 1.0 - progress
                    sample[i] = sin(2 * Math.PI * freq * t) * env
                }
            }
            SfxType.COIN_COLLECT -> {
                val freq1 = 987.77 // B5
                val freq2 = 1318.51 // E6
                val half = numSamples / 2
                for (i in 0 until numSamples) {
                    val freq = if (i < half) freq1 else freq2
                    val t = i.toDouble() / sampleRate
                    val env = 1.0 - (i.toDouble() / numSamples)
                    sample[i] = sin(2 * Math.PI * freq * t) * env
                }
            }
            SfxType.CORRECT -> {
                // Happy major arpeggio C5 - E5 - G5 - C6
                val freqs = doubleArrayOf(523.25, 659.25, 783.99, 1046.50)
                val chunk = numSamples / freqs.size
                for (i in 0 until numSamples) {
                    val idx = (i / chunk).coerceIn(0, freqs.size - 1)
                    val t = i.toDouble() / sampleRate
                    val env = 1.0 - ((i % chunk).toDouble() / chunk)
                    sample[i] = sin(2 * Math.PI * freqs[idx] * t) * env
                }
            }
            SfxType.WRONG -> {
                // Low buzz wobble
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val freq = 180.0 + 30.0 * sin(2 * Math.PI * 15.0 * t)
                    val env = 1.0 - (i.toDouble() / numSamples)
                    sample[i] = sin(2 * Math.PI * freq * t) * env
                }
            }
            SfxType.LEVEL_UP -> {
                // Rising pentatonic fanfare
                val notes = doubleArrayOf(523.25, 659.25, 783.99, 987.77, 1046.50, 1318.51)
                val chunk = numSamples / notes.size
                for (i in 0 until numSamples) {
                    val idx = (i / chunk).coerceIn(0, notes.size - 1)
                    val t = i.toDouble() / sampleRate
                    val env = 1.0 - ((i % chunk).toDouble() / chunk)
                    sample[i] = sin(2 * Math.PI * notes[idx] * t) * env
                }
            }
            SfxType.FANFARE -> {
                // Victory brassy chords
                val f1 = 523.25
                val f2 = 659.25
                val f3 = 783.99
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val env = 1.0 - (i.toDouble() / numSamples)
                    sample[i] = (sin(2 * Math.PI * f1 * t) + sin(2 * Math.PI * f2 * t) + sin(2 * Math.PI * f3 * t)) / 3.0 * env
                }
            }
            SfxType.STARS_EARNED -> {
                val freq = if (customFreq > 0f) customFreq.toDouble() else 880.0
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val env = sin(Math.PI * i / numSamples)
                    sample[i] = sin(2 * Math.PI * freq * t) * env
                }
            }
            SfxType.XYLOPHONE_NOTE -> {
                val freq = if (customFreq > 0f) customFreq.toDouble() else 523.25
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val env = Math.exp(-t * 8.0)
                    sample[i] = (sin(2 * Math.PI * freq * t) + 0.3 * sin(2 * Math.PI * freq * 2 * t)) * env
                }
            }
        }

        val effectiveVol = (sfxVolume * 32767).toInt().coerceIn(0, 32767)
        for (i in 0 until numSamples) {
            buffer[i] = (sample[i] * effectiveVol).toInt().toShort()
        }

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(buffer.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(buffer, 0, buffer.size)
        track.play()
        thread {
            Thread.sleep(durationMs.toLong() + 50)
            try {
                track.release()
            } catch (_: Exception) {}
        }
    }

    private fun startBackgroundMusic() {
        if (isMusicPlaying) return
        isMusicPlaying = true
        musicThread = thread {
            val sampleRate = 22050
            // Kid friendly soothing lullaby melody loop: C - E - G - A - G - E - C
            val melody = doubleArrayOf(261.63, 329.63, 392.00, 440.00, 392.00, 329.63, 261.63, 349.23, 392.00, 523.25)
            val noteDurationMs = 400
            val numSamplesPerNote = (sampleRate * (noteDurationMs / 1000.0)).toInt()

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(numSamplesPerNote * 2)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            track.play()

            var noteIdx = 0
            while (isMusicPlaying) {
                if (!isMusicEnabled) {
                    Thread.sleep(300)
                    continue
                }
                val freq = melody[noteIdx % melody.size]
                val noteBuffer = ShortArray(numSamplesPerNote)
                val vol = (musicVolume * 0.18f * 32767).toInt().coerceIn(0, 32767)

                for (i in 0 until numSamplesPerNote) {
                    val t = i.toDouble() / sampleRate
                    val env = sin(Math.PI * i / numSamplesPerNote)
                    val s = (sin(2 * Math.PI * freq * t) + 0.2 * sin(2 * Math.PI * (freq * 0.5) * t)) * env
                    noteBuffer[i] = (s * vol).toInt().toShort()
                }

                track.write(noteBuffer, 0, noteBuffer.size)
                noteIdx++
            }

            try {
                track.stop()
                track.release()
            } catch (_: Exception) {}
        }
    }

    fun release() {
        isMusicPlaying = false
        try {
            musicThread?.interrupt()
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}
