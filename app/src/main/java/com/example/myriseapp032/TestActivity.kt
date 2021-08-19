package com.example.myriseapp032

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myriseapp032.databinding.ActivityTestBinding
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import java.io.InputStream
import java.util.*
import kotlin.math.roundToInt

class TestActivity : AppCompatActivity() {
    var totalRuntimeScore = 100.0F
    var totalGrade = "A"
    var dayString = "day1"
    var singleTimerThread = SingleTimerThread()
    var wholeTimerThread = WholeTimerThread()
    lateinit var binding : ActivityTestBinding

    //한개의 Day의 모든 단어 넣음( 아마 40개씩 ) (인덱스로 컨트롤할거라 데이터 변경 하지 말것)
    var vocaDataList : MutableList<VocaData> = mutableListOf()
    var answerArray: Array<Int?> = arrayOfNulls(10)


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var day ="day1"
        if(intent.hasExtra("day")){
            day = intent.getStringExtra("day").toString()
            dayString = day
        }

        initVocaData()

        Log.d("VocaQ","onCreate()")
        initBtnClickListener()



        wholeTimerThread.isRunning = true
        wholeTimerThread.start()
        singleTimerThread.isRunning = !singleTimerThread.isRunning
        singleTimerThread.start()
/*

        try{
            wholeTimerThread.join()

        }catch(e:InterruptedException){
            e.printStackTrace()
        }
        showResultDialog()
*/


    }

    private fun initVocaData() {
/*        for( vocaNum in 1..40){
            vocaDataList.add(VocaData("Day1","eng${vocaNum}","mean${vocaNum}"))
        }*/

        readExcelFileFromAssets(1)


        val random = Random()
        var num :Int

        for( i in answerArray.indices ){
            num = random.nextInt(4) + 1    //1~4 중 랜덤으로
            answerArray[i] = num
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        singleTimerThread.isRunning = false
        wholeTimerThread.isRunning = false
    }




    inner class SingleTimerThread: Thread() {
        var usedIndexOfVocaData: MutableList<Int> = mutableListOf()
        var isRunning = false;
        var iterateNum = 1  //문제번호
        var isCorrect = false
        var wrongCount = 0
        var clickedBtnNumber = 0
        var second = 9
        var mSecond = 100
        var targetVoca = VocaData("null","null","null")
        var four_answerMeaningArray = arrayOf("null1","null2","null3","null4")
        override fun run() {
            binding.apply {

                runOnUiThread {
                    sleep(5000)

                }
            }

            initSingleQuestionUI()
            while (isRunning){
                //타이머 ui 업데이트
                runOnUiThread {
                    binding.textTimer.text = "${second}:${mSecond}"
                }


                mSecond--
                sleep(10)

                /////////// 제한시간 종료되거나 버튼을 눌렀을경우/////////////////////
                if( second == 0 && mSecond == 0 || clickedBtnNumber != 0){


                    if(clickedBtnNumber == answerArray[iterateNum-1]){
                        isCorrect = true
                    }else{
                        wrongCount++
                    }

                    totalRuntimeScore = totalRuntimeScore-10.0F+second.toFloat()


                    singleQuestionResult(isCorrect)

                    //Result 화면까지 처리했으니 다시 state 초기화
                    second = 9
                    mSecond = 100
                    iterateNum ++
                    isCorrect = false
                    clickedBtnNumber = 0

                    if(iterateNum == 11 || totalRuntimeScore <= 0.0F){       //10문제 다풀면 쓰레드 탈출
                        isRunning = false
                        wholeTimerThread.isRunning = false
                        showGrade()
                        sleep(2000)
                        continue
                    }
                    Log.d("VocaQ","$iterateNum 번째 문제 종료")
                    initSingleQuestionUI()
                    continue  //다음문제로 쓰레드 다시 돌아
                }
                ////////////////////////////////////////////
                if(mSecond == 0){
                    second--
                    mSecond=100
                }

            }


        }

        private fun showGrade() {
            binding.apply {
                runOnUiThread {
                    if(totalRuntimeScore>90.0F ){
                        gradeTextView.setText("A")
                    }else if(totalRuntimeScore>75.0F){
                        gradeTextView.setText("B")
                    }else if(totalRuntimeScore>55.0F){
                        gradeTextView.setText("C+")
                    }else if(totalRuntimeScore>40.0F){
                        gradeTextView.setText("C")
                    }else if(totalRuntimeScore>30.0F){
                        gradeTextView.setText("D")
                    }else{
                        gradeTextView.setText("F")
                    }
                    gradeTextView.visibility = View.VISIBLE
                }
            }
        }

        private fun initSingleQuestionUI() {
            val answerNum = answerArray[iterateNum-1]
            val answerIndex = answerNum?.minus(1)
            var randomIndex = 0
            var randomIndex2 = 0
            val random = Random()
            randomIndex = random.nextInt(vocaDataList.size)  //0~39
            while(randomIndex in usedIndexOfVocaData){
                randomIndex = random.nextInt(vocaDataList.size)
            }
            usedIndexOfVocaData.add(randomIndex)
            targetVoca = vocaDataList[randomIndex]
            four_answerMeaningArray[answerIndex!!] = targetVoca.meaning
            for( i in 0..3){
                if(i == answerIndex!!)
                    continue
                randomIndex2 = random.nextInt(vocaDataList.size)
                while(randomIndex == randomIndex2)
                    randomIndex2 = random.nextInt(vocaDataList.size)
                four_answerMeaningArray[i] = vocaDataList[randomIndex2].meaning
            }

            binding.apply{
                runOnUiThread{
                    if(totalRuntimeScore<50.0F)
                        manImage.setImageResource(R.drawable.man2)
                    else
                        manImage.setImageResource(R.drawable.man0)
                    questionVocaTextview.text = targetVoca.name
                    btnAnswer1.text = four_answerMeaningArray[0]
                    btnAnswer2.text = four_answerMeaningArray[1]
                    btnAnswer3.text = four_answerMeaningArray[2]
                    btnAnswer4.text = four_answerMeaningArray[3]
                }
            }


        }


        private fun singleQuestionResult(isCorrect: Boolean) {

            var totalScore =(totalRuntimeScore*10).roundToInt()/10F
            runOnUiThread {
                if(isCorrect){          //정답시
                    totalScore += 5.0F
                    binding.questionVocaTextview.text = "정답"
                }else{                  //오답시
                    totalRuntimeScore -= 10.0F
                    totalScore =(totalRuntimeScore*10).roundToInt()/10F
                    if(totalScore < 0.0F)
                        totalScore = 0.0F
                    binding.scoreTextView.text = totalScore.toString()
                    binding.manImage.setImageResource(R.drawable.man1)
                    binding.wrongCountTextView.text = "틀린개수: ${wrongCount}개"
                    binding.questionVocaTextview.text = "오답"
                }
                //항상 바뀌는 ui
            }

            sleep(1500)
        }

    }



    inner class WholeTimerThread : Thread() {
        var isRunning = false
        var second = 0
        var mSecond = 0
        override fun run() {
            while (isRunning) {
                runOnUiThread {
                    binding.textWholeTimer.text = "${second}:${mSecond}"
                }
                mSecond++
                sleep(10)
                if (mSecond == 100) {
                    second++
                    mSecond = 0
                }
                if (second == 200) {
                    isRunning = false
                }


            }
        }
    }



    private fun initBtnClickListener() {

        binding.apply {
            btnAnswer1.setOnClickListener {
                singleTimerThread.clickedBtnNumber = 1
            }
            btnAnswer2.setOnClickListener {
                singleTimerThread.clickedBtnNumber = 2

            }
            btnAnswer3.setOnClickListener {
                singleTimerThread.clickedBtnNumber = 3

            }
            btnAnswer4.setOnClickListener {
                singleTimerThread.clickedBtnNumber = 4

            }
        }
    }


/*
    private fun showResultDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_test_result,null)
        val daytext = dialogView.findViewById<TextView>(R.id.dayText)
        val timetext = dialogView.findViewById<TextView>(R.id.timeText)
        val gradetext = dialogView.findViewById<TextView>(R.id.gradeText)
        val wrongtext = dialogView.findViewById<TextView>(R.id.wrongText)

        daytext.text = dayString
        timetext.text = "${wholeTimerThread.second}:${wholeTimerThread.mSecond}"
        gradetext.text = "${totalGrade}"
        wrongtext.text = "${singleTimerThread.wrongCount}개"


        builder.setView(dialogView)
            .setPositiveButton("확인"){dialogInterface, i->
                val i = Intent(this,MenuActivity::class.java)

                startActivity(i)

            }
            .setNegativeButton("취소"){dialogInterface, i->}
            .show()
    }
*/

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
                                vocaDataList.add(VocaData(data_day, data_name, data_meaning))
                            }
                        }else{}
                        colno++
                    }

                }
                rowno++
            }


        } catch (e: Exception) {
            Toast.makeText(this, "에러 발생", Toast.LENGTH_LONG).show()
        }
    }

}