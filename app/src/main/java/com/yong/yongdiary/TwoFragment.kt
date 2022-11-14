package com.yong.yongdiary

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yong.yongdiary.databinding.FragmentTwoBinding

class TwoFragment : Fragment() {
    lateinit var binding: FragmentTwoBinding
    lateinit var mainActivity: MainActivity
    var dataList = mutableListOf<DataVO>()
    var customAdapter = CustomAdapter(dataList)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTwoBinding.inflate(inflater, container, false)

        val linearLayoutManager = LinearLayoutManager(container?.context)
        customAdapter = CustomAdapter(dataList)
        binding.recyclerView.adapter = customAdapter
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.addItemDecoration(MyDecoration(binding.root.context))

        for (i in 1..30){
            val dataVO = DataVO(
                "메모${i}", "22.11.13",
                false, "d",
                false, "d",
                false, "d",
                mainActivity.binding.userImg.drawable.toBitmap(), false,
                "d","☆"
            )
            dataList.add(dataVO)
        }
        return binding.root
    }

    fun refreshRecyclerViewAdd(dataVO: DataVO) {
        dataList.add(dataVO)
        customAdapter.notifyDataSetChanged()
    }

    fun refreshRecyclerViewDrop(dataVO: DataVO) {
        Toast.makeText(binding.root.context,"해당 메모를 삭제합니다", Toast.LENGTH_SHORT).show()
        dataList.remove(dataVO)
        customAdapter.notifyDataSetChanged()
    }
}