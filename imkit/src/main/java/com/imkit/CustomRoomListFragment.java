package com.imkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imkit.sdk.ApiResponse;
import com.imkit.sdk.IMKit;
import com.imkit.sdk.IMRestCallback;
import com.imkit.sdk.model.Room;
import com.imkit.widget.fragment.IRoomListFragment;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;

import static android.app.Activity.RESULT_OK;

public class CustomRoomListFragment extends com.imkit.widget.fragment.RoomListFragment implements IRoomListFragment {

    private static final String TAG = "RoomListFragment";

    private ImageView imgCreateRoom;

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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        bindViews(rootView);

        return rootView;
    }

    private void bindViews(View rootView) {
        imgCreateRoom = rootView.findViewById(R.id.imgCreateRoom);
        imgCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick create room fab");
                showCreateRoomDialog();
            }
        });
        imgCreateRoom.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle(getString(R.string.app_name));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 建立私聊→選擇人員
                case 0xFF2F: {
                    ArrayList<Integer> list = data.getIntegerArrayListExtra("member_list");
                    if (list.size() == 0) {
                        Toast.makeText(getContext(), "Please select at least one users", Toast.LENGTH_SHORT).show();
                    } else if (list.size() == 1) {
                        IMKIT.createRoomWithUser(String.valueOf(list.get(0)), new IIMKIT.CreateRoom() {
                            @Override
                            public void success(String roomId, String title) {
                                Toast.makeText(getContext(), "Room Created", Toast.LENGTH_SHORT).show();
                                IMKIT.showChat(getActivity(), roomId, title, 7000);
                            }

                            @Override
                            public void failed(String reason) {
                                Toast.makeText(getContext(), "Create room failed, " + reason, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ArrayList<String> users = new ArrayList<>();
                        for (int id : list) {
                            users.add(String.valueOf(id));
                        }

                        IMKIT.createRoomWithUsers(users, new IIMKIT.CreateRoom() {
                            @Override
                            public void success(String roomId, String title) {
                                Toast.makeText(getContext(), "Room Created", Toast.LENGTH_SHORT).show();
                                IMKIT.showChat(getActivity(), roomId, title, 7000);
                            }

                            @Override
                            public void failed(String reason) {
                                Toast.makeText(getContext(), "Create room failed, " + reason, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
            }
        }
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
                Log.d(TAG, "room name = " + roomName);
                TextView inviteeTextView = alertDialog.findViewById(R.id.dialog_create_room_invitee);
                String invitee = inviteeTextView.getText().toString();
                if (!TextUtils.isEmpty(roomName)) {
                    createAndJoinRoom(roomName, invitee);
                } else {
                    Log.e(TAG, "empty room name");
                }
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

    private void createAndJoinRoom(String roomName, String invitee) {
        final String roomId = convertNameToId(roomName);
        final String inviteeId = convertNameToId(invitee);
        Room room = new Room();
        // If roomId is left empty or null, a unique room id will be assigned by Server
        room.setId(roomId);
        room.setName(roomName);
        room.setCover("");
        room.setDescription("Demo room " + roomName);
        IMKit.instance().createAndJoinRoom(room, inviteeId, new IMRestCallback<Room>() {
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

    private static final String convertNameToId(String name) {
        return name.toLowerCase(Locale.US).replaceAll("[^a-zA-Z0-9]+", "-");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.im_room_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.im_room_list_create) {
            showCreateRoomDialog();
        }

        return false;
    }
}
