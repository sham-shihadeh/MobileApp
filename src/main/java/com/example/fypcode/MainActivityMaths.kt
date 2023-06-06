package com.example.fypcode

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fypcode.Model.Question

class MainActivityMaths : AppCompatActivity() {

    private val dbHelper = DataBaseHelper(this)

    // declare variables for UI elements
    private lateinit var questionTextView: TextView
    private lateinit var answersRadioGroup: RadioGroup
    private lateinit var answer1RadioButton: RadioButton
    private lateinit var answer2RadioButton: RadioButton
    private lateinit var answer3RadioButton: RadioButton
    private lateinit var answer4RadioButton: RadioButton
    private lateinit var submitButton: Button
    private lateinit var nextButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_maths)

        // Initialize UI elements
        questionTextView = findViewById(R.id.tv_question)
        answersRadioGroup = findViewById(R.id.rg_answers)
        answer1RadioButton = findViewById(R.id.rb_answer1)
        answer2RadioButton = findViewById(R.id.rb_answer2)
        answer3RadioButton = findViewById(R.id.rb_answer3)
        answer4RadioButton = findViewById(R.id.rb_answer4)
        submitButton = findViewById(R.id.btn_submit)
        nextButton = findViewById(R.id.btn_submit)


        // Retrieve questions from the database
        val dbHelper = DataBaseHelper(this)
        val questions = dbHelper.getQuestionsFromId(1,2)
       // Set up the current question index
        var currentQuestionIndex = 0

       // Set the text of the questionTextView to the first question
        questionTextView.text = questions[currentQuestionIndex].QuestionText

        // Set up the Next button to show the next question
        nextButton.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                questionTextView.text = questions[currentQuestionIndex].QuestionText
            } else {
                // Disable the Next button or display a message
                nextButton.isEnabled = false

                // Enable the submit button if there are no more questions
                submitButton.isEnabled = true

                // Redirect the user to a new activity when the submit button is pressed
                submitButton.setOnClickListener {
                    val intent = Intent(this, MainActivityResult::class.java)
                    startActivity(intent)
                }
            }
        }
    }






    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.subjectmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.muMathematicsPage -> {
                startActivity(Intent(this, MainActivityMaths::class.java))
                true
            }
            R.id.muEnglishPage -> {
                startActivity(Intent(this, MainActivityEnglish::class.java))
                true
            }
            R.id.muLoginPage -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            R.id.muSelectionPage -> {
                startActivity(Intent(this, MainActivityStudent::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
