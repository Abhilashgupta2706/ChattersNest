package com.example.chattersnest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var iiMsgContainer: LinearLayout
    private lateinit var etSendMessage: EditText
    private lateinit var fabSendMsg: FloatingActionButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val userName = intent.getStringExtra("UserName")
        val userUid = intent.getStringExtra("UserUid")

        supportActionBar?.title = userName

        rvChat = findViewById(R.id.rvChat)
        etSendMessage = findViewById(R.id.etSendMessage)
        fabSendMsg = findViewById(R.id.fabSendMsg)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

    }
}