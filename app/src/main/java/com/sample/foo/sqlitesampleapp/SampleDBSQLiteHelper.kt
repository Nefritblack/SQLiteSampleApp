package com.sample.foo.sqlitesampleapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by obaro on 26/09/2016.
 */

class SampleDBSQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(SampleDBContract.Employer.CREATE_TABLE)
        sqLiteDatabase.execSQL(SampleDBContract.Employee.CREATE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SampleDBContract.Employer.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SampleDBContract.Employee.TABLE_NAME)
        onCreate(sqLiteDatabase)
    }

    companion object {

        private val DATABASE_VERSION = 1
        val DATABASE_NAME = "sample_database"
    }
}
