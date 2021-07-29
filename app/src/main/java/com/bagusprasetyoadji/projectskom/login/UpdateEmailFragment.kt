package com.bagusprasetyoadji.projectskom.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bagusprasetyoadji.projectskom.databinding.FragmentUpdateEmailBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException


class UpdateEmailFragment : Fragment() {

    private lateinit var binding: FragmentUpdateEmailBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateEmailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        (context as AppCompatActivity).supportActionBar!!.title = "Ganti Email Baru"

        val user = auth.currentUser
        binding.layoutPassword.visibility = View.VISIBLE
        binding.layoutEmail.visibility = View.GONE

        binding.btnAuth.setOnClickListener {
            val password = binding.etPassword.text.toString().trim()
            if (password.isEmpty()) {
                binding.etPassword.error = "Password Harus diisi"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            user?.let {
                val userCredential = EmailAuthProvider.getCredential(it.email!!, password)
                it.reauthenticate(userCredential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.layoutPassword.visibility = View.GONE
                        binding.layoutEmail.visibility = View.VISIBLE
                    } else if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        binding.etPassword.error = "Password Salah"
                        binding.etPassword.requestFocus()
                    } else {
                        Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            binding.btnUpdate.setOnClickListener { view ->
                val email = binding.etEmail.text.toString().trim()

                if (email.isEmpty()) {
                    binding.etEmail.error = "Email Harus Diisi"
                    binding.etEmail.requestFocus()
                    return@setOnClickListener
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmail.error = "Email Tidak Valid"
                    binding.etEmail.requestFocus()
                    return@setOnClickListener
                }

                user?.let {
                    user.updateEmail(email).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val actionEmailUpdated =
                                UpdateEmailFragmentDirections.actionEmailUpdated()
                            Navigation.findNavController(view).navigate(actionEmailUpdated)
                        } else {
                            Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}