package com.ezegaona.gympro.ui.workouts

data class Routine(
    val id: Int = 0,
    val muscleGroup: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val rest: String,
    val image: String
)
