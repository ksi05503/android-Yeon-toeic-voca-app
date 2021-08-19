package com.example.myriseapp032

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myriseapp032.databinding.ActivityRemindBinding

class RemindActivity : AppCompatActivity() {


    lateinit var binding : ActivityRemindBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRemindBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()


    }

    private fun initRecycler() {
        var  items = setItems()

        var adapter = VocaAdapter()
        adapter.setMutable(1)    // 변경가능한 리스트임을 알려준다
        adapter.VocaList = items
        binding.VocaRecyclerView.adapter = adapter
        binding.VocaRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.addBtn.setOnClickListener {
            adapter.VocaList.add(VocaData("day1",binding.newName.text.toString(),binding.newMeaning.text.toString()))
            adapter.notifyDataSetChanged()
        }
    }

    private fun setItems() : MutableList<VocaData> {
        var items:MutableList<VocaData> = mutableListOf()
        items.add(VocaData("Day 1","resume","이력서"))
        items.add(VocaData("Day 1","proficiency","능숙"))
        items.add(VocaData("Day 1","payroll","금료명부"))
        items.add(VocaData("Day 1","apple","사과"))
        items.add(VocaData("Day 1","banana","바나나"))
        items.add(VocaData("Day 1","orange","오렌지"))
        items.add(VocaData("Day 1","melon","멜론/한국의 음악재생플레이어"))
        items.add(VocaData("Day 1","watermelon","수박"))
        items.add(VocaData("Day 1","peach","복숭아"))

        return items

    }

}
