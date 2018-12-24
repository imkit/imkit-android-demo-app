package com.imkit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imkit.sdk.model.Room;
import com.imkit.widget.fragment.RoomListFragment;
import com.imkit.widget.utils.Utils;

public class RoomListActivity extends AppCompatActivity {

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
                IMKIT.showChat(RoomListActivity.this, room.getId(), Utils.getDisplayRoomTitle(room), 7000);
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
}
