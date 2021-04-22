package com.university.ip.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.university.ip.R
import com.university.ip.ui.main.MainActivity
import com.university.ip.util.files.FileSaver.Companion.IMAGE_MIME_TYPE
import com.university.ip.util.files.FileSaverLegacy
import kotlinx.android.synthetic.main.activity_editor.*
import org.opencv.android.OpenCVLoader

class EditorActivity : AppCompatActivity(), EditorContract.View, View.OnClickListener,
    FiltersAdapter.ItemClickListener, SeekBar.OnSeekBarChangeListener {

    override fun appContext(): Context = applicationContext
    private val TAG = "EditorActivity"
    var prevBrightnessProgress = 1
    var prevContrastProgress = 1
    var prevBinarizingProgress = 1
    var prevBlurProgress = 1
    var prevMedianBlurProgress = 1
    var prev2dConvProgress = 1
    var prevSharpenProgress = 1
    var prevGrayProgress = 1
    var prevBilateralProgress = 1
    var prevRedCProgress = 1
    var prevGreenCProgress = 1
    var prevBlueCProgress = 1
    var prevRedBProgress = 1
    var prevGreenBProgress = 1
    var prevBlueBProgress = 1
    private lateinit var backButton: ImageView
    private lateinit var saveButton: TextView
    private lateinit var imageView: ImageView
    private lateinit var originalBitmap: Bitmap

    private lateinit var filterList: RecyclerView
    private lateinit var seekBar: SeekBar
    private lateinit var seekBar1: SeekBar
    private lateinit var seekBar2: SeekBar

    private lateinit var adapter: FiltersAdapter

    private lateinit var fileSaver: FileSaverLegacy
    private lateinit var bitmap: Bitmap
    private lateinit var selectedFilter: String
    private lateinit var presenter: EditorPresenter

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        //list initialization
        val layoutManager = LinearLayoutManager(appContext(), LinearLayoutManager.HORIZONTAL, false)
        filterList = findViewById(R.id.filters_list)
        filterList.layoutManager = layoutManager
        adapter = FiltersAdapter(appContext(), this)
        adapter.setMediaList(FILTERS_ARRAY);
        filterList.adapter = adapter

        seekBar = findViewById(R.id.seek_bar_editor)
        seekBar1 = findViewById(R.id.seek_bar_editor1)
        seekBar2 = findViewById(R.id.seek_bar_editor2)

        backButton = findViewById(R.id.back_editor)
        backButton.setOnClickListener(this)

        imageView = findViewById(R.id.image_edited)

        fileSaver = FileSaverLegacy(appContext())
        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener(this)
        presenter = EditorPresenter()
        presenter.bindView(this)
        //image load
        loadImage()
        openCvInit()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        // onTouch listener function when the image is clicked
        imageView.setOnTouchListener { v, m -> // Perform tasks here
            zoom_controls.show()
            false
        }

        btn_flip.setOnClickListener({
            presenter.flip(bitmap)
        })

        rotate_left.setOnClickListener({
            presenter.rotate90ClockWise(bitmap)
        })

        rotate_right.setOnClickListener({
            presenter.rotate90CounterClockWise(bitmap)
        })

        // This function will be automatically called out,when
        // zoom in button is being pressed
        zoom_controls.setOnZoomInClickListener(
                View.OnClickListener {
                    val x: Float = imageView.getScaleX()
                    val y: Float = imageView.getScaleY()

                    // setting the new scale
                    imageView.scaleX = (x + 0.5f) as Float
                    imageView.scaleY = (y + 0.5f) as Float
                    zoom_controls.hide()
                }
        )

        // This function will be called when
        // zoom out button is pressed
        zoom_controls.setOnZoomOutClickListener(
                View.OnClickListener {
                    val x: Float = imageView.getScaleX()
                    val y: Float = imageView.getScaleY()
                    if (x == 1f && y == 1f) {
                        imageView.setScaleX(x as Float)
                        imageView.setScaleY(y as Float)
                        zoom_controls.hide()
                    } else {
                        // setting the new scale
                        imageView.setScaleX((x - 0.5f) as Float)
                        imageView.setScaleY((y - 0.5f) as Float)
                        // hiding the zoom controls
                        zoom_controls.hide()
                    }
                }
        )
    }

    private fun openCvInit() {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV not loaded");
        } else {
            Log.e(TAG, "OpenCV loaded");
        }
    }

    private fun loadImage() {
        val data = intent.getBundleExtra(INTENT_EXTRAS)
        val requestCode = intent.getIntExtra(REQUEST_CODE, 2)
        val resultCode = intent.getIntExtra(RESULT_CODE, Activity.RESULT_CANCELED)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = data.get("data") as Bitmap
                    originalBitmap = selectedImage
                    bitmap = selectedImage
                    imageView.setImageBitmap(selectedImage)
                }
                1 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = intent.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(
                            selectedImage,
                            filePathColumn, null, null, null
                    )
                    if (cursor != null) {
                        cursor.moveToFirst()

                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        originalBitmap = BitmapFactory.decodeFile(picturePath)
                        bitmap = BitmapFactory.decodeFile(picturePath)
                        imageView.setImageBitmap(bitmap)
                        cursor.close()
                    }

                }
            }
        }
    }

    companion object {
        const val INTENT_EXTRAS: String = "INTENT_EXTRAS"
        const val REQUEST_CODE: String = "REQUEST_CODE"
        const val RESULT_CODE: String = "RESULT_CODE"
        val FILTERS_ARRAY: List<String> = listOf("Brightness", "Contrast", "Binarizing","Blur","Median","2D Convolution","Sharpen","Gray","Adaptive Binary","Bilateral","RGB Contrast","RGB Brightness")
        val FILTERS_SLIDER_ARRAY: List<String> = listOf("Brightness", "Contrast","Binarizing","Blur","Median","2D Convolution","Sharpen","Gray","Adaptive Binary","Bilateral","RGB Contrast","RGB Brightness")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_editor -> {
                finish()
            }
            R.id.save_button -> {
                val uri = fileSaver.getFileUri(IMAGE_MIME_TYPE) ?: return
                appContext().contentResolver.openOutputStream(uri)?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }
                startActivity(Intent(appContext(), MainActivity::class.java))
            }
        }
    }

    override fun onItemClick(filter: String) {
        selectedFilter = filter
        if (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter) >= 0) {
            seekBar.visibility = View.VISIBLE
            seekBar.setOnSeekBarChangeListener(this)
            when (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter)) {
                0 -> {
                    seekBar1.visibility = View.GONE
                    seekBar2.visibility = View.GONE
                    seekBar.max=100
                    return
                }
                1 -> {
                    seekBar1.visibility = View.GONE
                    seekBar2.visibility = View.GONE
                    seekBar.max=25
                    return
                }
                2 -> {
                    seekBar1.visibility = View.GONE
                    seekBar2.visibility = View.GONE
                    seekBar.max=255

                    return
                }
                3 -> {
                    seekBar1.visibility = View.GONE
                    seekBar2.visibility = View.GONE
                    seekBar.max=500
                    return
                }
                4 -> {
                    seekBar1.visibility = View.GONE
                    seekBar2.visibility = View.GONE
                    seekBar.max=255
                    return
                }
                5 -> {
                    seekBar1.visibility = View.GONE
                    seekBar2.visibility = View.GONE
                   seekBar.max=31
                   return
                }
                6 -> {
                    seekBar2.visibility = View.GONE
                    seekBar1.visibility = View.GONE
                    seekBar.max=60
                    return
                }
                7 -> {
                    seekBar1.visibility = View.GONE
                    seekBar2.visibility = View.GONE
                    seekBar.max=2
                    return
                }
                8 -> {
                    seekBar1.visibility = View.GONE
                    seekBar2.visibility = View.GONE
                    seekBar.max=255
                    return
                }
                9 -> {
                    seekBar1.visibility = View.GONE
                    seekBar.max=25
                    return
                }
                10 -> {
                    seekBar1.visibility = View.VISIBLE
                    seekBar1.setOnSeekBarChangeListener(this)
                    seekBar2.visibility = View.VISIBLE
                    seekBar2.setOnSeekBarChangeListener(this)
                    seekBar.max=255
                    seekBar1.max=255
                    seekBar2.max=255
                    return
                }
                11 -> {

                    seekBar1.visibility = View.VISIBLE
                    seekBar1.setOnSeekBarChangeListener(this)
                    seekBar2.visibility = View.VISIBLE
                    seekBar2.setOnSeekBarChangeListener(this)
                    seekBar.max=255
                    seekBar1.max=255
                    seekBar2.max=255
                    return
                }

                else -> return
            }
        } else {
            seekBar.visibility = View.GONE
        }
        println(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter))
    }

    override fun onProgressChanged(seekBarr: SeekBar, progress: Int, fromUser: Boolean) {
        println(progress)
        println(selectedFilter)
        when (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter)) {
            0 -> {
                    seekBarr.progress = prevBrightnessProgress
                    bitmap = originalBitmap
                  //  presenter.decreaseBrightness(bitmap, prevBrightnessProgress)
                    presenter.increaseBrightness(bitmap, progress)
                    prevBrightnessProgress = progress
                return
            }
            1 -> {
                    seekBarr.progress = prevContrastProgress
                    bitmap = originalBitmap
                    //presenter.decreaseContrast(bitmap, progress)

                    presenter.modifyRGBContrast(bitmap, 50.0,30.0,0.0)
                    prevContrastProgress = progress
                return
            }
            2 -> {
                seekBarr.progress = prevBinarizingProgress
                if(progress>=20) {
                    bitmap = originalBitmap
                    presenter.toBinary(bitmap, progress)
                } else {
                    bitmap = originalBitmap
                    setBitmap(bitmap)
                }
                prevBinarizingProgress = progress
                return
            }
            3 -> {
                    seekBarr.progress = prevBlurProgress
                    bitmap = originalBitmap
                    presenter.blur(bitmap,progress)
                    prevBlurProgress = progress
                return
            }
            4 -> {
                seekBarr.progress = prevMedianBlurProgress
                bitmap = originalBitmap
                presenter.medianBlur(bitmap,progress)
                prevMedianBlurProgress = progress
                return
            }
            5 -> {
                seekBarr.progress = prev2dConvProgress
                bitmap = originalBitmap
                presenter.highPass(bitmap,progress)
                prev2dConvProgress = progress
                return
            }
            6 -> {
                seekBarr.progress = prevSharpenProgress
                bitmap = originalBitmap
                presenter.unsharpMask(bitmap,progress)
                prevSharpenProgress = progress
                return
            }
            7 -> {
                seekBarr.progress = prevGrayProgress
                bitmap = originalBitmap
                if(progress<2){
                    setBitmap(bitmap)
                } else {
                    presenter.toGray(bitmap)
                }
                prevGrayProgress = progress
                return
            }
            8 -> {
                seekBarr.progress = prevBinarizingProgress
                if(progress>=20) {
                    bitmap = originalBitmap
                    presenter.toAdaptiveBinary(bitmap, progress)
                } else {
                    bitmap = originalBitmap
                    setBitmap(bitmap)
                }
                prevBinarizingProgress = progress
                return
            }
            9 -> {
                seekBarr.progress = prevBilateralProgress
                bitmap = originalBitmap
                presenter.bilateralFilter(bitmap, progress)
                prevBilateralProgress = progress
                return
            }
            10 -> {
                seekBar.progress = prevRedCProgress
                seekBar1.progress = prevGreenCProgress
                seekBar2.progress = prevBlueCProgress
                bitmap = originalBitmap
                presenter.modifyRGBContrast(bitmap, seekBar.progress.toDouble(),seekBar1.progress.toDouble(),seekBar2.progress.toDouble())
                if(seekBarr == seekBar){
                    prevRedCProgress = progress
                } else  if(seekBarr == seekBar1){
                    prevGreenCProgress = progress
                } else prevBlueCProgress = progress
                return
            }

            11 -> {
                seekBar.progress = prevRedBProgress
                seekBar1.progress = prevGreenBProgress
                seekBar2.progress = prevBlueBProgress
                bitmap = originalBitmap
                presenter.modifyRGBContrast(bitmap, seekBar.progress.toDouble(),seekBar1.progress.toDouble(),seekBar2.progress.toDouble())
                if(seekBarr == seekBar){
                    prevRedBProgress = progress
                } else  if(seekBarr == seekBar1){
                    prevGreenBProgress = progress
                } else prevBlueBProgress = progress
                return
            }

            else -> return
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    override fun setBitmap(bitmapp: Bitmap) {
        imageView.setImageBitmap(bitmapp)
        bitmap=bitmapp
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}