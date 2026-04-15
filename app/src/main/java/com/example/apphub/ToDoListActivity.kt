package com.example.apphub

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import android.widget.CheckBox

data class Tarefa(var texto: String, var isConcluida: Boolean = false) : java.io.Serializable

class ToDoListActivity : AppCompatActivity() {
    private val tasks = mutableListOf<Tarefa>()
    private lateinit var adapter: TaskAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTask: TextInputEditText
    private lateinit var btnAdd: MaterialButton
    private var editingPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)

        if (isDarkMode) {
            setTheme(R.style.DarkThemeBasquete)
        } else {
            setTheme(R.style.LightThemeBasquete)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        editTask = findViewById(R.id.editTask)
        btnAdd = findViewById(R.id.btnAdd)

        val listView = findViewById<ListView>(R.id.listView)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val switchTema: SwitchCompat = findViewById(R.id.switchTema)

        adapter = TaskAdapter(this, tasks)
        listView.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }

        switchTema.isChecked = isDarkMode

        switchTema.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("isDarkMode", isChecked).apply()
            recreate()
        }

        btnAdd.setOnClickListener {
            val text = editTask.text.toString()

            if (text.isNotEmpty()) {
                if (editingPosition != null) {
                    tasks[editingPosition!!].texto = text
                    editingPosition = null
                    btnAdd.text = "Adicionar"
                } else {
                    tasks.add(Tarefa(texto = text))
                }

                adapter.notifyDataSetChanged()
                editTask.text?.clear()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("minhas_tarefas", ArrayList(tasks))
        outState.putString("texto_editado", editTask.text.toString())
        editingPosition?.let { outState.putInt("posicao_editada", it) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedTasks = savedInstanceState.getSerializable("minhas_tarefas") as? ArrayList<Tarefa>
        if (savedTasks != null) {
            tasks.clear()
            tasks.addAll(savedTasks)
            adapter.notifyDataSetChanged()
        }

        if (savedInstanceState.containsKey("posicao_editada")) {
            editingPosition = savedInstanceState.getInt("posicao_editada")
            editTask.setText(savedInstanceState.getString("texto_editado"))
            btnAdd.text = "Salvar Alteração"
        }
    }

    inner class TaskAdapter(context: Context, private val taskList: MutableList<Tarefa>) :
        ArrayAdapter<Tarefa>(context, R.layout.item_tarefa, taskList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_tarefa, parent, false)

            val taskText = view.findViewById<TextView>(R.id.taskText)
            val checkboxTask = view.findViewById<CheckBox>(R.id.checkboxTask)
            val btnEdit = view.findViewById<ImageButton>(R.id.btnEditTask)
            val btnDelete = view.findViewById<ImageButton>(R.id.btnDeleteTask)

            val task = taskList[position]

            checkboxTask.setOnCheckedChangeListener(null)
            checkboxTask.isChecked = task.isConcluida

            if (task.isConcluida) {
                taskText.text = "${task.texto} ✅"
                taskText.paintFlags = taskText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                taskText.text = task.texto
                taskText.paintFlags = taskText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            checkboxTask.setOnCheckedChangeListener { _, isChecked ->
                task.isConcluida = isChecked
                notifyDataSetChanged()
            }

            btnEdit.setOnClickListener {
                editTask.setText(task.texto)
                editTask.setSelection(task.texto.length)
                editingPosition = position
                btnAdd.text = "Salvar Alteração"
            }

            btnDelete.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Remover Tarefa")
                    .setMessage("Deseja realmente remover a tarefa?")
                    .setPositiveButton("Sim") { dialog, _ ->
                        if (editingPosition == position) {
                            editingPosition = null
                            editTask.text?.clear()
                            btnAdd.text = "Adicionar"
                        } else if (editingPosition != null && position < editingPosition!!) {
                            editingPosition = editingPosition!! - 1
                        }

                        taskList.removeAt(position)
                        notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Não") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            return view
        }
    }
}