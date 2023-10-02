package com.example.coba1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertActivity : AppCompatActivity() {
    private lateinit var etEmpName : EditText
    private lateinit var etEmpAge : EditText
    private lateinit var etEmpGender : EditText
    private lateinit var etEmpSalary : EditText
    private lateinit var btnSave : Button
    private lateinit var btnBack : Button
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        etEmpName = findViewById(R.id.etEmpName)
        etEmpAge = findViewById(R.id.etEmpAge)
        etEmpGender = findViewById(R.id.etEmpGender)
        etEmpSalary = findViewById(R.id.etEmpSalary)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        btnSave.setOnClickListener(){
            saveEmployee();
        }
        btnBack.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun saveEmployee()
    {
        val empName = etEmpName.text.toString()
        val empAge = etEmpAge.text.toString()
        val empGender = etEmpGender.text.toString()
        val empSalary = etEmpSalary.text.toString()

        if (empName.isEmpty()){
            etEmpName.error = "Name is required"
        }
        if(empAge.isEmpty()){
            etEmpAge.error = "Age is required"
        }
        if(empGender.isEmpty()){
            etEmpGender.error = "Gender is required"

        }
        if(empSalary.isEmpty()){
            etEmpSalary.error = "Salary is required"

        }
        val empId = dbRef.push().key!!
        val employee = EmployeeModel(empId,empName,empAge,empGender,empSalary)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener{
                Toast.makeText(this,"Complete Add",Toast.LENGTH_LONG).show()
                    etEmpName.text.clear()
                    etEmpAge.text.clear()
                    etEmpGender.text.clear()
                    etEmpSalary.text.clear()
            }
            .addOnFailureListener{err->
                Toast.makeText(this,"Error , ${err.message}",Toast.LENGTH_LONG).show()
            }
    }
}