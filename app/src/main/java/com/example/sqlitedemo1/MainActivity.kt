package com.example.sqlitedemo1

import android.content.ClipData.Item
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button


    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: ItemAdapter? = null
    private var std:ItemModel? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener{ addItem() }
        btnView.setOnClickListener { getItems() }
        btnUpdate.setOnClickListener{ updateItems()}
        //ok now we need to delete the records


        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            //now to update record
            edName.setText(it.name)
            std = it
        }

        adapter?.setOnClickDeleteItem {
            deleteItem(it.id)
        }
    }



    private fun getItems() {
        val stdList = sqliteHelper.getAllItems()
        Log.e("ppp", "${stdList.size}")

        //now to display data in recycler view
        adapter?.addItems(stdList)
    }

    private fun addItem() {
        val name= edName.text.toString()

        if(name.isEmpty()) {
            Toast.makeText(this,"Please enter required field ", Toast.LENGTH_SHORT).show()
        } else {
            val std = ItemModel(name = name)
            val status = sqliteHelper.insertItem(std)
            // check insert success or no success
            if (status > -2) {
                Toast.makeText(this, "Item added.",Toast.LENGTH_SHORT).show()
                clearEditText()
                getItems()
            }else {
                Toast.makeText(this,"Record not saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateItems() {
        val name= edName.text.toString()

        //check record not changed
        if(name == std?.name ) {
            Toast.makeText(this,"Record not changed." , Toast.LENGTH_SHORT).show()
            return
        }

        if(std == null) return

        val std= ItemModel(id=std!!.id,name = name)
        val status = sqliteHelper.updateItem(std)
        if (status > -1){
            clearEditText()
            getItems()
        }else{
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }




    }

    private fun deleteItem(id:Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog, _ ->
            sqliteHelper.deleteItemById(id)
            getItems()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        edName.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager=LinearLayoutManager(this)
        adapter = ItemAdapter()
        recyclerView.adapter = adapter

    }

    private fun initView() {
        edName = findViewById(R.id.edName)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate=findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }


}