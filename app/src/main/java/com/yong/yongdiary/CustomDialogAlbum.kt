package com.yong.yongdiary

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.yong.yongdiary.databinding.DialogCustomAlbumBinding

class CustomDialogAlbum(val context: Context) {
    val dialog = Dialog(context)
    val binding = DialogCustomAlbumBinding.inflate(LayoutInflater.from(context))

    fun showDialog() {
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
        )

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }
    }
}