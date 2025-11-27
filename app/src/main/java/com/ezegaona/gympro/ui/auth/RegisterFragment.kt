package com.ezegaona.gympro.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ezegaona.gympro.R
import com.ezegaona.gympro.data.DatabaseHelper
import com.ezegaona.gympro.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = dbHelper.registerUser(name, email, pass)
            if (success) {
                Toast.makeText(requireContext(), "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.homeFragment)
            } else {
                Toast.makeText(requireContext(), "El correo ya está registrado", Toast.LENGTH_SHORT).show()
            }
        }

        // Navegar al login si ya tiene cuenta
        binding.btnGoLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
