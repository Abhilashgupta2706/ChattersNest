package com.example.chattersnest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var iiMsgContainer: LinearLayout
    private lateinit var etSendMessage: EditText
    private lateinit var fabSendMsg: FloatingActionButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val userName = intent.getStringExtra("UserName")
        val receiverUid = intent.getStringExtra("UserUid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = userName

        rvChat = findViewById(R.id.rvChat)
        etSendMessage = findViewById(R.id.etSendMessage)
        fabSendMsg = findViewById(R.id.fabSendMsg)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = messageAdapter

        mDbRef
            .child("chats")
            .child(senderRoom!!)
            .child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for (postSnapShot in snapshot.children) {
                        val message = postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        fabSendMsg.setOnClickListener {
            if (etSendMessage.text.toString() == "") {
                return@setOnClickListener
            }

            val message = etSendMessage.text.toString()
            val messageObj = Message(message, senderUid)

            mDbRef
                .child("chats")
                .child(senderRoom!!)
                .child("messages")
                .push()
                .setValue(messageObj).addOnSuccessListener {
                    mDbRef
                        .child("chats")
                        .child(receiverRoom!!)
                        .child("messages")
                        .push()
                        .setValue(messageObj)
                }

            etSendMessage.setText("")
        }
    }
}