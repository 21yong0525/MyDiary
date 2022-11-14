package com.yong.yongdiary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.yong.yongdiary.databinding.FragmentOneBinding
import java.text.SimpleDateFormat

class OneFragment : Fragment() {
    lateinit var binding: FragmentOneBinding
    lateinit var mainActivity: MainActivity

    val check = mutableListOf(false, false, false)
    var checkBoxCount = 0
    var clicked = false
    var rotateOpen: Animation? = null
    var rotateClose: Animation? = null
    var fromBottom: Animation? = null
    var toBottom: Animation? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        Log.e("yongdiary","onPause")
        if (clicked){
            onAddButtonClicked()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneBinding.inflate(inflater, container, false)

        val currentTime: Long = System.currentTimeMillis()
        val dataFormat = SimpleDateFormat("yy.MM.dd")
        val inputTime = SimpleDateFormat("yy.MM.dd.mm.ss").format(currentTime)
        binding.tvDate.text = binding.tvDate.text.toString().replace("현재날짜", "${dataFormat.format(currentTime)}")

        rotateOpen = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim);

        binding.btnFloating.setOnClickListener {
            onAddButtonClicked()
        }

        binding.btnFloatingSave.setOnClickListener {
            if (clicked) {
                if (binding.tvInput.text.toString().isNotEmpty()) {
                    Toast.makeText(context, "해당 메모를 저장합니다.", Toast.LENGTH_SHORT).show()
                    val dataVO = DataVO(
                        binding.tvInput.text.toString(), binding.tvDate.text.toString(),
                        check[0], binding.checkBox1.text.toString(),
                        check[1], binding.checkBox2.text.toString(),
                        check[2], binding.checkBox3.text.toString(),
                        binding.ivUserimg.drawable.toBitmap(), mainActivity.imgcheck,
                        inputTime,"☆"
                    )
                    mainActivity.twoFragment.refreshRecyclerViewAdd(dataVO)
                    mainActivity.binding.viewpager.currentItem = 1
                    reset()
                }
            }
        }

        binding.btnFloatingReset.setOnClickListener {
            if (clicked) reset()
        }

        binding.btnFloatingCamera.setOnClickListener {
            if (clicked) {
                mainActivity.pictureType = 1
                mainActivity.bringingPictures(1)
            }
        }

        binding.btnFloatingGallery.setOnClickListener {
            if (clicked) {
                mainActivity.pictureType = 2
                mainActivity.bringingPictures(2)
            }
        }

        binding.btnFloatingCheck.setOnClickListener {
            if (clicked) {
                if (checkBoxCount < 3) {
                    val dialogCheckBox = CustomDialogCheckBox(binding.root.context)
                    dialogCheckBox.showDialog(checkBoxCount)
                    check[checkBoxCount] = true
                    checkBoxCount++
                } else {
                    Toast.makeText(context, "체크박스는 3개 초과할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvCheck1Delete.setOnClickListener {
            when (checkBoxCount) {
                1 -> {
                    binding.checkBox1.visibility = View.GONE
                    binding.tvCheck1Delete.visibility = View.GONE
                    binding.tvFtMsg.visibility = View.GONE
                    checkBoxCount--
                    check[0] = false
                }
                2 -> {
                    binding.checkBox1.text = binding.checkBox2.text
                    binding.checkBox2.visibility = View.GONE
                    binding.tvCheck2Delete.visibility = View.GONE
                    checkBoxCount--
                    check[1] = false
                }
                3 -> {
                    binding.checkBox1.text = binding.checkBox2.text
                    binding.checkBox2.text = binding.checkBox3.text
                    binding.checkBox3.visibility = View.GONE
                    binding.tvCheck3Delete.visibility = View.GONE
                    checkBoxCount--
                    check[2] = false
                }
            }
            if (checkBoxCount == 0) {
                binding.tvFtMsg.visibility = View.GONE
            }
        }

        binding.tvCheck2Delete.setOnClickListener {
            when (checkBoxCount) {
                2 -> {
                    binding.checkBox2.visibility = View.GONE
                    binding.tvCheck2Delete.visibility = View.GONE
                    checkBoxCount--
                    check[1] = false
                }
                3 -> {
                    binding.checkBox2.text = binding.checkBox3.text
                    binding.checkBox3.visibility = View.GONE
                    binding.tvCheck3Delete.visibility = View.GONE
                    checkBoxCount--
                    check[2] = false
                }
            }
            if (checkBoxCount == 0) {
                binding.tvFtMsg.visibility = View.GONE
            }
        }

        binding.tvCheck3Delete.setOnClickListener {
            binding.checkBox3.visibility = View.GONE
            binding.tvCheck3Delete.visibility = View.GONE
            checkBoxCount--
            if (checkBoxCount == 0) {
                binding.tvFtMsg.visibility = View.GONE
                check[2] = false
            }
        }
        return binding.root
    }

    fun reset() {
        for (i in 0..2) {
            check[i] = false
        }
        binding.ivUserimg.setImageResource(R.drawable.defimg)
        binding.checkBox1.visibility = View.GONE
        binding.tvCheck1Delete.visibility = View.GONE
        binding.checkBox2.visibility = View.GONE
        binding.tvCheck2Delete.visibility = View.GONE
        binding.checkBox3.visibility = View.GONE
        binding.tvCheck3Delete.visibility = View.GONE
        binding.tvFtMsg.visibility = View.GONE
        checkBoxCount = 0
        binding.tvInput.text = ""
        mainActivity.imgcheck = false
        if (clicked){
            onAddButtonClicked()
        }
    }

    fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.btnFloatingCheck.visibility = View.VISIBLE
            binding.btnFloatingCamera.visibility = View.VISIBLE
            binding.btnFloatingSave.visibility = View.VISIBLE
            binding.btnFloatingGallery.visibility = View.VISIBLE
            binding.btnFloatingReset.visibility = View.VISIBLE
        } else {
            binding.btnFloatingCheck.visibility = View.GONE
            binding.btnFloatingCamera.visibility = View.GONE
            binding.btnFloatingSave.visibility = View.GONE
            binding.btnFloatingGallery.visibility = View.GONE
            binding.btnFloatingReset.visibility = View.GONE
        }
    }

    fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.btnFloatingCheck.startAnimation(fromBottom)
            binding.btnFloatingCamera.startAnimation(fromBottom)
            binding.btnFloatingSave.startAnimation(fromBottom)
            binding.btnFloatingReset.startAnimation(fromBottom)
            binding.btnFloatingGallery.startAnimation(fromBottom)
            binding.btnFloating.startAnimation(rotateOpen)
        } else {
            binding.btnFloatingCheck.startAnimation(toBottom)
            binding.btnFloatingCamera.startAnimation(toBottom)
            binding.btnFloatingSave.startAnimation(toBottom)
            binding.btnFloatingReset.startAnimation(toBottom)
            binding.btnFloatingGallery.startAnimation(toBottom)
            binding.btnFloating.startAnimation(rotateClose)
        }
    }
}