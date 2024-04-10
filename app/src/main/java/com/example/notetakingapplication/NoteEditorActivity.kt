package com.example.notetakingapplication

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.notetakingapplication.R
import java.util.*

class NoteEditorActivity : AppCompatActivity() {

    private var noteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        val editText = findViewById<EditText>(R.id.editText)
        val saveButton = findViewById<Button>(R.id.saveButton)

        val intent = intent
        noteId = intent.getIntExtra("noteId", -1)

        if (noteId != -1) {
            val noteText = intent.getStringExtra("noteText")
            editText.setText(noteText)
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // add your code here
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // add your code here
            }

            override fun afterTextChanged(editable: Editable) {
                // add your code here
            }
        })

        saveButton.setOnClickListener {
            val noteText = editText.text.toString()
            saveNoteToSharedPreferences(noteText)
            finish() // Finish the activity after saving
        }
    }

    private fun saveNoteToSharedPreferences(noteText: String) {
        val sharedPreferences = applicationContext.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE)
        val notesSet = sharedPreferences.getStringSet("notes", HashSet()) ?: HashSet()
        val notesList = ArrayList(notesSet)
        if (noteId != -1) {
            notesList[noteId] = noteText
        } else {
            notesList.add(noteText)
            noteId = notesList.size - 1
        }
        sharedPreferences.edit().putStringSet("notes", HashSet(notesList)).apply()
    }
}
