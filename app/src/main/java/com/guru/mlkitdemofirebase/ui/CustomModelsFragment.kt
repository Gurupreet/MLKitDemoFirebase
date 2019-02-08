package com.guru.mlkitdemofirebase.ui


import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.ml.custom.FirebaseModelManager
import com.google.firebase.ml.custom.model.FirebaseCloudModelSource
import com.google.firebase.ml.custom.model.FirebaseModelDownloadConditions

import com.guru.mlkitdemofirebase.R

class CustomModelsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_models, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var conditionsBuilder: FirebaseModelDownloadConditions.Builder =
                FirebaseModelDownloadConditions.Builder().requireWifi()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Enable advanced conditions on Android Nougat and newer.
            conditionsBuilder = conditionsBuilder
                    .requireCharging()
                    .requireDeviceIdle()
        }
        val conditions = conditionsBuilder.build()

// Build a FirebaseCloudModelSource object by specifying the name you assigned the model
// when you uploaded it in the Firebase console.
        val cloudSource = FirebaseCloudModelSource.Builder("my_cloud_model")
                .enableModelUpdates(true)
                .setInitialDownloadConditions(conditions)
                .setUpdatesDownloadConditions(conditions)
                .build()
        FirebaseModelManager.getInstance().registerCloudModelSource(cloudSource)

    }


}
