package com.example.fypcode

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fypcode.Model.Question
import com.example.fypcode.Model.Subject
import kotlin.random.Random


class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "database.db"

        // Define the table name and columns
        const val TABLE_NAME_STUDENT = "Student"
        const val COLUMN_STUDENT_ID = "StudentId"
        const val COLUMN_STUDENT_LOGIN_NAME = "StudentLoginName"
        const val COLUMN_STUDENT_PASSWORD = "StudentPassword"

        const val TABLE_NAME_ADMIN = "Admin"
        const val COLUMN_ADMIN_ID = "AdminId"
        const val COLUMN_ADMIN_LOGIN_NAME = "AdminLoginName"
        const val COLUMN_ADMIN_PASSWORD = "AdminPassword"

        const val TABLE_NAME_QUESTIONS = "Questions"
        const val COLUMN_QUESTION_ID = "QuestionId"
        const val COLUMN_QUESTION_TEXT = "QuestionText"
        const val COLUMN_ANSWER = "Answer"
        private const val COLUMN_SUBJECT_ID_question = "subject_id"

        const val TABLE_NAME_ANSWER= "Answer"
        const val COLUMN_ANSWER_ID = "AnswerId"
        const val COLUMN_ANSWER_TEXT = "AnswerText"

        private const val TABLE_NAME_SUBJECTS = "Subject"
        private const val COLUMN_SUBJECT_ID = "subject_id"
        private const val COLUMN_SUBJECT_NAME = "subject_name"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "SELECT * FROM $TABLE_NAME_STUDENT"
        val cursor = db?.rawQuery(query, null)
        if (cursor == null || cursor.count == 0) {
            val CREATE_STUDENT_TABLE = ("CREATE TABLE IF NOT EXISTS $TABLE_NAME_STUDENT "
                    + "($COLUMN_STUDENT_ID INTEGER PRIMARY KEY, "
                    + "$COLUMN_STUDENT_LOGIN_NAME TEXT,"
                    + "$COLUMN_STUDENT_PASSWORD INTEGER)")
            db?.execSQL(CREATE_STUDENT_TABLE)
        }

        val queryAdmin = "SELECT * FROM $TABLE_NAME_ADMIN"
        val cursorAdmin = db?.rawQuery(queryAdmin, null)
        if (cursorAdmin == null || cursorAdmin.count == 0) {
            val CREATE_ADMIN_TABLE = ("CREATE TABLE IF NOT EXISTS $TABLE_NAME_ADMIN "
                    + "($COLUMN_ADMIN_ID INTEGER PRIMARY KEY, "
                    + "$COLUMN_ADMIN_LOGIN_NAME TEXT,"
                    + "$COLUMN_ADMIN_PASSWORD TEXT)")
            db?.execSQL(CREATE_ADMIN_TABLE)
        }

        val queryQuestions = "SELECT * FROM $TABLE_NAME_QUESTIONS"
        val cursorQuestions = db?.rawQuery(queryQuestions, null)
        if (cursorQuestions == null || cursorQuestions.count == 0) {
            val CREATE_QUESTIONS_TABLE = ("CREATE TABLE IF NOT EXISTS $TABLE_NAME_QUESTIONS "
                    + "($COLUMN_QUESTION_ID INTEGER PRIMARY KEY, "
                    + "$COLUMN_QUESTION_TEXT TEXT,"
                    + "$COLUMN_ANSWER TEXT,"
                    + "$COLUMN_STUDENT_ID INTEGER)"
                    + "$COLUMN_STUDENT_ID INTEGER)"
                    + "$COLUMN_SUBJECT_ID INTEGER")
            db?.execSQL(CREATE_QUESTIONS_TABLE)
        }


        val queryAnswers = "SELECT * FROM $TABLE_NAME_ANSWER"
        val cursorAnswers = db?.rawQuery(queryAnswers, null)
        if (cursorAnswers == null || cursorAnswers.count == 0) {
            val CREATE_ANSWERS_TABLE = ("CREATE TABLE IF NOT EXISTS $TABLE_NAME_ANSWER "
                    + "($COLUMN_ANSWER_ID INTEGER PRIMARY KEY, "
                    + "$COLUMN_ANSWER_TEXT TEXT,"
                    + "$COLUMN_QUESTION_ID INTEGER,"
                    + "FOREIGN KEY($COLUMN_QUESTION_ID) REFERENCES $TABLE_NAME_QUESTIONS($COLUMN_QUESTION_ID))")
            db?.execSQL(CREATE_ANSWERS_TABLE)
        }

        val querySubjects = "SELECT * FROM $TABLE_NAME_SUBJECTS"
        val cursorSubject = db?.rawQuery(querySubjects, null)
        if (cursorSubject == null || cursorSubject.count == 0) {
            val CREATE_QUESTIONS_SUBJECT= ("CREATE TABLE IF NOT EXISTS $TABLE_NAME_SUBJECTS "
                    + "($COLUMN_SUBJECT_ID INTEGER PRIMARY KEY, "
                    + "$COLUMN_SUBJECT_NAME TEXT," )
            db?.execSQL(CREATE_QUESTIONS_SUBJECT)
        }
    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_STUDENT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ADMIN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_QUESTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ANSWER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_SUBJECTS")
        onCreate(db)
    }


    fun addQuestion(question: Question) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUESTION_TEXT, question.QuestionText)

        }
        val questionId = db.insert(TABLE_NAME_QUESTIONS, null, values)
        question.QuestionId = questionId.toInt()
        db.close()
    }


    fun getAllQuestions(): List<Question> {
        val questions = mutableListOf<Question>()
        val selectQuery = "SELECT * FROM $TABLE_NAME_QUESTIONS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                //TODO - CHECK THIS WITH THE DATABASE COLUMN NUMBERS
                val questionId = cursor.getInt(0)
                val questionText = cursor.getString(1)
                val studentId = cursor.getInt(2)
                val answer = cursor.getString(3)
                val question = Question(questionId, questionText, studentId)
                questions.add(question)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return questions
    }
    fun getQuestionsFromId(minId: Int, maxId: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val selectQuery = "SELECT * FROM $TABLE_NAME_QUESTIONS WHERE $COLUMN_QUESTION_ID >= $minId AND $COLUMN_QUESTION_ID <= $maxId"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                //TODO - CHECK THIS WITH THE DATABASE COLUMN NUMBERS
                val questionId = cursor.getInt(0)
                val questionText = cursor.getString(1)
                val studentId = cursor.getInt(2)
                val answer = cursor.getString(3)
                val question = Question(questionId, questionText, studentId)
                questions.add(question)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return questions
    }








}

