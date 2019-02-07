package com.guru.mlkitdemofirebase.data

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector

class MLManager {

    companion object {
        private val INSTANCE : MLManager? = null;
        fun get() : MLManager =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: MLManager()
                }
    }

    fun detectLabel(visionImage : FirebaseVisionImage, responseListener: MLResponseListener) {
        val labelDetector: FirebaseVisionLabelDetector = FirebaseVision.getInstance().visionLabelDetector
        labelDetector.detectInImage(visionImage)
                .addOnSuccessListener { responseListener.onSuccess(it, "") }
                .addOnFailureListener { responseListener.onFailure("") }
    }

    fun detectText(visionImage: FirebaseVisionImage, responseListener: MLResponseListener) {
        val textDetector = FirebaseVision.getInstance().visionTextDetector
        textDetector.detectInImage(visionImage)
                .addOnSuccessListener { responseListener.onSuccess(it, "") }
                .addOnFailureListener{ responseListener.onFailure("") }
    }

    fun detectFace(vistionImage: FirebaseVisionImage, responseListener: MLResponseListener) {
        val faceDetector = FirebaseVision.getInstance().visionFaceDetector
        faceDetector.detectInImage(vistionImage)
                .addOnSuccessListener { responseListener.onSuccess(it, "") }
                .addOnFailureListener { responseListener.onFailure("") }
    }
}