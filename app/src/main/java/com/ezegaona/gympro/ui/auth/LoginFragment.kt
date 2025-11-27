package com.ezegaona.gympro.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ezegaona.gympro.R
import com.ezegaona.gympro.data.DatabaseHelper
import com.ezegaona.gympro.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Si ya hay un usuario guardado, lo mandamos directo al home
        val sharedPref = requireActivity().getSharedPreferences("GymProPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("user_email", null)
        if (savedEmail != null) {
            findNavController().navigate(R.id.homeFragment)
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Completa email y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = dbHelper.loginUser(email, pass)
            if (success) {
                // Guardamos la sesión local
                sharedPref.edit().putString("user_email", email).apply()

                Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.homeFragment)
            } else {
                Toast.makeText(requireContext(), "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        // Navegar a registro
        binding.btnGoRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
