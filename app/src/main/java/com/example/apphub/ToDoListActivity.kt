package com.example.apphub

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ToDoListActivity : ComponentActivity() {
    private val tasks = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        val editTask = findViewById<TextInputEditText>(R.id.editTask)
        val btnAdd = findViewById<MaterialButton>(R.id.btnAdd)
        val listView = findViewById<ListView>(R.id.listView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)
        listView.adapter = adapter

        btnAdd.setOnClickListener {
            val text = editTask.text.toString()

            if (text.isNotEmpty()) {
                tasks.add(text)
                adapter.notifyDataSetChanged()
                editTask.text?.clear()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            tasks.removeAt(position)
            adapter.notifyDataSetChanged()
        }
    }
}