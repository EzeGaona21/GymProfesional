package com.ezegaona.gympro.ui.workouts

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ezegaona.gympro.R
import com.ezegaona.gympro.data.repository.RoutineRepository

class RoutineFormDialog(
    context: Context,
    private val existing: Routine? = null,
    private val onSave: (Routine) -> Unit
) : Dialog(context) {

    private val repo = RoutineRepository(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_routine_form, null)
        setContentView(view)

        // Obtener vistas
        val etName = view.findViewById<EditText>(R.id.etName)
        val etGroup = view.findViewById<EditText>(R.id.etGroup)
        val etSets = view.findViewById<EditText>(R.id.etSets)
        val etReps = view.findViewById<EditText>(R.id.etReps)
        val etRest = view.findViewById<EditText>(R.id.etRest)
        val etImage = view.findViewById<EditText>(R.id.etImage)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        // Si editamos, rellenar campos
        existing?.let { r ->
            etName.setText(r.name)
            etGroup.setText(r.muscleGroup)
            etSets.setText(r.sets.toString())
            etReps.setText(r.reps.toString())
            etRest.setText(r.rest)
            etImage.setText(r.image)
        }

        btnCancel.setOnClickListener { dismiss() }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val group = etGroup.text.toString().trim().ifEmpty { "General" }
            val setsStr = etSets.text.toString().trim()
            val repsStr = etReps.text.toString().trim()
            val rest = etRest.text.toString().trim().ifEmpty { "60s" }
            val image = etImage.text.toString().trim().ifEmpty { "pesa" }

            if (name.isEmpty() || setsStr.isEmpty() || repsStr.isEmpty()) {
                Toast.makeText(context, "Completa nombre, series y repeticiones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sets = setsStr.toIntOrNull() ?: 0
            val reps = repsStr.toIntOrNull() ?: 0

            if (sets <= 0 || reps <= 0) {
                Toast.makeText(context, "Series y repeticiones deben ser números > 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (existing == null) {
                val newRoutine = Routine(0, group, name, sets, reps, rest, image)
                val added = repo.add(newRoutine)
                if (added) {
                    onSave(newRoutine)
                    dismiss()
                } else {
                    Toast.makeText(context, "Error al crear rutina", Toast.LENGTH_SHORT).show()
                }
            } else {
                val updated = Routine(existing.id, group, name, sets, reps, rest, image)
                val ok = repo.update(updated)
                if (ok) {
                    onSave(updated)
                    dismiss()
                } else {
                    Toast.makeText(context, "Error al actualizar rutina", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
