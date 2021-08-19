package com.example.myriseapp032

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class VocaAdapter: RecyclerView.Adapter<Holder2>() {


    var VocaList = mutableListOf<VocaData>()
    var isCheckedList = MutableList(100) { 1 }          //토글문제 해결

    private var isMutable = 0
/*    init{} // 기본생성자
    constructor(isMutable:Int):this(isMutable=1){     //이거 왜 생성자 오버롣딩 안되지
        this.isMutable = isMutable
    }*/

    fun setMutable(m :Int){
        isMutable = m
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voca,parent,false)
        return Holder2(view)
    }

    override fun onBindViewHolder(holder: Holder2, position: Int) {


        val data = VocaList.get(position)
        holder.setData(data)

        if(isCheckedList[position] == 0)
            holder.setBlankMeaning()                //토글문제 해결


        holder.itemView.setOnClickListener {
            if(isCheckedList[position]==1){
                isCheckedList[position]=0
                holder.setBlankMeaning()
            }else{
                isCheckedList[position]=1
                holder.setData(data)
            }
        }

        //DataSetChange를 이렇게 어댑터 내부에서 써도되고, 외부에서 쓰는 방법도있다. (외부에서 어댑터속 리스트를 수정하고, 이거호출해도 가능
        if(isMutable==1){
            holder.itemView.setOnLongClickListener {

                var builder = AlertDialog.Builder(it.context)
                var builder2 = AlertDialog.Builder(it.context).setView(R.layout.dialog_modify_voca)

/*                lateinit var nameInput: EditText
                lateinit var meaningInput: EditText*/


                builder.setTitle("단어를 수정하시겠습니까?")
                    .setNegativeButton("삭제"){dialogInterface, i ->
                        VocaList.removeAt(position)
                        notifyDataSetChanged()
                    }
                    .setPositiveButton("수정"){dialogInterface, i->
                        builder2
                            .setPositiveButton("수정"){dialogInterface, i->
                                //setContentView보다 findViewById 먼저 호출되면 프로그램이 죽는다(여기서는 dialog.setView)
/*                                nameInput = it.findViewById(R.id.nameInput)
                                meaningInput = it.findViewById(R.id.meaningInput)*/

/*                                VocaList[position].name = nameInput.text.toString()
                                VocaList[position].meaning = meaningInput.text.toString()*/

                                VocaList[position].name =  "aaa"
                                VocaList[position].meaning = "bbb"
                                notifyDataSetChanged()
                            }
                            .setNegativeButton("취소"){dialogInterface, i->
                            }
                            .show()
                    }
                    .show()



                true


            }
        }

 /*       holder.itemView.setOnLongClickListener{

                .setN
        }*/
    }


    override fun getItemCount(): Int {
        return VocaList.size
    }

}





class Holder2(itemView: View) :RecyclerView.ViewHolder(itemView){
    var voc_name = itemView.findViewById<TextView>(R.id.voc_name)
    var voc_meaning = itemView.findViewById<TextView>(R.id.voc_meaning)
    fun setData(data : VocaData){
        voc_name.text = data.name
        voc_meaning.text = data.meaning
    }
    fun setBlankMeaning(){                          //토글문제 해결
        voc_meaning.text = ""
    }


}

