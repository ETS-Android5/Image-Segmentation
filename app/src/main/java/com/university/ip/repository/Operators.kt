package com.university.ip.repository

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.core.Core.BORDER_DEFAULT
import org.opencv.imgproc.Imgproc


class Operators {
    fun increaseBrightness(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, 1.0, value.toDouble())
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun increaseContrast(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, value.toDouble(), 1.0)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun toGray(bitmap: Bitmap, value: Int): Bitmap {

        val src = Mat(bitmap.width, bitmap.height, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_GRAY2RGB, 4)
        Utils.matToBitmap(src, bitmap)
        return bitmap
    }

    fun toBinary(bmpOriginal: Bitmap, threshold: Int): Bitmap? {
        /*val height: Int = bmpOriginal.height
        val width: Int = bmpOriginal.width
        val threshold: Int = 127
        val bmpBinary = Bitmap.createBitmap(bmpOriginal)
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get one pixel color
                val pixel = bmpOriginal.getPixel(x, y)
                val red: Int = Color.red(pixel)

                //get binary value
                if (red < threshold) {
                    bmpBinary.setPixel(x, y, -0x1000000)
                } else {
                    bmpBinary.setPixel(x, y, -0x1)
                }
            }
        }
        return bmpBinary*/

        val imageMat = Mat()
        Utils.bitmapToMat(bmpOriginal, imageMat)
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY)
        Imgproc.threshold(imageMat, imageMat, threshold.toDouble(), 255.0, Imgproc.THRESH_BINARY)
        Utils.matToBitmap(imageMat, bmpOriginal)
        return bmpOriginal;
    }

    fun blur(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst = Mat(src.cols(), src.rows(), src.type())
        val size = Size(value.toDouble(), value.toDouble())
        val point = Point(value.toDouble() / 2, value.toDouble() / 2)
        Imgproc.blur(src, dst, size, point, Core.BORDER_DEFAULT)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, result)
        return result
    }

    fun medianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst = Mat(src.cols(), src.rows(), src.type())
        if (value % 2 ==0){
            Imgproc.medianBlur(src, dst, value + 1)
        }else{
            Imgproc.medianBlur(src, dst, value)
        }
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, result)
        return result
    }

    fun Convolution2d(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst = Mat(src.cols(), src.rows(), src.type())
        val M  = Mat.eye(3, 3, CvType.CV_32FC1)
        val anchor = Point((-1).toDouble(), (-1).toDouble())
        Imgproc.filter2D(src, dst, CvType.CV_8UC1,M ,anchor, 0.0, Core.BORDER_DEFAULT)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, result)
        return result
    }


    fun decreaseBrightness(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, 1.0, -value.toDouble())
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun decreaseContrast(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, -value.toDouble(), 1.0)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }
}