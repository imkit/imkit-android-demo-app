package com.imkit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.imkit.widget.fragment.IChatFragment;

import androidx.appcompat.app.AppCompatActivity;

public class CustomChatFragment extends com.imkit.widget.fragment.ChatFragment implements IChatFragment {

    public final int REQ_ROOM_INFO = 7000;

    public interface Listener {
        void doLeaveRoom();
    }

    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof Activity)) {
            return;
        }

        if (context instanceof Listener) {
            listener = (Listener) context;
        }
    }

    public static CustomChatFragment newInstance(String roomId, String title) {
        Bundle args = new Bundle();
        args.putString(ARG_ROOM_ID, roomId);
        args.putString(ARG_TITLE, title);
        CustomChatFragment fragment = new CustomChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Activity activity = getActivity();
        if (activity != null) {
            if (activity instanceof AppCompatActivity) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                if (appCompatActivity.getSupportActionBar() != null) {
                    Drawable resImg = requireContext().getResources().getDrawable(R.drawable.ic_nav_back);
                    resImg.setColorFilter(Color.parseColor(IMKIT.TOOLBAR_HOME_COLOR), PorterDuff.Mode.SRC_ATOP);
                    appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(resImg);

                    appCompatActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(IMKIT.TOOLBAR_BACKGROUND_COLOR)));
                }
            }
        }
    }

    @Override
    public void leaveRoom() {
        super.leaveRoom();
        if (listener != null) {
            listener.doLeaveRoom();
        }
    }
}
