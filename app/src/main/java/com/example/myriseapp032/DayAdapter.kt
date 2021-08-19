package com.example.myriseapp032

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DayAdapter: RecyclerView.Adapter<Holder>() {
    var DayList = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day_grid,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = DayList.get(position)
        holder.setText(data)
    }

    override fun getItemCount(): Int {
        return DayList.size
    }
}

class Holder(itemView: View) :RecyclerView.ViewHolder(itemView){
    var idTextView = itemView.findViewById<TextView>(R.id.itemTextView)
    fun setText(day : String){
        idTextView.text = "${day}"
    }

    init {
        itemView.setOnClickListener {
            val dayNum = idTextView.text.split(" ")[1].toInt()
            val intent = Intent(itemView.context,StudyActivity::class.java)
            intent.putExtra("day",dayNum)
            itemView.context.startActivity(intent)
        }
    }
}