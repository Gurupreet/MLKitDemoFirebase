package com.guru.mlkitdemofirebase.ui

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.guru.mlkitdemofirebase.R
import com.guru.mlkitdemofirebase.data.FirebaseManager
import com.guru.mlkitdemofirebase.ui.samples.MainFragment
import com.guru.mlkitdemofirebase.ui.test.TestFragment
import com.guru.mlkitdemofirebase.utill.Constants
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {
    private var TAG = Constants.TAG_LABEL
    private var handler : Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        handler = Handler()
        loadFragment(TAG)
        FirebaseManager.get().createUserAuth()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_label -> {
                TAG = Constants.TAG_LABEL
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_test -> {
                TAG = Constants.TAG_TEST
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(type : String) {
        val fragment  = when (type) {
            Constants.TAG_LABEL -> MainFragment()
            Constants.TAG_TEST -> TestFragment()
            else -> loadFragment(Constants.TAG_LABEL)
        }
        handler?.post {
            try {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.containerView, fragment as android.support.v4.app.Fragment)
                fragmentTransaction.commit()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, "Error loading fragments", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
