package com.guru.mlkitdemofirebase.ui.chatbot

import ai.api.android.AIService
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.guru.mlkitdemofirebase.R
import com.guru.mlkitdemofirebase.data.FirebaseManager
import com.guru.mlkitdemofirebase.data.model.Chat
import com.guru.mlkitdemofirebase.utill.FirebaseObserverType
import kotlinx.android.synthetic.main.activity_chat_bot.*

import ai.api.AIDataService
import ai.api.model.AIError
import ai.api.model.AIResponse

import ai.api.model.AIRequest
import ai.api.ui.AIDialog
import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.guru.mlkitdemofirebase.data.ChatBotManager
import com.guru.mlkitdemofirebase.data.MessageFirebaseSync
import com.guru.mlkitdemofirebase.utill.FirebaseResponseCompletionHandler
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class ChatBotActivity : AppCompatActivity(), AIDialog.AIDialogListener, CoroutineScope {

    private lateinit var chatAdapter: ChatAdapter
    private val list = mutableListOf<Chat>()
    private var messageFirebaseSync: MessageFirebaseSync? = null

    private lateinit var aiservice: AIService
    private lateinit var aiRequest: AIRequest

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        job = Job()
        setupUI()
        setupApiAi()
        setupRecyclerview()
        startFirebaseSync()
    }

    override fun onDestroy() {
        messageFirebaseSync?.stopListener()
        messageFirebaseSync = null
        aiservice?.cancel()
        job?.cancel()
        super.onDestroy()
    }

    override fun onResume() {
        aiservice?.resume()
        super.onResume()
    }

    override fun onPause() {
        aiservice?.pause()
        super.onPause()
    }

    private fun setupRecyclerview() {
        chatAdapter = ChatAdapter()
        chat_recycler_view.adapter = chatAdapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        chat_recycler_view.layoutManager = layoutManager
    }

    private fun setupUI() {
        send.setOnClickListener {
            sendMessage()
        }
        edittext.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage()
                    return true
                }
                return false
            }
        })
        mic.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                getAIMicDailog()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1);
            }
        }
    }

    private fun setupApiAi() {
        aiservice = ChatBotManager.get().getAIService(this)
        aiRequest = AIRequest()
    }

    private fun getAIMicDailog() {
        val aiDialog = ChatBotManager.get().getAIDialog(this)
        aiDialog.setResultsListener(this)
        aiDialog.showAndListen()
    }

    private fun startFirebaseSync() {
        if (messageFirebaseSync == null) {
            messageFirebaseSync = MessageFirebaseSync()
            list.clear()
            messageFirebaseSync?.startMessageFirebaseSync(object : FirebaseResponseCompletionHandler {
                override fun onSuccess(result: Any?, observerType: FirebaseObserverType) {
                    var chat = result as Chat
                    when (observerType) {

                        FirebaseObserverType.CHILD_ADDED -> {
                            list.add(chat)
                            chatAdapter?.addItem(chat)
                            chat_recycler_view?.smoothScrollToPosition(list.size)
                        }

                        FirebaseObserverType.CHILD_REMOVED -> {
                            val iterator :MutableListIterator<Chat> = list.listIterator()
                            var index = 0
                            while (iterator.hasNext()) {
                                if (iterator.next().chatId == chat.chatId) {
                                    iterator.remove()
                                    chatAdapter?.deleteItem(index)
                                    break
                                }
                                index++
                            }
                        }
                    }
                }

                override fun onFailure(result: String) {
                    Toast.makeText(this@ChatBotActivity, "Failed to load chat", Toast.LENGTH_SHORT)
                }
            })

        }
    }

    private fun sendMessage() {
        if (edittext.text.isNotBlank()) {
            FirebaseManager.get().sendMessage(edittext.text.toString(), "User")
            queryAPiAI()
            edittext.text.clear()
        } else {

        }
    }

    private fun queryAPiAI() {
        aiRequest.setQuery(edittext.text.toString())
        launch {
            val response = withContext(Dispatchers.Default) { ChatBotManager.aiDataService.request(aiRequest) }
            sendMessage(response.result?.fulfillment?.speech!!, "Bot")
        }
    }

    private fun sendMessage(message: String, user: String) {
        FirebaseManager.get().sendMessage(message, user)
    }

    override fun onCancelled() {}

    override fun onResult(response: AIResponse?) {
        sendMessage(response?.result?.resolvedQuery!!, "User")
        sendMessage(response?.result?.fulfillment?.speech!!, "Bot")
    }

    override fun onError(error: AIError?) {}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chatbot_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
             android.R.id.home -> finish()
             R.id.delete -> FirebaseManager.get().deleteAllChat()
            }
        return super.onOptionsItemSelected(item)
    }
}
