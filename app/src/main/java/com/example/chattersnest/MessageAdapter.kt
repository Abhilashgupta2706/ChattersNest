package com.example.chattersnest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVED = 1
    private val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.received_msg_layout, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.send_msg_layout, parent, false)
            return SendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = messageList[position]

        if (holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            holder.tvSentMsg.text = currentMsg.message

        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.tvReceivedMsg.text = currentMsg.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMsg = messageList[position]

        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMsg.senderId)) {
            ITEM_SENT
        } else {
            ITEM_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }


    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSentMsg: TextView = itemView.findViewById(R.id.tvSentMsg)
    }


    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvReceivedMsg: TextView = itemView.findViewById(R.id.tvReceivedMsg)
    }
}