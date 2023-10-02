package com.example.coba1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class DetailEmployeeActivity : AppCompatActivity() {

    private lateinit var tvEmpId: TextView
    private lateinit var tvEmpName : TextView
    private lateinit var tvEmpAge : TextView
    private lateinit var tvEmpGender : TextView
    private lateinit var tvEmpSalary : TextView
    private lateinit var btnUpdate : Button
    private lateinit var btnDelete : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_employee)
        initView()
        setValuesToViews()
        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empName").toString()
            )
        }
        btnDelete.setOnClickListener {
            openDeleteDialog(
                intent.getStringExtra("empId").toString()
            )
        }
    }

    private fun initView()
    {
        tvEmpId = findViewById(R.id.tvEmpId)
        tvEmpName = findViewById(R.id.tvEmpName)
        tvEmpAge = findViewById(R.id.tvEmpAge)
        tvEmpGender = findViewById(R.id.tvEmpGender)
        tvEmpSalary = findViewById(R.id.tvEmpSalary)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews()
    {
        tvEmpId.text = intent.getStringExtra("empId")
        tvEmpName.text = intent.getStringExtra("empName")
        tvEmpAge.text = intent.getStringExtra("empAge")
        tvEmpGender.text = intent.getStringExtra("empGender")
        tvEmpSalary.text = intent.getStringExtra("empSalary")
    }

    private fun openUpdateDialog(empId:String,empName:String)
    {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog,null)

        mDialog.setView(mDialogView)
        val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpName)
        val etEmpAge = mDialogView.findViewById<EditText>(R.id.etEmpAge)
        val etEmpGender = mDialogView.findViewById<EditText>(R.id.etEmpGender)
        val etEmpSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalary)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etEmpName.setText(intent.getStringExtra("empName"))
        etEmpAge.setText(intent.getStringExtra("empAge"))
        etEmpGender.setText(intent.getStringExtra("empGender"))
        etEmpSalary.setText(intent.getStringExtra("empSalary"))
        mDialog.setTitle("Update $empName")
        val alertDialog = mDialog.create()
        alertDialog.show()
        btnUpdateData.setOnClickListener {
            updateEmployeeData(
                empId,
                etEmpName.text.toString(),
                etEmpAge.text.toString(),
                etEmpGender.text.toString(),
                etEmpSalary.text.toString()
                )
            Toast.makeText(applicationContext,"Employee Update",Toast.LENGTH_LONG).show()
            tvEmpName.text = etEmpName.text.toString()
            tvEmpAge.text = etEmpAge.text.toString()
            tvEmpGender.text = etEmpGender.text.toString()
            tvEmpSalary.text = etEmpSalary.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmployeeData(
        id:String,
        name:String,
        age:String,
        gender:String,
        salary:String
    )
    {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val empInfo = EmployeeModel(id,name,age,gender,salary)
        dbRef.setValue(empInfo)
    }

    private fun openDeleteDialog(empId:String)
    {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(empId)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this,"Employee data deleted",Toast.LENGTH_LONG).show()
            val intent = Intent(this,FetchActivity::class.java)
            finish()
            startActivity(intent)
        }
            .addOnFailureListener { err ->
                Toast.makeText(this,"Delete 1 item ${err.message}",Toast.LENGTH_LONG).show()
            }
    }

}