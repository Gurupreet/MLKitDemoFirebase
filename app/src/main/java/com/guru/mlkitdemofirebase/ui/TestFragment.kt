package com.guru.mlkitdemofirebase.ui


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.guru.mlkitdemofirebase.BuildConfig
import com.guru.mlkitdemofirebase.data.MLManager
import com.guru.mlkitdemofirebase.data.MLResponseListener
import com.guru.mlkitdemofirebase.R
import kotlinx.android.synthetic.main.fragment_test.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TestFragment : Fragment() {
    companion object {
        private const val REQUEST_PERMISSION_CAMERA = 10
        private const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 11
        private const val REQUEST_CODE_IMAGE_SELECTION = 9
        private const val REQUEST_CODE_IMAGE_CAPTURE = 8
    }

    private var visionImage: FirebaseVisionImage? = null
    private var cameraUri: Uri? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        visionImage = FirebaseVisionImage.fromBitmap((test_image?.drawable as BitmapDrawable).bitmap)

        test_label.setOnClickListener {
            result_text?.visibility = View.VISIBLE
            result_text?.text = "Running ML..."
            MLManager.get().detectLabel(visionImage!!, object : MLResponseListener {
                override fun onSuccess(result: Any, type: String) {
                    val list = result as List<FirebaseVisionImageLabel>
                    result_text?.text = ""
                    list.forEach {
                        result_text.append(it.text + " " + it.confidence + "\n")
                    }
                }

                override fun onFailure(error: String) {
                    result_text.append("Failed to detect anything")
                }
            })
        }

        test_text.setOnClickListener {
            result_text?.visibility = View.VISIBLE
            result_text?.text = "Running ML..."
            MLManager.get().detectText(visionImage!!, object : MLResponseListener {
                override fun onSuccess(result: Any, type: String) {
                    val item = result as FirebaseVisionText
                    result_text?.text = ""
                    item.textBlocks.forEach {
                        result_text?.append(it.text + " ")
                    }
                }

                override fun onFailure(error: String) {
                    result_text.text = "Failed to detect anything"
                }
            })
        }

        test_face.setOnClickListener {
            result_text?.visibility = View.VISIBLE
            result_text?.text = "Running ML..."
            MLManager.get().detectFace(visionImage!!, object : MLResponseListener {
                override fun onSuccess(result: Any, type: String) {
                    val list = result as List<FirebaseVisionFace>
                    result_text.text = ""
                    if (list.isEmpty()) {
                        result_text.text = "No face detected"
                    }
                    list.forEach {
                        result_text?.append("Person smiling : " + it.smilingProbability + "\n")
                    }
                }

                override fun onFailure(error: String) {
                    result_text.text = "Failed to detect anything"
                }
            })
        }

        upload.setOnClickListener {
            result_text?.text = ""
            result_text?.visibility = View.GONE
            checkPermissionAndOpenGallery()
        }

        camera.setOnClickListener {
            result_text?.text = ""
            result_text?.visibility = View.GONE
            checkCameraPermissionAndOpenCamera()
        }

    }

    private fun checkPermissionAndOpenGallery() {
        val hasWriteStoragePermission = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (!hasWriteStoragePermission) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
            return
        }
        openGallery()
    }

    private fun checkCameraPermissionAndOpenCamera() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val hasWriteStoragePermission = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (!hasCameraPermission || !hasWriteStoragePermission) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
            return
        }
        openCamera()
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_CODE_IMAGE_SELECTION)
    }

    private fun openCamera() {
        try {
            cameraUri =   FileProvider.getUriForFile(context!!,
                    BuildConfig.APPLICATION_ID + ".provider",
                    createImageFile())
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    cameraUri)
            startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE)
        } catch (ex: IOException) {

        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_SELECTION && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            test_image?.setImageURI(imageUri)
            visionImage = FirebaseVisionImage.fromFilePath(activity!!, imageUri!!)
        } else if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            test_image?.setImageURI(cameraUri)
            visionImage = FirebaseVisionImage.fromFilePath(activity!!, cameraUri!!)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else if (requestCode == REQUEST_PERMISSION_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }
}