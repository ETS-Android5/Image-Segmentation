package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.ui.base.BaseContract

interface EditorContract {

    interface View : BaseContract.View {
        //view functions for each change of activity
        fun setBitmap(bitmap: Bitmap)
    }

    interface Presenter {
        //functions that are going to use our library
        fun increaseBrightness(bitmap: Bitmap, value: Int)

        fun increaseContrast(bitmap: Bitmap, value: Int)

        fun decreaseBrightness(bitmap: Bitmap, value: Int)

        fun decreaseContrast(bitmap: Bitmap, value: Int)

        fun toGray(bitmap: Bitmap)

        fun toBinary(bitmap: Bitmap,threshold: Int)

        fun toAdaptiveBinary(bitmap: Bitmap,threshold: Int)

        fun bilateralFilter(bitmap: Bitmap,value: Int)

        fun blur(bitmap: Bitmap, value: Int)

        fun medianBlur(bitmap: Bitmap, value: Int)

        fun highPass(bitmap: Bitmap, value: Int)

        fun unsharpMask(bitmap: Bitmap, value: Int)

        fun flip(bitmap: Bitmap)

        fun rotate90ClockWise(bitmap: Bitmap)

        fun rotate90CounterClockWise(bitmap: Bitmap)

        fun sobel(bitmap: Bitmap,value:Int)

        fun modifyRGBContrast(bitmap: Bitmap, red: Double, green: Double, blue: Double)

        fun modifyRGBBrightness(bitmap: Bitmap, red: Double, green: Double, blue: Double)
    }
}