package com.example.apphub

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class ContadorPontosActivity : AppCompatActivity() {
    private var pontuacaoTimeA: Int = 0
    private var pontuacaoTimeB: Int = 0

    private lateinit var pTimeA: TextView
    private lateinit var pTimeB: TextView
    private lateinit var sharedPreferences: SharedPreferences

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

        if (isDarkMode) {
            switchTema.text = "Modo Claro"
        } else {
            switchTema.text = "Modo Escuro"
        }

        switchTema.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("isDarkMode", isChecked).apply()
            recreate()
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
}