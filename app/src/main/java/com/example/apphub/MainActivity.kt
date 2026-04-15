package com.example.apphub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.btnBasquete).setOnClickListener {
            startActivity(Intent(this, ContadorPontosActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnCalculadora).setOnClickListener {
            startActivity(Intent(this, CalculadoraActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnBlocoDeNotas).setOnClickListener {
            startActivity(Intent(this, ToDoListActivity::class.java))
        }
    }
}