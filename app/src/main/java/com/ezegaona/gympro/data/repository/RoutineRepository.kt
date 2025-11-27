package com.ezegaona.gympro.data.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.ezegaona.gympro.data.DatabaseHelper
import com.ezegaona.gympro.ui.workouts.Routine

class RoutineRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    // Obtener todas las rutinas
    fun getAll(): List<Routine> {
        return dbHelper.getRoutines()
    }

    // Filtrar por grupo muscular
    fun getByGroup(group: String): List<Routine> {
        return dbHelper.getRoutines(group)
    }

    // Agregar una nueva rutina
    fun add(r: Routine): Boolean {
        var db: SQLiteDatabase? = null
        return try {
            db = dbHelper.writableDatabase
            val query = """
                INSERT INTO routines (muscle_group, name, sets, reps, rest, image)
                VALUES (?, ?, ?, ?, ?, ?)
            """
            db.execSQL(query, arrayOf(r.muscleGroup, r.name, r.sets, r.reps, r.rest, r.image))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db?.close()
        }
    }

    // Actualizar una rutina existente
    fun update(r: Routine): Boolean {
        var db: SQLiteDatabase? = null
        return try {
            db = dbHelper.writableDatabase
            val query = """
                UPDATE routines 
                SET muscle_group=?, name=?, sets=?, reps=?, rest=?, image=? 
                WHERE id=?
            """
            db.execSQL(query, arrayOf(r.muscleGroup, r.name, r.sets, r.reps, r.rest, r.image, r.id))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db?.close()
        }
    }

    // Eliminar una rutina
    fun delete(id: Int): Boolean {
        var db: SQLiteDatabase? = null
        return try {
            db = dbHelper.writableDatabase
            db.execSQL("DELETE FROM routines WHERE id=?", arrayOf(id))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db?.close()
        }
    }
}
