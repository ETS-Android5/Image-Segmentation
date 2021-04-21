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
import kotlinx.android.synthetic.main.photos_display_item.*
import org.opencv.android.OpenCVLoader

class EditorActivity : AppCompatActivity(), EditorContract.View, View.OnClickListener,
    FiltersAdapter.ItemClickListener, SeekBar.OnSeekBarChangeListener {

    override fun appContext(): Context = applicationContext
    private val TAG = "EditorActivity"
    var prevBrightnessProgress = 0
    var prevContrastProgress = 0
    var prevBinarizingProgress = 0
    var prevBlurProgress = 0
    var prevMedianBlurProgress = 0
    var prev2dConvProgress = 0
    var prevSharpenProgress = 0
    private lateinit var backButton: ImageView
    private lateinit var saveButton: TextView
    private lateinit var imageView: ImageView
    private lateinit var originalBitmap: Bitmap

    private lateinit var filterList: RecyclerView
    private lateinit var seekBar: SeekBar

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
        val FILTERS_ARRAY: List<String> = listOf("Brightness", "Contrast", "Binarizing","Blur","Median","2D Convolution","Sharpen")
        val FILTERS_SLIDER_ARRAY: List<String> = listOf("Brightness", "Contrast","Binarizing","Blur","Median","2D Convolution","Sharpen")
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
                    seekBar.max=100
                    seekBar.progress = prevBrightnessProgress
                    return
                }
                1 -> {
                    seekBar.max=50
                    seekBar.progress = prevContrastProgress
                    return
                }
                2 -> {
                    seekBar.max=255
                    seekBar.progress = prevBinarizingProgress
                    return
                }
                3 -> {
                    seekBar.max=500
                    seekBar.progress = prevBlurProgress
                    return
                }
                4 -> {
                    seekBar.max=255
                    seekBar.progress = prevMedianBlurProgress
                    return
                }
                5 -> {
                   seekBar.max=10
                   seekBar.progress = prev2dConvProgress
                   return
                }
                6 -> {
                    seekBar.max=31
                    seekBar.progress = prevSharpenProgress
                    return
                }
                else -> return
            }
        } else {
            seekBar.visibility = View.GONE
        }
        println(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter))
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        println(progress)
        println(selectedFilter)
        when (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter)) {
            0 -> {
                val diff = progress - prevBrightnessProgress
                if (diff > 0) {
                    bitmap=originalBitmap
                    presenter.increaseBrightness(bitmap, progress)
                } else {
                    bitmap=originalBitmap
                    presenter.decreaseBrightness(bitmap, progress)
                }
                prevBrightnessProgress = progress
                return
            }
            1 -> {
                val diff = progress - prevContrastProgress
                if (diff > 0) {
                    bitmap=originalBitmap
                    presenter.increaseContrast(bitmap, progress)
                } else {
                    bitmap=originalBitmap
                    presenter.decreaseContrast(bitmap, progress)
                }
                prevContrastProgress = progress
                return
            }
            2 -> {
                if(progress>=50) {
                    bitmap = originalBitmap
                    presenter.toBinary(bitmap, progress)
                }
                prevBinarizingProgress = progress
                return
            }
            3 -> {
                    bitmap = originalBitmap
                    presenter.blur(bitmap,progress)
                    prevBlurProgress = progress
                return
            }
            4 -> {
                bitmap = originalBitmap
                presenter.medianBlur(bitmap,progress)
                prevMedianBlurProgress = progress
                return
            }
            5 -> {
                bitmap = originalBitmap
                presenter.Convolution2d(bitmap,progress)
                prev2dConvProgress = progress
                return
            }
            6 -> {
                bitmap = originalBitmap
                presenter.unsharpMask(bitmap,progress)
                prevSharpenProgress = progress
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