package com.imkit.demo.ui.businessChat

import com.imkit.widget.fragment.ChatFragment
import android.os.Bundle

class CustomChatFragment : ChatFragment() {
    companion object {
        fun newInstance(roomId: String, title: String): CustomChatFragment {
            val args = Bundle()
            args.putString(ARG_ROOM_ID, roomId)
            args.putString(ARG_TITLE, title)
            val fragment = CustomChatFragment()
            fragment.arguments = args
            return fragment
        }
    }
}