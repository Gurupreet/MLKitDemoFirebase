package com.guru.mlkitdemofirebase.ui

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.guru.mlkitdemofirebase.R
import com.guru.mlkitdemofirebase.data.MLManager
import com.guru.mlkitdemofirebase.data.MLResponseListener
import com.guru.mlkitdemofirebase.data.model.Item
import com.guru.mlkitdemofirebase.utill.Constants
import kotlinx.android.synthetic.main.row_image_layout.view.*
import java.util.logging.Logger

class HorizontalRecyclerAdapter(val mList: ArrayList<Any>, val mContext: Context?, var type : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_image_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImageHolder).bind(mList[position], type)
    }


    class ImageHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val LOG = Logger.getLogger(this.javaClass.name)
        // Holds the TextView that will add each animal to
        fun bind(any: Any, type: String) {
            val item = any as Item
            itemView.recy_title?.text = item.text
            itemView.recy_image?.setImageResource(item.id)
            val visionImage: FirebaseVisionImage = FirebaseVisionImage.fromBitmap((itemView.recy_image?.drawable as BitmapDrawable).bitmap)
            when (type) {
                Constants.TAG_LABEL -> MLManager.get().detectLabel(visionImage, object : MLResponseListener {
                    override fun onSuccess(result: Any, type: String) {
                        val list = result as List<FirebaseVisionImageLabel>
                        itemView.recy_title?.text = ""
                        list.forEach {
                            itemView.recy_title?.append(it.text + " " + it.confidence + "\n")

                        }
                    }

                    override fun onFailure(error: String) {
                        itemView.recy_title?.text = "ML Failed to detect"

                    }
                })

                Constants.TAG_TEXT -> MLManager.get().detectText(visionImage, object : MLResponseListener {
                    override fun onSuccess(result: Any, type: String) {
                        val item = result as FirebaseVisionText

                        itemView.recy_title?.text = ""
                        item.textBlocks.forEach {
                            itemView.recy_title?.append(it.text + " ")
                        }
                        if (itemView.recy_title?.text!!.isEmpty()) {
                            itemView.recy_title.text = "Couldn't find anything"
                        }
                    }

                    override fun onFailure(error: String) {
                        itemView.recy_title?.text = error
                    }
                })

                Constants.TAG_FACE -> MLManager.get().detectFace(visionImage, object : MLResponseListener {
                    override fun onSuccess(result: Any, type: String) {
                        val list = result as List<FirebaseVisionFace>
                        itemView.recy_title?.text = ""
                        if (list.isEmpty()) {
                            itemView.recy_title?.text = "No face detected"

                        }
                        list.forEach {
                            itemView.recy_title?.append("Person smiling : " + it.smilingProbability + "\n"+
                                                        "Left eye open : " + it.leftEyeOpenProbability + "\n"+
                                                         "Right eye open : " + it.rightEyeOpenProbability + "\n")
                        }
                    }

                    override fun onFailure(error: String) {
                        itemView.recy_title?.text = "No face detected"
                    }
                })
            }

        }
    }
}