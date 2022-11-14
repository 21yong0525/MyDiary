package com.yong.yongdiary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yong.yongdiary.databinding.ItemImgBinding

class CustomAdapterAlbum(val imgDataList: MutableList<DataVOImg>): RecyclerView.Adapter<CustomAdapterAlbum.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val customViewHolder = CustomViewHolder(binding)

        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = holder.binding
        val imgdataVO = imgDataList.get(position)
        binding.userImg.setImageBitmap(imgdataVO.image)
    }

    override fun getItemCount(): Int {
        return imgDataList.size
    }

    class CustomViewHolder(val binding : ItemImgBinding): RecyclerView.ViewHolder(binding.root)
}