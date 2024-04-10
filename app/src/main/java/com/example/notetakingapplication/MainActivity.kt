package com.example.notetakingapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_note) {
            val intent = Intent(applicationContext, NoteEditorActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        val sharedPreferences = applicationContext.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE)
        val set = sharedPreferences.getStringSet("notes", null)

        val notes = if (set == null) {
            ArrayList(listOf("Example note"))
        } else {
            ArrayList(set)
        }

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notes)

        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(applicationContext, NoteEditorActivity::class.java)
            intent.putExtra("noteId", i)
            intent.putExtra("noteText", notes[i])
            startActivity(intent)
        }

        listView.setOnItemLongClickListener { _, view, i, _ ->
            AlertDialog.Builder(this@MainActivity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes") { _, _ ->
                    notes.removeAt(i)
                    arrayAdapter.notifyDataSetChanged()
                    val sharedPreferences = applicationContext.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE)
                    val set = HashSet(notes)
                    sharedPreferences.edit().putStringSet("notes", set).apply()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        reloadNotes()
    }

    private fun reloadNotes() {
        val sharedPreferences = applicationContext.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE)
        val set = sharedPreferences.getStringSet("notes", null)

        val notes = if (set == null) {
            ArrayList(listOf("Example note"))
        } else {
            ArrayList(set)
        }

        arrayAdapter.clear()
        arrayAdapter.addAll(notes)
        arrayAdapter.notifyDataSetChanged()
    }
}
