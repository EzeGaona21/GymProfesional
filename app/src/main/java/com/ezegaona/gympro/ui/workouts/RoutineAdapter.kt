package com.ezegaona.gympro.ui.workouts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezegaona.gympro.R

class RoutineAdapter(
    private var routines: List<Routine>,
    private val onEditClick: (Routine) -> Unit = {},
    private val onDeleteClick: (Routine) -> Unit = {}
) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.img)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val reps: TextView = itemView.findViewById(R.id.tvReps)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routines[position]

        holder.name.text = routine.name
        holder.reps.text = "${routine.sets} x ${routine.reps} (${routine.rest})"

        // Mostrar imagen
        val context = holder.itemView.context
        val resId = context.resources.getIdentifier(routine.image, "drawable", context.packageName)
        holder.img.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_foreground)

        holder.btnEdit.setOnClickListener { onEditClick(routine) }
        holder.btnDelete.setOnClickListener { onDeleteClick(routine) }
    }

    override fun getItemCount(): Int = routines.size

    // 👉 Esto permite actualizar los datos del RecyclerView
    fun updateData(newList: List<Routine>) {
        routines = newList
        notifyDataSetChanged()
    }
}
