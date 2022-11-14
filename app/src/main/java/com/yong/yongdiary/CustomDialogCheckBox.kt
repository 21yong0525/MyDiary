package com.yong.yongdiary

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.yong.yongdiary.databinding.DialogCustomCheckboxBinding

class CustomDialogCheckBox(val context: Context) {
    val dialog = Dialog(context)

    fun showDialog(checkBoxCount: Int) {
        val binding = DialogCustomCheckboxBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        binding.btnCancle.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnOk.setOnClickListener {
            when (checkBoxCount) {
                0 -> {
                    (context as MainActivity).oneFragment.binding.checkBox1.text =
                        binding.edtName.text.toString()
                    context.oneFragment.binding.checkBox1.visibility = View.VISIBLE
                    context.oneFragment.binding.tvCheck1Delete.visibility = View.VISIBLE
                    context.oneFragment.binding.tvFtMsg.visibility = View.VISIBLE
                }
                1 -> {
                    (context as MainActivity).oneFragment.binding.checkBox2.text =
                        binding.edtName.text.toString()
                    context.oneFragment.binding.checkBox2.visibility = View.VISIBLE
                    context.oneFragment.binding.tvCheck2Delete.visibility = View.VISIBLE
                }
                2 -> {
                    (context as MainActivity).oneFragment.binding.checkBox3.text =
                        binding.edtName.text.toString()
                    context.oneFragment.binding.checkBox3.visibility = View.VISIBLE
                    context.oneFragment.binding.tvCheck3Delete.visibility = View.VISIBLE
                }
            }
            dialog.dismiss()
        }
    }
}