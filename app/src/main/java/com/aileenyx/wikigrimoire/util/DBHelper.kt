package com.aileenyx.wikigrimoire.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Companion.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun toggleDashboardStatus(id: Int, newStatus: Boolean): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.Wiki.COLUMN_NAME_DASHBOARD, newStatus)
        }
        val selection = "ID = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.update(
            Contract.Wiki.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun insertWiki(name: String, url: String, bannerImage: String, dashboardStatus: Boolean, default: Boolean = false): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.Wiki.COLUMN_NAME_NAME, name)
            put(Contract.Wiki.COLUMN_NAME_URL, url)
            put(Contract.Wiki.COLUMN_NAME_BANNER, bannerImage)
            put(Contract.Wiki.COLUMN_NAME_DASHBOARD, dashboardStatus)
            put(Contract.Wiki.COLUMN_NAME_DEFAULT, default)
        }
        return db.insert(Contract.Wiki.TABLE_NAME, null, values)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 7
        const val DATABASE_NAME = "wikigrimoire.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${Contract.Wiki.TABLE_NAME} (" +
                    "ID INTEGER NOT NULL PRIMARY KEY," +
                    "${Contract.Wiki.COLUMN_NAME_NAME} VARCHAR(50)," +
                    "${Contract.Wiki.COLUMN_NAME_URL} VARCHAR(512)," +
                    "${Contract.Wiki.COLUMN_NAME_BANNER} VARCHAR(74)," +
                    "${Contract.Wiki.COLUMN_NAME_DASHBOARD} BOOLEAN," +
                    "${Contract.Wiki.COLUMN_NAME_DEFAULT} BOOLEAN DEFAULT FALSE)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${Contract.Wiki.TABLE_NAME}"

        fun fetchData(context: Context, query: String): List<Map<String, Any>> {
            val dbHelper = DBHelper(context)
            val db = dbHelper.readableDatabase
            val cursor: Cursor = db.rawQuery(query, null)
            val result = mutableListOf<Map<String, Any>>()

            if (cursor.moveToFirst()) {
                do {
                    val row = mutableMapOf<String, Any>()
                    for (i in 0 until cursor.columnCount) {
                        val columnName = cursor.getColumnName(i)
                        row[columnName] = when (cursor.getType(i)) {
                            Cursor.FIELD_TYPE_INTEGER -> cursor.getInt(i)
                            Cursor.FIELD_TYPE_FLOAT -> cursor.getFloat(i)
                            Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                            Cursor.FIELD_TYPE_BLOB -> cursor.getBlob(i)
                            Cursor.FIELD_TYPE_NULL -> null
                            else -> cursor.getString(i)
                        } as Any
                    }
                    result.add(row)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return result
        }
    }
}
