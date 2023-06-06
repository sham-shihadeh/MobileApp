package com.example.fypcode


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var EditId : EditText
    private lateinit var EtLoginName: EditText
    private lateinit var etPassword : EditText
    private lateinit var btnValidate : Button
    private lateinit var dbHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DataBaseHelper(this)

        findViewById<EditText>(R.id.editId).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            transformationMethod = PasswordTransformationMethod.getInstance()
        }

        // Set up the EtLoginName EditText to automatically capitalize the first letter of each word
        findViewById<EditText>(R.id.etLoginName).inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS

        // Set up the etPassword EditText to display stars and show only the number keyboard
        findViewById<EditText>(R.id.etPassword).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            transformationMethod = PasswordTransformationMethod.getInstance()
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            val id = findViewById<EditText>(R.id.editId).text.toString().trim()
            val password = findViewById<EditText>(R.id.etPassword).text.toString().trim()
            val loginName = findViewById<EditText>(R.id.etLoginName).text.toString().trim()

            if (id.isEmpty() || id.isBlank()) {
                findViewById<EditText>(R.id.editId).error = "id required"
                return@setOnClickListener
            } else if (loginName.isEmpty() || loginName.isBlank()) {
                findViewById<EditText>(R.id.etLoginName).error = "LoginName required"
                return@setOnClickListener
            } else if (password.isEmpty() || password.isBlank()) {
                findViewById<EditText>(R.id.etPassword).error = "password required"
                return@setOnClickListener
            } else {
                // Query the database to check if the login details are valid
                val db = dbHelper.readableDatabase
                val query =
                    "SELECT * FROM ${DataBaseHelper.TABLE_NAME_STUDENT} WHERE ${DataBaseHelper.COLUMN_STUDENT_ID} = ? AND ${DataBaseHelper.COLUMN_STUDENT_LOGIN_NAME} = ? AND ${DataBaseHelper.COLUMN_STUDENT_PASSWORD} = ?"
                val cursor = db.rawQuery(query, arrayOf(id, loginName, password))

                if (cursor != null && cursor.count > 0) {
                    // Login successful - navigate to the student page
                    cursor.moveToFirst()
                    val studentIdIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_STUDENT_ID)
                    if (studentIdIndex != -1) {
                        val studentId = cursor.getString(studentIdIndex)
                        val intent = Intent(this, MainActivityStudent::class.java)
                        intent.putExtra("student_id", studentId)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Invalid login details", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Login failed - display an error message
                    Toast.makeText(this, "Invalid login details", Toast.LENGTH_SHORT).show()
                }

                cursor?.close()
                db.close()

                // Hide the keyboard after the user presses the login button
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
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

    fun AdminloginButton(view: View) {
        setContentView(R.layout.android_main_admin)
        EditId = findViewById(R.id.editId)
        etPassword = findViewById(R.id.etPassword)
        EtLoginName = findViewById(R.id.etLoginName)
        btnValidate = findViewById(R.id.button)

        dbHelper = DataBaseHelper(this)

        // Set input type for EtLoginName
        EtLoginName.setRawInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS)

        // Set input type for etPassword
        etPassword.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        btnValidate.setOnClickListener {
            val id = EditId.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val loginName = EtLoginName.text.toString().trim()

            if (id.isEmpty() || id.isBlank()) {
                EditId.error = "id required"
                return@setOnClickListener
            } else if (loginName.isEmpty() || loginName.isBlank()) {
                EtLoginName.error = "LoginName required"
                return@setOnClickListener
            } else if (password.isEmpty() || password.isBlank()) {
                etPassword.error = "password required"
                return@setOnClickListener
            } else {
                val db = dbHelper.readableDatabase

                val selection =
                    "${DataBaseHelper.COLUMN_ADMIN_ID} = ? AND ${DataBaseHelper.COLUMN_ADMIN_LOGIN_NAME} = ? AND ${DataBaseHelper.COLUMN_ADMIN_PASSWORD} = ?"
                val selectionArgs = arrayOf(id, loginName, password)
                val cursor = db.query(
                    DataBaseHelper.TABLE_NAME_ADMIN,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
                )

                if (cursor != null && cursor.count > 0) {
                    // Login successful - navigate to the admin page
                    cursor.moveToFirst()
                    val adminIdIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_ADMIN_ID)
                    if (adminIdIndex != -1) {
                        val adminId = cursor.getString(adminIdIndex)
                        val intent = Intent(this, MainActivityAdmin::class.java)
                        intent.putExtra("admin_id", adminId)
                        startActivity(intent)
                    }
                } else {
                    // Login failed - display an error message
                    Toast.makeText(this, "Invalid login details", Toast.LENGTH_SHORT).show()
                }

                cursor?.close()
                db.close()

            }
        }
    }
}


