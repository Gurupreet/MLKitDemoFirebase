package com.guru.mlkitdemofirebase

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var TAG = Constants.TAG_LABEL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadFragment(TAG)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_label -> {
                TAG = Constants.TAG_LABEL
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_text -> {
                TAG = Constants.TAG_TEXT
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_face -> {
                TAG = Constants.TAG_FACE
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(type : String) {
        val fragment  = when (type) {
            Constants.TAG_LABEL -> LabelImageFragment()
            Constants.TAG_TEXT -> TextRecognitionFragment()
            Constants.TAG_FACE -> FaceDetectionFragment()
            else -> loadFragment("two")
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerView, fragment as android.support.v4.app.Fragment)
        fragmentTransaction.commit()
    }
}
