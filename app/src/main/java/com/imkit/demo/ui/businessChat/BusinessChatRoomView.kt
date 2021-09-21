package com.imkit.demo.ui.businessChat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imkit.demo.R
import com.imkit.widget.IMRoomViewHolder
import com.imkit.widget.recyclerview.RoomRecyclerViewHolder

class BusinessChatRoomView(itemView: View) : RoomRecyclerViewHolder(itemView) {
    class Factory : IMRoomViewHolder.Factory {
        override fun instantiate(
            context: Context,
            container: ViewGroup,
            viewType: Int
        ): BusinessChatRoomView {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.im_room_list_item, container, false)
            return BusinessChatRoomView(itemView)
        }
    }
}