package com.imkit;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.imkit.sdk.ApiResponse;
import com.imkit.sdk.IMKit;
import com.imkit.sdk.IMRestCallback;
import com.imkit.sdk.model.Room;
import com.imkit.widget.fragment.IRoomListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;

public class CustomRoomListFragment extends com.imkit.widget.fragment.RoomListFragment implements IRoomListFragment {

    private static final String TAG = "RoomListFragment";

    private FloatingActionButton createRoomFab;

    public static CustomRoomListFragment newInstance() {
        Bundle args = new Bundle();
        CustomRoomListFragment fragment = new CustomRoomListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        bindViews(rootView);

        return rootView;
    }

    protected void bindViews(View rootView) {
        super.bindViews(rootView);
        createRoomFab = (FloatingActionButton) rootView.findViewById(R.id.im_roomlist_create_room_fab);
        createRoomFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMKit.logD(TAG, "onClick create room fab");
                showCreateRoomDialog();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle(getString(com.imkit.widget.R.string.app_name));

        if (IMKIT.ENABLE_ROOMLIST_DISPLAY_HOME) {
            Activity activity = requireActivity();
            if (activity instanceof AppCompatActivity) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                if (appCompatActivity.getSupportActionBar() != null) {
                    appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    Drawable resImg = requireContext().getResources().getDrawable(R.drawable.ic_nav_back);
                    resImg.setColorFilter(Color.parseColor(IMKIT.TOOLBAR_HOME_COLOR), PorterDuff.Mode.SRC_ATOP);
                    appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(resImg);

                    appCompatActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(IMKIT.TOOLBAR_BACKGROUND_COLOR)));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void showCreateRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Create/Join room");

        // Set up the input
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_room, null, false);
        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AlertDialog alertDialog = (AlertDialog) dialog;
                TextView roomNameTextView = alertDialog.findViewById(R.id.dialog_create_room_name);
                String roomName = roomNameTextView.getText().toString();
                IMKit.logD(TAG, "room name = " + roomName);
                TextView inviteeTextView = alertDialog.findViewById(R.id.dialog_create_room_invitee);
                String invitees = inviteeTextView.getText().toString();
                createAndJoinRoom(roomName, invitees);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void createAndJoinRoom(String roomName, String inviteeStr) {
        Room room = null;
        if (!TextUtils.isEmpty(roomName)) {
            final String roomId = convertNameToId(roomName);
            room = new Room();
            // If roomId is left empty or null, a unique room id will be assigned by Server
            room.setId(roomId);
            room.setName(roomName);
            room.setCover("");
        }

        String[] invitees = inviteeStr.split(",");
        if (invitees.length <= 1) {
            // Create and join a pair chat room room with one member
            IMKit.instance().createAndJoinRoom(room, inviteeStr, false, IMKIT.GROUP_INVITATION_REQUIRED, new IMRestCallback<Room>() {
                @Override
                public void onResult(Room result) {
                    loadRooms();
                }

                @Override
                public void onFailure(Call<ApiResponse<Room>> call, Throwable t) {
                    Log.e(TAG, "createAndJoinRoom", t);
                    loadRooms();
                }
            });
        } else {
            // Create and join a group chat room room with members
            List<String> inviteesList = new ArrayList<>();
            for (String invitee : invitees) {
                invitee = invitee.trim();
                if (!TextUtils.isEmpty(invitee)) {
                    inviteesList.add(invitee);
                }
            }
            IMKit.instance().createAndJoinRoom(room, inviteesList, false, IMKIT.GROUP_INVITATION_REQUIRED, new IMRestCallback<Room>() {
                @Override
                public void onResult(Room result) {
                    loadRooms();
                }

                @Override
                public void onFailure(Call<ApiResponse<Room>> call, Throwable t) {
                    Log.e(TAG, "createAndJoinRoom", t);
                    loadRooms();
                }
            });
        }

    }

    private static final String convertNameToId(String name) {
        // return name.toLowerCase(Locale.US).replaceAll("[^a-zA-Z0-9]+", "-");
        return name.toLowerCase(Locale.US);
    }
}
