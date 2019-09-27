package com.imkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RoomInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        String id = getIntent().getExtras().getString("roomId");
        String title = getIntent().getExtras().getString("title");

        CustomRoomInfoFragment fragment = CustomRoomInfoFragment.newInstance(id, title);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();

        fragment.setListener(() -> {
            setResult(RESULT_OK);
            finish();
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
