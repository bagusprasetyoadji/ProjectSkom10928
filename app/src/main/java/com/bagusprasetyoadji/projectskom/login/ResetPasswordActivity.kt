package com.bagusprasetyoadji.projectskom.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.bagusprasetyoadji.projectskom.R
import com.bagusprasetyoadji.projectskom.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("Reset Password")

        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmailReset.text.toString().trim()
            if (email.isEmpty()){
                binding.etEmailReset.error = "Email Harus Diisi"
                binding.etEmailReset.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmailReset.error = "Email Tidak Valid"
                binding.etEmailReset.requestFocus()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this@ResetPasswordActivity,"Cek Email Untuk Reset Password",Toast.LENGTH_SHORT).show()
                    Intent(this@ResetPasswordActivity,LoginActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }else{
                    Toast.makeText(this@ResetPasswordActivity,"${it.exception?.message}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}