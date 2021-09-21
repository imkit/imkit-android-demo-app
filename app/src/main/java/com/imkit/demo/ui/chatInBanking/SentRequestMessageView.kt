package com.imkit.demo.ui.chatInBanking

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imkit.demo.R
import com.imkit.sdk.model.Message
import com.imkit.widget.IMMessageViewHolder
import com.imkit.widget.fragment.ChatFragment
import java.text.SimpleDateFormat
import java.util.*

class SentRequestMessageView(itemView: View?) : ReceivedRequestMessageView(
    itemView!!
) {
    class Factory : IMMessageViewHolder.Factory {
        override fun instantiate(
            context: Context,
            container: ViewGroup,
            viewType: Int,
            chatFragment: ChatFragment
        ): IMMessageViewHolder {
            val itemView = LayoutInflater.from(context)
                .inflate(R.layout.im_message_item_with_select_right, container, false)
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
            return SentRequestMessageView(itemView)
        }

        override fun instantiate(
            context: Context,
            container: ViewGroup,
            viewType: Int
        ): IMMessageViewHolder {
            return instantiate(context, container, viewType)
        }
    }

    override fun setMessage(message: Message) {
        super.setMessage(message)
        messageTextView.text =
            "You have sent a payment request on " + SimpleDateFormat("MM dd, yyyy hh:mm a")
                .format(Date(message.messageTimeMS))
    }

    companion object {
        private const val TAG = "SentTxnMessageView"
    }
}