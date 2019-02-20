package com.guru.mlkitdemofirebase.ui.chatbot

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.guru.mlkitdemofirebase.R
import com.guru.mlkitdemofirebase.data.model.Chat
import kotlinx.android.synthetic.main.activity_chat_bot.*
import kotlinx.android.synthetic.main.row_chat_layout.view.*

class ChatAdapter: RecyclerView.Adapter<ChatAdapter.ChatVH>() {
    private var mList= mutableListOf<Chat>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ChatVH {
      return ChatVH(LayoutInflater.from(parent.context).inflate(R.layout.row_chat_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ChatVH, position: Int) {
       holder.bind(mList[position])
    }

    fun setData(list: MutableList<Chat>) {
        mList = list
        notifyDataSetChanged()
    }

    fun addItem(chat: Chat) {
        mList.add(chat)
        notifyItemInserted(mList.size)
    }

    fun clearData() {

    }


    class ChatVH(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(chat: Chat) {
            itemView.message.text = chat.message
            itemView.username.text = "@${chat.username}"
            if (chat.username != "bot") {
                Glide.with(itemView.context)
                        .load(R.drawable.ic_user_secret)
                        .into(itemView.avatar)
            } else {
                Glide.with(itemView.context)
                        .load(R.drawable.ic_robot_solid)
                        .into(itemView.avatar)
            }
        }
    }
}