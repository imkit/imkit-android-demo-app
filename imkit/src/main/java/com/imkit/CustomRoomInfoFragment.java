package com.imkit;

import android.os.Bundle;

import com.imkit.widget.fragment.RoomInfoFragment;

public class CustomRoomInfoFragment extends RoomInfoFragment {

    public static CustomRoomInfoFragment newInstance(String roomId, String title) {
        Bundle args = new Bundle();
        args.putString(ARG_ROOM_ID, roomId);
        args.putString(ARG_TITLE, title);
        CustomRoomInfoFragment fragment = new CustomRoomInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
