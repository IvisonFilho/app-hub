package com.example.apphub

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.os.SystemClock
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class ContadorPontosActivity : AppCompatActivity() {
    private var pontuacaoTimeA: Int = 0
    private var pontuacaoTimeB: Int = 0

    private lateinit var pTimeA: TextView
    private lateinit var pTimeB: TextView
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var tvCronometro: TextView
    private var isRunning: Boolean = false
    private var startTime = 0L
    private var timeInMilliseconds = 0L
    private var timeSwapBuff = 0L
    private var updateTime = 0L
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)

        if (isDarkMode) {
            setTheme(R.style.DarkThemeBasquete)
        } else {
            setTheme(R.style.LightThemeBasquete)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_contador_pontos)

        pTimeA = findViewById(R.id.placarTimeA)
        pTimeB = findViewById(R.id.placarTimeB)

        val tiroLivreTimeA: Button = findViewById(R.id.tiroLivreA)
        val doisPontosTimeA: Button = findViewById(R.id.doisPontosA)
        val tresPontosTimeA: Button = findViewById(R.id.tresPontosA)

        val tiroLivreTimeB: Button = findViewById(R.id.tiroLivreB)
        val doisPontosTimeB: Button = findViewById(R.id.doisPontosB)
        val tresPontosTimeB : Button = findViewById(R.id.tresPontosB)

        val switchTema: SwitchCompat = findViewById(R.id.switchTema)
        switchTema.isChecked = isDarkMode

        switchTema.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("isDarkMode", isChecked).apply()
            recreate()
        }

        tvCronometro = findViewById(R.id.cronometro)
        val btnPlayPause: Button = findViewById(R.id.btnPlayPause)
        val btnFinalizar: Button = findViewById(R.id.btnFinalizar)

        runnable = object : Runnable {
            override fun run() {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime
                updateTime = timeSwapBuff + timeInMilliseconds

                val secs = (updateTime / 1000).toInt()
                val mins = secs / 60
                val milliseconds = (updateTime % 1000).toInt()
                val seconds = secs % 60

                tvCronometro.text = String.format("%02d:%02d.%02d", mins, seconds, milliseconds / 10)

                handler.postDelayed(this, 10)
            }
        }

        btnPlayPause.setOnClickListener {
            if (isRunning) {
                timeSwapBuff += timeInMilliseconds
                handler.removeCallbacks(runnable)
                isRunning = false
                btnPlayPause.text = "Play"
            } else {
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(runnable, 0)
                isRunning = true
                btnPlayPause.text = "Pause"
            }
        }

        btnFinalizar.setOnClickListener {
            finalizarPartida(btnPlayPause)
        }

        val removerPontoTimeA: Button = findViewById(R.id.removerPontoTimeA)
        removerPontoTimeA.setOnClickListener { removerPonto("A") }

        val removerPontoTimeB: Button = findViewById(R.id.removerPontoTimeB)
        removerPontoTimeB.setOnClickListener { removerPonto("B") }

        val reiniciar: Button = findViewById(R.id.reiniciarPartida)

        tiroLivreTimeA.setOnClickListener { adicionarPontos(1, "A") }
        doisPontosTimeA.setOnClickListener { adicionarPontos(2, "A") }
        tresPontosTimeA.setOnClickListener { adicionarPontos(3, "A") }

        tiroLivreTimeB.setOnClickListener { adicionarPontos(1, "B") }
        doisPontosTimeB.setOnClickListener { adicionarPontos(2, "B") }
        tresPontosTimeB.setOnClickListener { adicionarPontos(3, "B") }

        reiniciar.setOnClickListener { reiniciarPartida() }
    }

    fun adicionarPontos(pontos: Int, time: String) {
        if(time == "A") {
            pontuacaoTimeA += pontos
        } else {
            pontuacaoTimeB += pontos
        }
        atualizarPlacar(time)
    }

    fun removerPonto(time: String) {
        if(time == "A") {
            if (pontuacaoTimeA > 0) pontuacaoTimeA -= 1
        } else {
            if (pontuacaoTimeB > 0) pontuacaoTimeB -= 1
        }
        atualizarPlacar(time)
    }

    fun atualizarPlacar(time: String) {
        if(time == "A") {
            pTimeA.text = pontuacaoTimeA.toString()
        } else {
            pTimeB.text = pontuacaoTimeB.toString()
        }
    }

    fun reiniciarPartida() {
        pontuacaoTimeA = 0
        pTimeA.text = pontuacaoTimeA.toString()

        pontuacaoTimeB = 0
        pTimeB.text = pontuacaoTimeB.toString()

        Toast.makeText(this, "Placar Reiniciado", Toast.LENGTH_SHORT).show()
    }

    private fun finalizarPartida(btnPlayPause: Button) {
        handler.removeCallbacks(runnable)
        isRunning = false
        btnPlayPause.text = "Play"

        val mensagemResultado = when {
            pontuacaoTimeA > pontuacaoTimeB -> "O Time A venceu a partida!"
            pontuacaoTimeB > pontuacaoTimeA -> "O Time B venceu a partida!"
            else -> "A partida terminou em Empate!"
        }

        AlertDialog.Builder(this)
            .setTitle("Fim de Jogo \uD83C\uDFC0")
            .setMessage("$mensagemResultado\n\nPlacar Final:\nTime A: $pontuacaoTimeA  x  Time B: $pontuacaoTimeB\nTempo Final: ${tvCronometro.text}")
            .setPositiveButton("Nova Partida") { dialog, _ ->
                reiniciarPartida()
                resetarCronometro()
                dialog.dismiss()
            }
            .setNegativeButton("Fechar") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetarCronometro() {
        startTime = 0L
        timeInMilliseconds = 0L
        timeSwapBuff = 0L
        updateTime = 0L
        tvCronometro.text = "00:00.00"
        handler.removeCallbacks(runnable)
    }
}