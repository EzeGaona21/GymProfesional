package com.ezegaona.gympro.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.ezegaona.gympro.ui.workouts.Routine
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "gympro.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Tabla de usuarios
        db?.execSQL(
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "email TEXT UNIQUE, " +
                    "password TEXT)"
        )

        // Tabla de rutinas
        db?.execSQL(
            "CREATE TABLE routines (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "muscle_group TEXT, " +
                    "name TEXT, " +
                    "sets INTEGER, " +
                    "reps INTEGER, " +
                    "rest TEXT, " +
                    "image TEXT)"
        )

        // Cargar rutinas iniciales (seed)
        val seed = listOf(
            arrayOf("Pecho", "Press banca", 4, 10, "60s", "pesa"),
            arrayOf("Espalda", "Jalón al pecho", 4, 10, "60s", "pesa"),
            arrayOf("Bíceps", "Curl con mancuernas", 3, 12, "45s", "pesa"),
            arrayOf("Tríceps", "Fondos en banco", 3, 12, "45s", "pesa"),
            arrayOf("Hombro", "Press militar", 4, 10, "60s", "pesa"),
            arrayOf("Piernas", "Sentadilla", 4, 10, "90s", "pesa"),
            arrayOf("Abdomen", "Crunch abdominal", 3, 20, "30s", "pesa"),
            arrayOf("Glúteos", "Hip Thrust", 4, 12, "60s", "pesa"),
            arrayOf("Full Body", "Burpees", 3, 15, "60s", "pesa"),
            arrayOf("Cardio", "Soga", 1, 10, "min", "pesa")
        )

        seed.forEach { r ->
            db?.execSQL(
                "INSERT INTO routines (muscle_group, name, sets, reps, rest, image) VALUES (?,?,?,?,?,?)",
                arrayOf(r[0], r[1], r[2], r[3], r[4], r[5])
            )
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("DROP TABLE IF EXISTS routines")
        onCreate(db)
    }

    // ------------------------
    // Métodos para usuarios
    // ------------------------

    fun registerUser(name: String, email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }
        val result = db.insert("users", null, values)
        db.close()
        return result != -1L
    }

    fun loginUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM users WHERE email=? AND password=?",
            arrayOf(email, password)
        )
        val success = cursor.moveToFirst()
        cursor.close()
        db.close()
        return success
    }

    // ------------------------
    // Métodos para rutinas
    // ------------------------

    fun getRoutines(group: String? = null): List<Routine> {
        val db = readableDatabase
        val list = mutableListOf<Routine>()
        val cursor = if (group.isNullOrEmpty()) {
            db.rawQuery("SELECT * FROM routines ORDER BY muscle_group, name", null)
        } else {
            db.rawQuery("SELECT * FROM routines WHERE muscle_group=? ORDER BY name", arrayOf(group))
        }
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    Routine(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        muscleGroup = it.getString(it.getColumnIndexOrThrow("muscle_group")),
                        name = it.getString(it.getColumnIndexOrThrow("name")),
                        sets = it.getInt(it.getColumnIndexOrThrow("sets")),
                        reps = it.getInt(it.getColumnIndexOrThrow("reps")),
                        rest = it.getString(it.getColumnIndexOrThrow("rest")),
                        image = it.getString(it.getColumnIndexOrThrow("image"))
                    )
                )
            }
        }
        db.close()
        return list
    }
}
