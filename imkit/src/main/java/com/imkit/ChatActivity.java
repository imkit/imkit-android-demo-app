package com.imkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.imkit.sdk.IMKit;

public class ChatActivity extends AppCompatActivity implements CustomChatFragment.Listener {

    private CustomChatFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        String id = getIntent().getExtras().getString("roomId");
        String title = getIntent().getExtras().getString("title");

        fragment = CustomChatFragment.newInstance(id, title);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IMKIT.setCurrentActiveRoomID(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == fragment.REQ_ROOM_INFO) {
            doLeaveRoom();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IMKit.instance().disconnect();
        IMKit.instance().connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        IMKIT.setCurrentActiveRoomID("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void doLeaveRoom() {
        finish();
    }
}