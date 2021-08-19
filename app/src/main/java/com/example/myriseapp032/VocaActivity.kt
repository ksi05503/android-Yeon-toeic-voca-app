package com.example.myriseapp032

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myriseapp032.databinding.ActivityVocaBinding

class VocaActivity : AppCompatActivity() {

    lateinit var binding: ActivityVocaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVocaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerView()
    }

    fun initRecyclerView(){
        var dataList: MutableList<String> = setData()
        var adapter = DayAdapter()
        adapter.DayList = dataList
        binding.dayRecyclerView.adapter = adapter
        binding.dayRecyclerView.layoutManager = GridLayoutManager(this,2)  //여기서  Linear와 Grid를 선택 가능
    }
    fun setData(): MutableList<String>{
        var dataList: MutableList<String> = mutableListOf()
        for(num in 1..30){
            var day = "Day $num"
            dataList.add(day)
        }
        return dataList
    }
}