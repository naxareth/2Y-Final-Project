package com.second_year.finalproject.Api.Interfaces

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import com.second_year.finalproject.R
import com.second_year.finalproject.Api.RetrofitClient


class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var buttonResetPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password) // Fixed incorrect package reference

        etUsername = findViewById(R.id.etUsername)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        buttonResetPassword = findViewById(R.id.buttonResetPassword)

        buttonResetPassword.setOnClickListener {
            resetPassword()
        }

        findViewById<TextView>(R.id.textViewCancel).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun resetPassword() {
        val username = etUsername.text.toString().trim()
        val newPassword = etNewPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (username.isEmpty()) {
            etUsername.error = "Username is required"
            etUsername.requestFocus()
            return
        }

        if (newPassword.isEmpty()) {
            etNewPassword.error = "New password is required"
            etNewPassword.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.error = "Confirm password is required"
            etConfirmPassword.requestFocus()
            return
        }

        if (newPassword.length < 6) {
            etNewPassword.error = "Password should be at least 6 characters long"
            etNewPassword.requestFocus()
            return
        }

        if (newPassword != confirmPassword) {
            etConfirmPassword.error = "Passwords do not match"
            etConfirmPassword.requestFocus()
            return
        }

        val call: Call<ResponseBody> = RetrofitClient.api.updatePassword(username, newPassword)


        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObject = JSONObject(responseString)
                        val message = jsonObject.getString("message")
                        Toast.makeText(this@ForgotPasswordActivity, message, Toast.LENGTH_LONG).show()

                        if (!jsonObject.getBoolean("error")) {
                            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this@ForgotPasswordActivity, "No response from server", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this@ForgotPasswordActivity, "Invalid response format", Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@ForgotPasswordActivity, "Error reading response", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ForgotPasswordActivity, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
