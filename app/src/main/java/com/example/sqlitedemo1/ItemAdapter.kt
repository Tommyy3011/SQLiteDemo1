package com.example.sqlitedemo1

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter: RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private var stdList: ArrayList<ItemModel> = ArrayList()
    private var onClickItem: ((ItemModel)->Unit)?=null
    private var onClickDeleteItem: ((ItemModel)->Unit)?=null


    fun addItems(items: ArrayList<ItemModel>) {
        this.stdList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (ItemModel)->Unit){
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback:(ItemModel)->Unit){
        this.onClickDeleteItem = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items,parent,false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val std= stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener { onClickItem?.invoke(std) }
        holder.btnDelete.setOnClickListener{ onClickDeleteItem?.invoke(std)}

    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class ItemViewHolder (var view: View): RecyclerView.ViewHolder(view) {
        private var id= view.findViewById<TextView>(R.id.tvId)
        private var name= view.findViewById<TextView>(R.id.tvName)
         var btnDelete= view.findViewById<TextView>(R.id.btnDelete)



        fun bindView(std:ItemModel){
            id.text =std.id.toString()
            name.text=std.name

        }
    }
}