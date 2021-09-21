package com.imkit.demo.ui.chatInBanking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imkit.demo.R
import com.imkit.widget.IMRoomViewHolder
import com.imkit.widget.recyclerview.RoomRecyclerViewHolder

class ChatInBankingRoomView(itemView: View) : RoomRecyclerViewHolder(itemView) {
    class Factory : IMRoomViewHolder.Factory {
        override fun instantiate(
            context: Context,
            container: ViewGroup,
            viewType: Int
        ): ChatInBankingRoomView {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.im_room_list_item, container, false)
            return ChatInBankingRoomView(itemView)
        }
    }

    init {
        itemView.findViewById<View>(R.id.im_room_subtitle).visibility = View.GONE
    }
}