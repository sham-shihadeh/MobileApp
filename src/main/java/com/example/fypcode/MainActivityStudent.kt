package com.example.fypcode

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.fypcode.Model.Subject

class MainActivityStudent : AppCompatActivity() {

    private lateinit var subjectSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_student)

        subjectSpinner = findViewById(R.id.SubjectSpinner)

        val db = DataBaseHelper(this)
        val subjects = listOf("", "Mathematics", "English")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subjectSpinner.adapter = adapter

        subjectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedSubjectName = parent.getItemAtPosition(position) as String
                // get the selected subject name

                when (selectedSubjectName) {
                    "Mathematics" -> {
                        val intent = Intent(this@MainActivityStudent, MainActivityMaths::class.java)
                        startActivity(intent)
                    }
                    "English" -> {
                        val intent = Intent(this@MainActivityStudent, MainActivityEnglish::class.java)
                        startActivity(intent)
                    }
                    "No selection" -> {
                        // Do nothing
                    }
                    else -> {
                        // Do nothing
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.muLoginPage -> {
                // Navigate to the login page
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        subjectSpinner.setSelection(0)
    }
}
