package com.guru.mlkitdemofirebase.ui.chatbot

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.guru.mlkitdemofirebase.R
import com.guru.mlkitdemofirebase.data.FirebaseManager
import com.guru.mlkitdemofirebase.data.model.Chat
import com.guru.mlkitdemofirebase.utill.FirebaseObserverType
import kotlinx.android.synthetic.main.activity_chat_bot.*

class ChatBotActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private val list = mutableListOf<Chat>()
    private var messageFirebaseSync: MessageFirebaseSync? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        setupUI()
        setupApiAi()
        setupRecyclerview()
        startFirebaseSync()
    }

    override fun onDestroy() {
        messageFirebaseSync?.stopListener()
        messageFirebaseSync = null
        super.onDestroy()
    }

    private fun setupRecyclerview() {
        chatAdapter = ChatAdapter()
        chat_recycler_view.adapter = chatAdapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        chat_recycler_view.layoutManager = layoutManager
    }

    private fun setupUI() {
        send.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupApiAi() {

    }

    private fun startFirebaseSync() {
        if (messageFirebaseSync == null) {
            messageFirebaseSync = MessageFirebaseSync()
            list.clear()
            messageFirebaseSync?.startFutureFirebaseSync(object : FirebaseResponseCompletionHandler{
                override fun onSuccess(result: Any?, observerType: FirebaseObserverType) {
                    var chat = result as Chat
                    when (observerType) {
                        FirebaseObserverType.CHILD_ADDED -> {
                            list.add(chat)
                            chatAdapter?.addItem(chat)
                        }

                        FirebaseObserverType.CHILD_CHANGED -> {
                            val iterator :MutableListIterator<Chat> = list.listIterator()
                            var index = 0;
                            while (iterator.hasNext()) {
                                if (iterator.next().chatId == chat.chatId) {
                                    iterator.set(chat)
                                 //   futurAdapter.notifyItemChanged(index)
                                    break
                                }
                                index++
                            }
                        }

                        FirebaseObserverType.CHILD_REMOVED -> {
                            val iterator :MutableListIterator<Chat> = list.listIterator()
                            var index = 0;
                            while (iterator.hasNext()) {
                                if (iterator.next().chatId == chat.chatId) {
                                    iterator.remove()
                                  //  futurAdapter.notifyItemRemoved(index)
                                    break
                                }
                                index++
                            }
                        }
                    }

                }

                override fun onFailure(result: String) {
                  //  reloadData()
                }
            })

        }
    }

    private fun sendMessage() {
        if (edittext.text.isNotBlank()) {
            FirebaseManager.get().sendMessage(edittext.text.toString())
            edittext.text.clear()
        } else {

        }
    }
}
