package com.imkit.demo.ui.networkingChat

import com.imkit.widget.fragment.RoomListFragment
import android.os.Bundle
import android.text.TextUtils

class CustomRoomListFragment : RoomListFragment() {
    companion object {
        fun newInstance(tag: String?): CustomRoomListFragment {
            val args = Bundle()
            if (!TextUtils.isEmpty(tag)) {
                // filter by room tag
                args.putString(ARG_ROOM_TAG, tag)
            }
            val fragment = CustomRoomListFragment()
            fragment.arguments = args
            return fragment
        }
    }


}