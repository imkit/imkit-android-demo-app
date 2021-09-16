package com.imkit.demo.ui.tradingPlatform

import com.imkit.widget.fragment.ChatFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.imkit.sdk.IMKit
import com.imkit.sdk.model.Client
import com.imkit.widget.IMWidgetPreferences
import com.imkit.widget.fragment.IChatSearchFragment
import com.imkit.widget.utils.Utils

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