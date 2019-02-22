package com.guru.mlkitdemofirebase.ui.chatbot

import android.support.v4.content.ContextCompat
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

    fun addItem(chat: Chat) {
        mList.add(chat)
        notifyItemInserted(mList.size-1)
    }

    fun deleteItem(index: Int) {
        if (index < mList.size) {
            mList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class ChatVH(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(chat: Chat) {
            if (chat.username == "Bot") {
                itemView.message.text = chat.message
                itemView.username.text = "@${chat.username}"
                Glide.with(itemView.context)
                        .load(R.drawable.ic_bot)
                        .into(itemView.avatar)

                itemView.message.visibility = View.VISIBLE
                itemView.username.visibility = View.VISIBLE
                itemView.avatar.visibility = View.VISIBLE
                itemView.message_right.visibility = View.GONE
                itemView.username_right.visibility = View.GONE
                itemView.avatar_right.visibility = View.GONE
            } else {
                itemView.message_right.text = chat.message
                itemView.username_right.text = "@${chat.username}"
                Glide.with(itemView.context)
                        .load(R.drawable.ic_person_outline_black_24dp)
                        .into(itemView.avatar_right)

                itemView.message.visibility = View.GONE
                itemView.username.visibility = View.GONE
                itemView.avatar.visibility = View.GONE
                itemView.message_right.visibility = View.VISIBLE
                itemView.username_right.visibility = View.VISIBLE
                itemView.avatar_right.visibility = View.VISIBLE

            }
        }
    }
}