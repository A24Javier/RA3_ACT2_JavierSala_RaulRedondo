package com.example.pianoejercicio

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var octetIndex = 0;
    val stringOctets = arrayOf("C2", "C3", "C4")
    lateinit var octetTextView: TextView

    val pianoSoundsC2 = intArrayOf(
        R.raw.c2,
        R.raw.d2,
        R.raw.e2,
        R.raw.f2,
        R.raw.g2,
        R.raw.a2,
        R.raw.b2,
        R.raw.cs2,
        R.raw.ds2,
        R.raw.fs2,
        R.raw.gs2,
        R.raw.as2
    )
    val pianoSoundsC3 = intArrayOf(
        R.raw.c3,
        R.raw.d3,
        R.raw.e3,
        R.raw.f3,
        R.raw.g3,
        R.raw.a3,
        R.raw.b3,
        R.raw.cs3,
        R.raw.ds3,
        R.raw.fs3,
        R.raw.gs3,
        R.raw.as3
    )
    val pianoSoundsC4 = intArrayOf(
        R.raw.c4
    )

    val pianoSounds = arrayOf(
        pianoSoundsC2,
        pianoSoundsC3,
        pianoSoundsC4
    )

    lateinit var layoutButtons: Array<Button>
    lateinit var absOctButt: Button
    lateinit var addOctButt: Button

    lateinit var soundPool: SoundPool
    val soundMap = HashMap<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutButtons = arrayOf(
            findViewById(R.id.teclaC),
            findViewById(R.id.teclaD),
            findViewById(R.id.teclaE),
            findViewById(R.id.teclaF),
            findViewById(R.id.teclaG),
            findViewById(R.id.teclaA),
            findViewById(R.id.teclaB),
            findViewById(R.id.teclaCb),
            findViewById(R.id.teclaDb),
            findViewById(R.id.teclaFb),
            findViewById(R.id.teclaGb),
            findViewById(R.id.teclaAb)
        )

        addOctButt = findViewById(R.id.AddOctet)
        absOctButt = findViewById(R.id.AbsOctet)

        octetTextView = findViewById(R.id.octetText)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(12)
            .setAudioAttributes(audioAttributes)
            .build()

        for (octave in pianoSounds.indices) {
            for (soundRes in pianoSounds[octave]) {
                val soundId = soundPool.load(this, soundRes, 1)
                soundMap[soundRes] = soundId
            }
        }

        LoadButtons()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun LoadButtons() {
        octetTextView.text = stringOctets[octetIndex]

        for (i in layoutButtons.indices) {
            val hasSound = i < pianoSounds[octetIndex].size
            layoutButtons[i].isEnabled = hasSound

            if (hasSound) {
                layoutButtons[i].alpha = 1f
                layoutButtons[i].isClickable = true
                layoutButtons[i].isEnabled = true

                layoutButtons[i].setOnClickListener {
                    val resId = pianoSounds[octetIndex][i]
                    val soundId = soundMap[resId]

                    soundId?.let {
                        soundPool.play(it, 1f, 1f, 1, 0, 1f)
                    }
                }

            } else {
                layoutButtons[i].alpha = 0.3f
                layoutButtons[i].setOnClickListener(null)
                layoutButtons[i].isClickable = false
                layoutButtons[i].isEnabled = false
            }
        }

        LoadChangeOctetButtons()
    }

    private fun LoadChangeOctetButtons(){
        absOctButt.setOnClickListener(null)
        addOctButt.setOnClickListener(null)

        if(octetIndex > 0){
            absOctButt.alpha = 1f
            absOctButt.isClickable = true
            absOctButt.isEnabled = true

            absOctButt.setOnClickListener {
                octetIndex--
                LoadButtons()
            }
        }
        else{
            absOctButt.alpha = 0.3f
            absOctButt.isClickable = false
            absOctButt.isEnabled = false
        }

        if(octetIndex < pianoSounds.size - 1){
            addOctButt.alpha = 1f
            addOctButt.isClickable = true
            addOctButt.isEnabled = true

            addOctButt.setOnClickListener {
                octetIndex++
                LoadButtons()
            }
        }
        else{
            addOctButt.alpha = 0.3f
            addOctButt.isClickable = false
            addOctButt.isEnabled = false
        }
    }
}