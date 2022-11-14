package com.yong.yongdiary

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yong.yongdiary.databinding.FragmentThreeBinding

class ThreeFragment : Fragment() {
    lateinit var binding: FragmentThreeBinding
    lateinit var mainActivity: MainActivity

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
        binding = FragmentThreeBinding.inflate(inflater, container, false)

        binding.btnUserImg.setOnClickListener {
            mainActivity.pictureType = 3
            mainActivity.bringingPictures(2)
        }

        binding.btnUserSet.setOnClickListener {
            if (binding.edtName.text.isEmpty() || binding.edtEmail.text.isEmpty()){
                Toast.makeText(context,"닉네임과 이메일을 입력하세요",Toast.LENGTH_SHORT).show()
            } else {
                mainActivity.binding.userNickname.text = binding.edtName.text.toString()
                mainActivity.binding.userEmail.text = binding.edtEmail.text.toString()
                Toast.makeText(context,"프로필이 등록 되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUserReset.setOnClickListener {
            mainActivity.binding.userNickname.text = ""
            mainActivity.binding.userEmail.text = ""
            binding.edtName.setText("")
            binding.edtEmail.setText("")
            binding.ft3UserImg.setImageResource(R.drawable.defimg)
            binding.ft3UserImg.visibility = View.GONE
            mainActivity.binding.userImg.setImageResource(R.drawable.defimg)
            mainActivity.binding.userImg.visibility = View.GONE
            Toast.makeText(context,"프로필이 초기화 되었습니다.",Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}