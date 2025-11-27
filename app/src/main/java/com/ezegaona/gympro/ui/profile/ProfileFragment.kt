package com.ezegaona.gympro.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ezegaona.gympro.R
import com.ezegaona.gympro.data.DatabaseHelper
import com.ezegaona.gympro.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Simulación: mostramos el email del usuario guardado localmente
        val sharedPref = requireActivity().getSharedPreferences("GymProPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", "Usuario no registrado")
        binding.tvEmail.text = userEmail

        binding.btnLogout.setOnClickListener {
            // Borramos la sesión local
            sharedPref.edit().clear().apply()
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
