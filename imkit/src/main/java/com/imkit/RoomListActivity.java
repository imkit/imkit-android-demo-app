package com.imkit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.imkit.sdk.IMKit;
import com.imkit.sdk.model.Room;
import com.imkit.widget.fragment.RoomListFragment;
import com.imkit.widget.utils.Utils;

public class RoomListActivity extends AppCompatActivity {

    private static final String TAG = "RoomListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        final CustomRoomListFragment fragment = CustomRoomListFragment.newInstance();
        fragment.setListener(new RoomListFragment.RoomListFragmentListener() {
            /**
             * On user select a room in list
             * @param room
             */
            @Override
            public void onRoomSelect(Room room) {
                IMKIT.showChat(RoomListActivity.this, room.getId(), Utils.getDisplayRoomTitle(RoomListActivity.this, room), 7000);
            }

            /**
             * Override to custom the hint view which shows when room list is empty
             * @param parent The container which will holds the hint view
             * @return Custom hint view.
             */
            @Override
            public View onCreateRoomListEmptyView(ViewGroup parent) {
                View view = LayoutInflater.from(RoomListActivity.this).inflate(com.imkit.widget.R.layout.im_room_list_empty, parent, false);
                view.findViewById(R.id.txtCreateRoom).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment.onCreateRoomClicked();
                    }
                });
                return view;
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        IMKit.instance().disconnect();
        IMKit.instance().connect();

        IMKIT.getBadge(null);
    }
}