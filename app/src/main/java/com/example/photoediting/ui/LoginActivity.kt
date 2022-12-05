package com.example.photoediting.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.photoediting.remote.ApiConfig
import com.example.photoediting.remote.LoginResponse
import com.example.photoediting.remote.LoginResult
import com.example.photoediting.remote.SessionManager
import com.example.photoediting.ui.customui.MyButton
import com.example.photoediting.ui.customui.MyEditText
import com.example.photoediting.ui.customui.MyPassEditText
import example.photoediting.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    private lateinit var myEnterButton: MyButton
    private lateinit var myEmailEditText: MyEditText
    private lateinit var myPassEditText: MyPassEditText
    private lateinit var edToRegister: TextView
    private lateinit var progressBar : ProgressBar

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sessionManager: SessionManager

    private var SHARED_PREF_NAME = "mypref"
    private var KEY_ID = "key_email"
    private var KEY_NAME = "key_name"
    private var KEY_TOKEN = "key_token"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        myEnterButton = findViewById(R.id.id_my_button)
        myEmailEditText = findViewById(R.id.id_my_email_et)
        myPassEditText = findViewById(R.id.id_my_pass_et)
        edToRegister = findViewById(R.id.id_register_tv)
        progressBar = findViewById(R.id.progress_bar)

        setMyButtonEnable()

        sessionManager = SessionManager(this)

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(KEY_TOKEN, null)
        if (token != null) {
            val i = Intent(this, HomePageActivity::class.java)
            startActivity(i)
            finish()
        }

        myEmailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        myEnterButton.setOnClickListener {
            val thisEmail = myEmailEditText.text.toString()
            val thisPassword = myPassEditText.text.toString()
            startLogin(thisEmail, thisPassword)
        }

        myPassEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        edToRegister.setOnClickListener {
            val i = Intent(this, EditImageActivity::class.java)
            startActivity(i)
        }

//        showLoading(true)
    }

    private fun startLogin(name: String, password: String) {
        showLoading(true)
        progressBar.visibility = View.VISIBLE
        val client = ApiConfig.getApiService(this).login(
            name,
            password
        )
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, responseBody?.message, Toast.LENGTH_SHORT)
                        .show()
                    if (responseBody != null) {
                        setSession(responseBody.loginResult)
                        sessionManager.saveAuthToken(responseBody.loginResult.token)
                        Intent(this@LoginActivity, HomePageActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                } else {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@LoginActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setSession(session: LoginResult) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_ID, session.userId)
        editor.putString(KEY_NAME, session.name)
        editor.putString(KEY_TOKEN, session.token)
        editor.apply()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun setMyButtonEnable() {
        val enteredEmail = myEmailEditText.text
        val enteredPass = myPassEditText.text
        myEnterButton.isEnabled = enteredEmail != null && enteredEmail.toString()
            .isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(enteredEmail.toString())
            .matches() && enteredPass != null && enteredPass.toString()
            .isNotEmpty() && enteredPass.length >= 6
    }

}