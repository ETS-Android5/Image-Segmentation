package com.university.ip.repository

import android.R.attr.src
import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.core.Core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY


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

    fun toGray(bitmap: Bitmap): Bitmap {

        val src = Mat(bitmap.width, bitmap.height, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, result)
        return result
    }

    fun toBinary(bmpOriginal: Bitmap, threshold: Int): Bitmap? {
        val imageMat = Mat(bmpOriginal.height, bmpOriginal.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bmpOriginal, imageMat)
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY)
        Imgproc.threshold(imageMat, imageMat, threshold.toDouble(), 255.0, Imgproc.THRESH_BINARY)
        val result = Bitmap.createBitmap(imageMat.cols(), imageMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, result)
        return result
    }

    fun toAdaptiveBinary(bmpOriginal: Bitmap, threshold: Int): Bitmap? {
        val imageMat = Mat(bmpOriginal.height, bmpOriginal.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bmpOriginal, imageMat)
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY)
        Imgproc.adaptiveThreshold(imageMat, imageMat, threshold.toDouble(), Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 3, 2.0);
        val result = Bitmap.createBitmap(imageMat.cols(), imageMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, result)
        return result
    }

    fun bilateralFilter(bmpOriginal: Bitmap, value: Int): Bitmap? {
        val imageMat = Mat(bmpOriginal.height, bmpOriginal.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bmpOriginal, imageMat)
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGRA2BGR);
        val dst :Mat= imageMat.clone();
        Imgproc.bilateralFilter(imageMat, dst, value, 250.0, 50.0);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2RGBA);
        val result = Bitmap.createBitmap(imageMat.cols(), imageMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, result)
        return result
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

    fun highPass(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val gray = Mat(src.rows(), src.cols(), src.type())
        val dst = Mat(src.rows(), src.cols(), src.type())
        Imgproc.GaussianBlur(src, src, Size(3.0, 3.0), 0.0, 0.0, BORDER_DEFAULT)
        Imgproc.cvtColor(src, gray, COLOR_BGR2GRAY) // Convert the image to grayscale
        if (value % 2 ==0) {
            Imgproc.Laplacian(gray, dst, CvType.CV_8UC1, value+1, 1.0, 0.0, BORDER_DEFAULT);
        } else {
            Imgproc.Laplacian(gray, dst, CvType.CV_8UC1, value, 1.0, 0.0, BORDER_DEFAULT);
        }
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

    fun unsharpMask(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst= Mat(src.rows(), src.cols(), src.type())

        if (value % 2 ==0){
            Imgproc.GaussianBlur(src, dst, Size(value.toDouble() + 1, value.toDouble() + 1), (value.toDouble() + 1 - 1) / 6, 0.0, Core.BORDER_DEFAULT);
        } else{
            Imgproc.GaussianBlur(src, dst, Size(value.toDouble(), value.toDouble()), (value.toDouble() - 1) / 6, (value.toDouble() - 1) / 6, Core.BORDER_DEFAULT);
        }

        Core.addWeighted(src, 1.5, dst, -0.5, 0.0, dst)
        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result

    }

    fun modifyRGBContrast(bitmap: Bitmap, red: Double, green: Double, blue: Double): Bitmap {

        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst: ArrayList<Mat> = ArrayList(3)
        Core.split(src, dst)

        dst[0].convertTo(dst[0], -1, red.toDouble(), 0.0)
        dst[1].convertTo(dst[1], -1, green.toDouble(), 0.0)
        dst[2].convertTo(dst[2], -1, blue.toDouble(), 0.0)
        Core.merge(dst, src)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun modifyRGBBrightness(bitmap: Bitmap, red: Double, green: Double, blue: Double): Bitmap {

        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst: ArrayList<Mat> = ArrayList(3)
        Core.split(src, dst)

        dst[0].convertTo(dst[0], -1, 0.0, red.toDouble())
        dst[1].convertTo(dst[1], -1, 0.0, green.toDouble())
        dst[2].convertTo(dst[2], -1, 0.0, blue.toDouble())
        Core.merge(dst, src)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun sobel(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst= Mat(src.rows(), src.cols(), src.type())
        Imgproc.Sobel(src, dst, CvType.CV_8UC1, 0, 1);
        /*if (value % 2 ==0){
            Imgproc.GaussianBlur(src,dst, Size(value.toDouble()+1, value.toDouble()+1), (value.toDouble()+1-1)/6, 0.0, Core.BORDER_DEFAULT);
        } else{
            Imgproc.GaussianBlur(src,dst, Size(value.toDouble(), value.toDouble()), (value.toDouble()-1)/6,(value.toDouble()-1)/6 , Core.BORDER_DEFAULT);
        }*/
        Core.addWeighted(src, 1.5, dst, -0.5, 0.0, dst)
        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result

    }

    fun flip(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst= Mat(src.cols(), src.rows(), src.type())
        Core.flip(src, dst, -1);
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result
    }

    fun rotate90ClockWise(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst= Mat(src.cols(), src.rows(), src.type())
        Core.transpose(src, dst)
        Core.flip(dst, dst, ROTATE_90_CLOCKWISE);
        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result
    }
    fun rotate90CounterClockWise(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst= Mat(src.cols(), src.rows(), src.type())
        Core.transpose(src, dst)
        Core.flip(dst, dst, ROTATE_90_COUNTERCLOCKWISE);
        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result
    }
}