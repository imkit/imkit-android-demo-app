package com.imkit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity implements CustomChatFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        String id = getIntent().getExtras().getString("roomId");
        String title = getIntent().getExtras().getString("title");

        CustomChatFragment fragment = CustomChatFragment.newInstance(id, title);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void doLeaveRoom() {
        onBackPressed();
    }
}
