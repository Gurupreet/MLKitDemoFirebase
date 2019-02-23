package com.guru.mlkitdemofirebase.data

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MLManager {

    companion object {
        private val INSTANCE : MLManager? = null;
        fun get() : MLManager =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: MLManager()
                }
        private val faceOptions = FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build()
    }

    fun detectLabel(visionImage : FirebaseVisionImage, responseListener: MLResponseListener) {
        val labelDetector: FirebaseVisionImageLabeler = FirebaseVision.getInstance().onDeviceImageLabeler
        labelDetector.processImage(visionImage)
                .addOnSuccessListener { responseListener.onSuccess(it, "") }
                .addOnFailureListener { responseListener.onFailure("") }
    }

    fun detectText(visionImage: FirebaseVisionImage, responseListener: MLResponseListener) {
        val textDetector = FirebaseVision.getInstance().onDeviceTextRecognizer
        textDetector.processImage(visionImage)
                .addOnSuccessListener { responseListener.onSuccess(it, "") }
                .addOnFailureListener{ responseListener.onFailure(it.toString()) }
    }

    fun detectFace(vistionImage: FirebaseVisionImage, responseListener: MLResponseListener) {
        val faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(faceOptions)
        faceDetector.detectInImage(vistionImage)
                .addOnSuccessListener { responseListener.onSuccess(it, "") }
                .addOnFailureListener { responseListener.onFailure("") }
    }
}