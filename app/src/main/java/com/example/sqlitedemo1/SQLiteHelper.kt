package com.example.sqlitedemo1

import android.content.ClipData.Item
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context:Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME="wishlist.db"
        private const val TBL_ITEM = "tbl_item"
        private const val ID= "id"
        private const val NAME="name"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblItems = ("CREATE TABLE " + TBL_ITEM + "("
                + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT"
                + ")")
        db?.execSQL(createTblItems)
    }

    override fun onUpgrade(db: SQLiteDatabase?,oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_ITEM")
        onCreate(db)
    }


    fun insertItem(std: ItemModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,std.id)
        contentValues.put(NAME,std.name)

        val success = db.insert(TBL_ITEM,null,contentValues)
        db.close()
        return success
    }

    fun getAllItems(): ArrayList<ItemModel>{
        val stdList: ArrayList<ItemModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_ITEM"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch(e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id:Int
        var name:String




        if (cursor.moveToFirst()) {
            do {
                id=cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                name=cursor.getString(cursor.getColumnIndexOrThrow("name"))

                val std = ItemModel(id= id, name = name)
                stdList .add(std)
            }while(cursor.moveToNext())
        }

        return stdList
    }

    fun updateItem(std: ItemModel): Int {
        val db=this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,std.id)
        contentValues.put(NAME,std.name)

        val success = db.update(TBL_ITEM,contentValues,"id=" + std.id, null)
        db.close()
        return success

    }

    fun deleteItemById(id:Int) : Int{
        val db= this.writableDatabase

        val contentValues=ContentValues()
        contentValues.put(ID,id)

        val success = db.delete(TBL_ITEM,"id=$id",null)
        db.close()
        return success
    }

}