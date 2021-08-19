package com.example.myriseapp032

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myriseapp032.databinding.ActivityStudyBinding
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import java.io.InputStream

class StudyActivity : AppCompatActivity() {

    var dayString = "day0"
    var items: MutableList<VocaData> = mutableListOf()  //데이터가 담길 리스트
    var meanShowing = 1

    lateinit var binding : ActivityStudyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStudyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setEventHandler()

        var day = 0
        if(intent.hasExtra("day")){
            day = intent.getIntExtra("day",0)
            dayString = "day$day"
        }

        if(day == 0){
            Toast.makeText(this,"표시할 데이터가 없습니다.",Toast.LENGTH_SHORT)
        }else{
            readExcelFileFromAssets(day)
        }

        var adapter = VocaAdapter()
        adapter.VocaList = items
        binding.VocaRecyclerView.adapter = adapter
        binding.VocaRecyclerView.layoutManager = LinearLayoutManager(this)


    }



    private fun setEventHandler() {
        binding.apply {
            meanVisBtn.setOnClickListener {
                if(meanShowing == 1){
                    meanShowing =0
                    //여따가 잘 구현해 보소 뜻가리기랑 보이기
                }
            }
        }
    }


    private fun readExcelFileFromAssets(day:Int) {
        try {
            val myInput: InputStream
            // assetManager 초기 설정
            val assetManager = assets
            //  엑셀 시트 열기
            myInput = when(day)
            {
                1,2,3,4,5-> assetManager.open("voca1.xls")
                6,7,8,9,10-> assetManager.open("voca2.xls")
                11,12,13,14,15-> assetManager.open("voca3.xls")
                16,17,18,19,20-> assetManager.open("voca4.xls")
                21,22,23,24,25-> assetManager.open("voca5.xls")
                26,27,28,29,30-> assetManager.open("voca6.xls")
                else-> assetManager.open("voca1.xls")
            }
            // POI File System 객체 만들기
            val myFileSystem = POIFSFileSystem(myInput)
            //워크 북
            val myWorkBook = HSSFWorkbook(myFileSystem)
            // 워크북에서 시트 가져오기
            val sheet = myWorkBook.getSheetAt(0)


            val rowIter = sheet.rowIterator() //행을 반복해줄 변수
            var rowno = 0 //행 넘버 변수


            while(rowIter.hasNext()){
                val myRow = rowIter.next() as HSSFRow
                if(rowno != 0) {
                    //열반복 변수
                    val cellIter = myRow.cellIterator()
                    //열 넘버 변수
                    var colno = 0
                    //내용 담을 변수
                    var data_day = ""
                    var data_name = ""
                    var data_meaning = ""
                    //열 반복문
                    while(cellIter.hasNext()){
                        val myCell = cellIter.next() as HSSFCell
                        if(colno === 0){
                            data_day = myCell.toString()
                        }else if(colno === 1){
                            data_name = myCell.toString()
                        }else if(colno === 2){
                            data_meaning = myCell.toString()
                            if(dayString == data_day){
                                items.add(VocaData(data_day, data_name, data_meaning))
                            }
                        }else{}
                        colno++
                    }

                }
                rowno++
            }
            Log.d("excel", "items: $items")


        } catch (e: Exception) {
            Toast.makeText(this, "에러 발생", Toast.LENGTH_LONG).show()
        }
    }
}