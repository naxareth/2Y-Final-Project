package com.second_year.finalproject.Api.Interfaces

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.second_year.finalproject.R
import com.second_year.finalproject.Api.RetrofitClient
import com.second_year.finalproject.Views.MainActivity
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)

        findViewById<View>(R.id.buttonLogin).setOnClickListener(this)
        findViewById<View>(R.id.textViewRegister).setOnClickListener(this)

        findViewById<View>(R.id.textViewForgotPassword).setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonLogin -> userLogin()
            R.id.textViewRegister -> startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun userLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (username.isEmpty()) {
            etUsername.error = "Username is required"
            etUsername.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            etPassword.error = "Password should be at least 6 characters long"
            etPassword.requestFocus()
            return
        }

        // ✅ Use RetrofitClient.api instead of RetrofitClient.instance.api
        val api = RetrofitClient.api
        val call: Call<ResponseBody> = api.userLogin(username, password)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { responseString ->
                        try {
                            val jsonObject = JSONObject(responseString)
                            val message = jsonObject.getString("message")

                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()

                            if (message == "Login successful") {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish() // Close LoginActivity after successful login
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@LoginActivity,
                                "JSON Error: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error: ${response.errorBody()?.string()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Request failed: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}