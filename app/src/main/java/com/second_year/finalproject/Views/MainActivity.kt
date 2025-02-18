package com.second_year.finalproject.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.second_year.finalproject.Api.RetrofitClient
import com.second_year.finalproject.R
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), android.view.View.OnClickListener {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)

        val buttonSignUp: Button = findViewById(R.id.buttonSignUp)
        val textViewLogin: TextView = findViewById(R.id.textViewLogin)

        buttonSignUp.setOnClickListener(this)
        textViewLogin.setOnClickListener(this)

        findViewById<TextView>(R.id.textViewForgotPassword).setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    override fun onClick(view: android.view.View) {
        when (view.id) {
            R.id.buttonSignUp -> userSignup()
            R.id.textViewLogin -> startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun userSignup() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (username.isEmpty()) {
            etUsername.error = "Username is Required"
            etUsername.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Password is Required"
            etPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            etPassword.error = "Password should be at least 6 characters long"
            etPassword.requestFocus()
            return
        }

        val call: Call<ResponseBody> = RetrofitClient.api.createUser(username, password)


        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var s: String? = response.body()?.string()

                if (s != null) {
                    try {
                        val jsonObject = JSONObject(s)
                        Toast.makeText(
                            this@MainActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()

                        if (jsonObject.getString("message") == "User registered successfully") {
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
