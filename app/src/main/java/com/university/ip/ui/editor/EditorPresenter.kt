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

    override fun decreaseBrightness(bitmap: Bitmap, value: Int) {
        val result = operators.decreaseBrightness(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun decreaseContrast(bitmap: Bitmap, value: Int) {
        val result = operators.decreaseContrast(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun toGray(bitmap: Bitmap, value: Int) {
        val result = operators.toGray(bitmap, value)
        getView()?.setBitmap(result)
    }

   override fun toBinary(bitmap: Bitmap,threshold: Int) {
       val result = operators.toBinary(bitmap,threshold)
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

    override fun Convolution2d(bitmap: Bitmap, value: Int) {
        val result = operators.Convolution2d(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun unsharpMask(bitmap: Bitmap, value: Int) {
        val result = operators.unsharpMask(bitmap, value)
        getView()?.setBitmap(result)
    }

}