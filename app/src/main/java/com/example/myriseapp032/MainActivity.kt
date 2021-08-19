package com.example.myriseapp032

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myriseapp032.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }

    fun init(){
        binding.apply {
            StudyVocaBtn.setOnClickListener {
                val i = Intent(this@MainActivity, VocaActivity::class.java)
                startActivity(i)
            }
            MyvocaBtn.setOnClickListener {
                val i = Intent(this@MainActivity, RemindActivity::class.java)
                startActivity(i)
            }
            TestBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this@MainActivity)
                val dialogView = layoutInflater.inflate(R.layout.dialog_select_day,null)
                val daySpinner = dialogView.findViewById<Spinner>(R.id.spinner_day)

                val day:String = daySpinner.selectedItem.toString()

                builder.setView(dialogView)
                    .setPositiveButton("시작"){dialogInterface, i->
                        val i = Intent(this@MainActivity,TestActivity::class.java)
                        i.putExtra("day",day)
                        Log.d("VocaQ","extra: $day")
                        startActivity(i)

                    }
                    .setNegativeButton("취소"){dialogInterface, i->}
                    .show()

            }

        }
    }
}