package com.imkit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RoomInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        String id = getIntent().getExtras().getString("roomId");
        String title = getIntent().getExtras().getString("title");

        CustomRoomInfoFragment fragment = CustomRoomInfoFragment.newInstance(id, title);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();

        fragment.setListener(new CustomRoomInfoFragment.RoomInfoFragmentListener() {
            @Override
            public void roomLeaved() {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
