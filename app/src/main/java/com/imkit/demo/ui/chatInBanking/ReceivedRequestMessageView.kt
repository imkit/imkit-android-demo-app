package com.imkit.demo.ui.chatInBanking

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.imkit.demo.R
import com.imkit.sdk.model.Message
import com.imkit.widget.IMMessageViewHolder
import com.imkit.widget.fragment.ChatFragment
import com.imkit.widget.recyclerview.MessageRecyclerViewHolder
import java.text.SimpleDateFormat
import java.util.*

open class ReceivedRequestMessageView(itemView: View) : MessageRecyclerViewHolder(itemView) {
    class Factory : IMMessageViewHolder.Factory {
        override fun instantiate(
            context: Context,
            container: ViewGroup,
            viewType: Int,
            chatFragment: ChatFragment
        ): IMMessageViewHolder {
            val itemView = LayoutInflater.from(context)
                .inflate(R.layout.im_message_item_with_select_left, container, false)
            val contentView =
                LayoutInflater.from(context).inflate(R.layout.im_message_transfer_request, null)
            val messageView =
                itemView.findViewById<ViewGroup>(com.imkit.widget.R.id.im_message_view)
            if (messageView != null) {
                try {
                    messageView.addView(contentView)
                } catch (e: Exception) {
                    Log.e(TAG, "add contentView", e)
                }
            }
            return ReceivedRequestMessageView(itemView)
        }

        override fun instantiate(
            context: Context,
            container: ViewGroup,
            viewType: Int
        ): IMMessageViewHolder {
            return instantiate(context, container, viewType)
        }
    }

    private var coverImageView: ImageView = itemView.findViewById(R.id.im_transaction_cover_image)
    private var actionView: View
    override fun setMessage(message: Message) {
        super.setMessage(message)
        coverImageView.setImageResource(R.drawable.ic_request_in_out)
        messageTextView.text =
            message.sender.nickname + " have sent a payment request on " + SimpleDateFormat("MM dd, yyyy hh:mm a").format(
                Date(message.messageTimeMS)
            )
        actionView.setOnClickListener {
            Toast.makeText(
                actionView.context,
                "Implement Customized View",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val TAG = "RcvTxnMessageView"
    }

    init {
        messageTextView = itemView.findViewById(R.id.im_message_text)
        actionView = itemView.findViewById(R.id.im_transaction_action_view)
    }
}