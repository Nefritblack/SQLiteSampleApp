package com.sample.foo.sqlitesampleapp

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.databinding.DataBindingUtil
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast

import com.sample.foo.sqlitesampleapp.databinding.ActivityEmployeeBinding

import java.text.SimpleDateFormat
import java.util.Calendar

class EmployeeActivity : AppCompatActivity() {

    private var binding: ActivityEmployeeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee)

        binding!!.recycleView.layoutManager = LinearLayoutManager(this)

        val queryCols = arrayOf("_id", SampleDBContract.Employer.COLUMN_NAME)
        val adapterCols = arrayOf(SampleDBContract.Employer.COLUMN_NAME)
        val adapterRowViews = intArrayOf(android.R.id.text1)

        val database = SampleDBSQLiteHelper(this).readableDatabase
        val cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME, // The table to query
                queryCols, null, null, null, null, null// don't sort
        )// The columns to return
        // The columns for the WHERE clause
        // The values for the WHERE clause
        // don't group the rows
        // don't filter by row groups

        val cursorAdapter = SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, cursor, adapterCols, adapterRowViews, 0)
        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.employerSpinner.adapter = cursorAdapter

        binding!!.saveButton.setOnClickListener { saveToDB() }

        binding!!.searchButton.setOnClickListener { readFromDB() }
    }

    private fun saveToDB() {
        val database = SampleDBSQLiteHelper(this).writableDatabase
        val values = ContentValues()
        values.put(SampleDBContract.Employee.COLUMN_FIRSTNAME, binding!!.firstnameEditText.text!!.toString())
        values.put(SampleDBContract.Employee.COLUMN_LASTNAME, binding!!.lastnameEditText.text!!.toString())
        values.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION, binding!!.jobDescEditText.text!!.toString())
        values.put(SampleDBContract.Employee.COLUMN_EMPLOYER_ID,
                (binding!!.employerSpinner.selectedItem as Cursor).getInt(0))

        Log.d("getINT", (binding!!.employerSpinner.selectedItem as Cursor).getInt(0).toString() + "")
        Log.d("getColumnName", (binding!!.employerSpinner.selectedItem as Cursor).getColumnName(0))

        try {
            val calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat("dd/MM/yyyy").parse(
                    binding!!.dobEditText.text!!.toString())
            var date = calendar.timeInMillis
            values.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH, date)

            calendar.time = SimpleDateFormat("dd/MM/yyyy").parse(
                    binding!!.employedEditText.text!!.toString())
            date = calendar.timeInMillis
            values.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE, date)
        } catch (e: Exception) {
            Log.e(TAG, "Error", e)
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show()
            return
        }

        val newRowId = database.insert(SampleDBContract.Employee.TABLE_NAME, null, values)

        Toast.makeText(this, "The new Row Id is $newRowId", Toast.LENGTH_LONG).show()
    }

    private fun readFromDB() {
        val firstname = binding!!.firstnameEditText.text!!.toString()
        val lastname = binding!!.lastnameEditText.text!!.toString()

        val database = SampleDBSQLiteHelper(this).readableDatabase

        val selectionArgs = arrayOf("%$firstname%", "%$lastname%")

        val cursor = database.rawQuery(SampleDBContract.SELECT_EMPLOYEE_WITH_EMPLOYER, selectionArgs)
        binding!!.recycleView.adapter = SampleJoinRecyclerViewCursorAdapter(this, cursor)
    }

    companion object {
        private val TAG = "EmployeeActivity"
    }
}
