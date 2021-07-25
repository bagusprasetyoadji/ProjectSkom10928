package com.bagusprasetyoadji.projectskom.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bagusprasetyoadji.projectskom.databinding.FragmentChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class ChangePasswordFragment : Fragment() {

    private lateinit var binding : FragmentChangePasswordBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        (context as AppCompatActivity).supportActionBar!!.title = "Ganti Password Baru"

        val user = auth.currentUser
        binding.layoutPassword.visibility = View.VISIBLE
        binding.layoutEmail.visibility = View.GONE

        binding.btnAuth.setOnClickListener {
            val password = binding.etPassword.text.toString().trim()
            if (password.isEmpty()){
                binding.etPassword.error = "Password Harus diisi"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            user?.let {
                val userCredential = EmailAuthProvider.getCredential(it.email!!, password)
                it.reauthenticate(userCredential).addOnCompleteListener {
                    if (it.isSuccessful){
                        binding.layoutPassword.visibility = View.GONE
                        binding.layoutEmail.visibility = View.VISIBLE
                    }else if (it.exception is FirebaseAuthInvalidCredentialsException){
                        binding.etPassword.error = "Password Salah"
                        binding.etPassword.requestFocus()
                    }else{
                        Toast.makeText(activity,"${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.btnUpdate.setOnClickListener { view ->
                val newPassword = binding.etNewPassword.text.toString().trim()
                val newPasswordConfirm = binding.etNewPasswordConfirm.text.toString().trim()

                if (newPassword.isEmpty() || newPassword.length < 6){
                    binding.etNewPassword.error = "Password Harus Lebih dari 6 Karakter"
                    binding.etNewPassword.requestFocus()
                    return@setOnClickListener
                }
                if (newPassword != newPasswordConfirm){
                    binding.etNewPasswordConfirm.error = "Password Tidak Sama"
                    binding.etNewPasswordConfirm.requestFocus()
                    return@setOnClickListener
                }
                user?.let {
                    user.updatePassword(newPassword).addOnCompleteListener {
                        if (it.isSuccessful){
                            val actionPasswordChange = ChangePasswordFragmentDirections.actionPasswordChange()
                            Navigation.findNavController(view).navigate(actionPasswordChange)
                            Toast.makeText(activity,"Password Berhasil diganti",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(activity,"${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}