package com.yong.yongdiary

import android.graphics.Bitmap

data class DataVOBookmark(val inPutData: String, val date: String,
                          val check1: Boolean, val check1Name: String,
                          val check2: Boolean, val check2Name: String,
                          val check3: Boolean, val check3Name: String,
                          val img: Bitmap, val imgCheck: Boolean, val inputTime: String)
                          