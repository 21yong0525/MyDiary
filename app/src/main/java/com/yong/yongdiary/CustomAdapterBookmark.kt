package com.yong.yongdiary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.yong.yongdiary.databinding.ItemMainBinding

class CustomAdapterBookmark(val bookmarkList: MutableList<DataVOBookmark>) : RecyclerView.Adapter<CustomAdapterBookmark.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val customViewHolder = CustomViewHolder(binding)
        val mainActivity = parent.context as MainActivity

        binding.tvBookmark.setOnClickListener {
            val position: Int = customViewHolder.bindingAdapterPosition
            val dataVOBookmark = bookmarkList.get(position)
            binding.tvBookmark.text = "☆"
            Toast.makeText(parent.context,"메모가 북마크에서 식제되었습니다.",Toast.LENGTH_SHORT).show()
            mainActivity.refreshRecyclerViewDropBookmark(dataVOBookmark,dataVOBookmark.inputTime)
        }

        customViewHolder.itemView.setOnClickListener {
            val position: Int = customViewHolder.bindingAdapterPosition
            val dataVOBookmark = bookmarkList.get(position)
            mainActivity.searchMemo(dataVOBookmark.inputTime)
        }
        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = holder.binding
        val dataVO = bookmarkList.get(position)
        binding.tvDateItem.text = dataVO.date
        binding.tvInputItem.text = dataVO.inPutData
        binding.checkBox1.text = dataVO.check1Name
        binding.checkBox2.text = dataVO.check2Name
        binding.checkBox3.text = dataVO.check3Name
        if (dataVO.check1 == true) {
            binding.tvFtMsg2.visibility = View.VISIBLE
            binding.checkBox1.visibility = View.VISIBLE
        }
        if (dataVO.check2 == true) {
            binding.checkBox2.visibility = View.VISIBLE
        }
        if (dataVO.check3 == true) {
            binding.checkBox3.visibility = View.VISIBLE
        }
        if (dataVO.imgCheck == true) {
            binding.ivUserimgset.setImageBitmap(dataVO.img)
            binding.ivUserimgset.visibility = View.VISIBLE
        }
        binding.tvBookmark.text = "★"
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }

    class CustomViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)
}