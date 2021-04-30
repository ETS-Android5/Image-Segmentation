package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.repository.Operators
import com.university.ip.ui.base.BasePresenter

class EditorPresenter : BasePresenter<EditorContract.View>(), EditorContract.Presenter {

    private val operators = Operators()

    override fun increaseBrightness(bitmap: Bitmap, value: Int) {
        val result = operators.increaseBrightness(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun increaseContrast(bitmap: Bitmap, value: Int) {
        val result = operators.increaseContrast(bitmap, value)
        getView()?.setBitmap(result)
    }


    override fun toGray(bitmap: Bitmap) {
        val result = operators.toGray(bitmap)
        getView()?.setBitmap(result)
    }

   override fun toBinary(bitmap: Bitmap,threshold: Int) {
       val result = operators.toBinary(bitmap,threshold)
       if (result != null) {
           getView()?.setBitmap(result)
       }
   }

    override fun bilateralFilter(bitmap: Bitmap,value:Int) {
        val result = operators.bilateralFilter(bitmap,value)
        if (result != null) {
            getView()?.setBitmap(result)
        }
    }

    override fun toAdaptiveBinary(bitmap: Bitmap,threshold: Int) {
        val result = operators.toAdaptiveBinary(bitmap,threshold)
        if (result != null) {
            getView()?.setBitmap(result)
        }
    }


    override fun blur(bitmap: Bitmap, value: Int) {
        val result = operators.blur(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun medianBlur(bitmap: Bitmap, value: Int) {
        val result = operators.medianBlur(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun highPass(bitmap: Bitmap, value: Int) {
        val result = operators.highPass(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun unsharpMask(bitmap: Bitmap, value: Int) {
        val result = operators.unsharpMask(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun flip(bitmap: Bitmap) {
        val result = operators.flip(bitmap)
        getView()?.setBitmap(result)
    }

    override fun rotate90ClockWise(bitmap: Bitmap) {
        val result = operators.rotate90ClockWise(bitmap)
        getView()?.setBitmap(result)
    }

    override fun rotate90CounterClockWise(bitmap: Bitmap) {
        val result = operators.rotate90CounterClockWise(bitmap)
        getView()?.setBitmap(result)
    }

    override fun sobel(bitmap: Bitmap,value:Int) {
        val result = operators.sobel(bitmap,value)
        getView()?.setBitmap(result)
    }

    override fun zoomIn(bitmap: Bitmap, value: Int){
        val result = operators.zoomIn(bitmap,value)
        getView()?.setBitmap(result)
    }

    override fun modifyRGBContrast(bitmap: Bitmap, red: Double, green: Double, blue: Double) {
        val result = operators.modifyRGBContrast(bitmap,red,green,blue)
        getView()?.setBitmap(result)
    }
    override fun modifyRGBBrightness(bitmap: Bitmap, red: Double, green: Double, blue: Double) {
        val result = operators.modifyRGBBrightness(bitmap,red,green,blue)
        getView()?.setBitmap(result)
    }

}