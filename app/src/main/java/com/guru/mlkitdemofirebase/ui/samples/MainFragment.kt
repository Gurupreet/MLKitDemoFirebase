package com.guru.mlkitdemofirebase.ui.samples


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.guru.mlkitdemofirebase.R
import com.guru.mlkitdemofirebase.data.model.Item
import com.guru.mlkitdemofirebase.ui.chatbot.ChatBotActivity
import com.guru.mlkitdemofirebase.utill.Constants


class MainFragment : Fragment() {
    private lateinit var adapterLabel: HorizontalRecyclerAdapter
    private lateinit var adapterText: HorizontalRecyclerAdapter
    private lateinit var adapterFace: HorizontalRecyclerAdapter
    private val lables = ArrayList<Any>()
    private val texts = ArrayList<Any>()
    private val faces = ArrayList<Any>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        adapterFace.notifyDataSetChanged()
        adapterText.notifyDataSetChanged()
        adapterLabel.notifyDataSetChanged()
        chat.setOnClickListener {
            val intent = Intent(context, ChatBotActivity::class.java)
            startActivity(intent)
        }


    }

    private fun setupRecyclerview() {
        lables.add(Item("ML running .. ", R.drawable.food))
        lables.add(Item("ML running .. ", R.drawable.breads))
        lables.add(Item("ML running .. ", R.drawable.running))
        lables.add(Item("ML running .. ", R.drawable.l3))
        lables.add(Item("ML running .. ", R.drawable.l4))
        lables.add(Item("ML running .. ", R.drawable.l5))
        lables.add(Item("ML running .. ", R.drawable.l6))

        texts.add(Item("ML running .. ", R.drawable.qoute))
        texts.add(Item("ML running .. ", R.drawable.q2))
        texts.add(Item("ML running .. ", R.drawable.q3))
        texts.add(Item("ML running .. ", R.drawable.q4))
        texts.add(Item("ML running .. ", R.drawable.q5))
        texts.add(Item("ML running .. ", R.drawable.q6))

        faces.add(Item("ML running .. ", R.drawable.food))
        faces.add(Item("ML running .. ", R.drawable.l4))
        faces.add(Item("ML running .. ", R.drawable.l5))

        val lm : RecyclerView.LayoutManager  = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        label_recyclerview?.layoutManager = lm
        adapterLabel = HorizontalRecyclerAdapter(lables, context, Constants.TAG_LABEL)
        label_recyclerview?.adapter = adapterLabel

        val lm1 : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        text_recyclerview?.layoutManager = lm1
        adapterText = HorizontalRecyclerAdapter(texts, context, Constants.TAG_TEXT)
        text_recyclerview?.adapter = adapterText

        val lm2 : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        face_recyclerview?.layoutManager = lm2
        adapterFace = HorizontalRecyclerAdapter(faces, context, Constants.TAG_FACE)
        face_recyclerview?.adapter = adapterFace
    }




}
