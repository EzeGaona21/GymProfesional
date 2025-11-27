package com.ezegaona.gympro.ui.workouts

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezegaona.gympro.R
import com.ezegaona.gympro.data.repository.RoutineRepository
import com.ezegaona.gympro.databinding.FragmentWorkoutsBinding

class WorkoutsFragment : Fragment() {

    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: RoutineRepository
    private lateinit var adapter: RoutineAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = RoutineRepository(requireContext())
        adapter = RoutineAdapter(emptyList(), onEditClick = { r -> openEditDialog(r) }, onDeleteClick = { r -> confirmDelete(r) })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // FAB para agregar nueva rutina
        binding.fabAdd.setOnClickListener {
            val dialog = RoutineFormDialog(requireContext(), existing = null) {
                // callback: recargar datos desde BD
                reloadData()
                Toast.makeText(requireContext(), "Rutina creada", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }

        loadInitialData()
    }

    private fun loadInitialData() {
        val list = repository.getAll()
        adapter.updateData(list)
    }

    private fun reloadData() {
        val list = repository.getAll()
        adapter.updateData(list)
    }

    private fun openEditDialog(routine: Routine) {
        val dialog = RoutineFormDialog(requireContext(), existing = routine) {
            reloadData()
            Toast.makeText(requireContext(), "Rutina actualizada", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    private fun confirmDelete(routine: Routine) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar rutina")
            .setMessage("¿Eliminar '${routine.name}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                val ok = repository.delete(routine.id)
                if (ok) {
                    reloadData()
                    Toast.makeText(requireContext(), "Eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}