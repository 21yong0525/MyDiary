package com.yong.yongdiary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.yong.yongdiary.databinding.ItemMain2Binding

class CustomAdapter(val dataList: MutableList<DataVO>) : RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemMain2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        val mainActivity = parent.context as MainActivity
        val customViewHolder = CustomViewHolder(binding)

        binding.tvBookmark.setOnClickListener {
            val position: Int = customViewHolder.bindingAdapterPosition
            val dataVO = dataList.get(position)
            val dataVOBookmark = DataVOBookmark(
                dataVO.inPutData,dataVO.date,
                dataVO.check1,dataVO.check1Name,dataVO.check2,dataVO.check2Name,dataVO.check3,dataVO.check3Name,
                dataVO.img,dataVO.imgCheck,dataList[position].inputTime
            )
            when (binding.tvBookmark.text) {
                "☆" -> {
                    binding.tvBookmark.text = "★"
                    val mainActivity = parent.context as MainActivity
                    mainActivity.refreshRecyclerViewAddBookmark(dataVOBookmark)
                    Toast.makeText(parent.context,"메모가 북마크에 추가되었습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        }

        customViewHolder.itemView.setOnClickListener {
            val position: Int = customViewHolder.bindingAdapterPosition
            val dataVO = dataList.get(position)
            mainActivity.openMemo(dataVO)
        }

        customViewHolder.itemView.setOnLongClickListener {
            val position: Int = customViewHolder.bindingAdapterPosition
            val dataVO = dataList.get(position)
            (parent.context as MainActivity).twoFragment.refreshRecyclerViewDrop(dataVO)
            true
        }
        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = holder.binding
        val dataVO = dataList.get(position)
        binding.tvDateItem.text = dataVO.date
        binding.tvInputItem.text = dataVO.inPutData
        binding.tvBookmark.text = dataVO.bookmark
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class CustomViewHolder(val binding: ItemMain2Binding) : RecyclerView.ViewHolder(binding.root)
}