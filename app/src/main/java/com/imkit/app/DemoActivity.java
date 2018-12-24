package com.imkit.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imkit.IIMKIT;
import com.imkit.IMKIT;

public class DemoActivity extends AppCompatActivity {

    private EditText edUserName;
    private Button btnLogin;
    private Button btnCreateRoom;
    private Button btnShowRoomList;
    private EditText edRoomId;
    private EditText edRoomTitle;
    private Button btnEnterRoom;
    private Button btnRoomInfo;
    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edUserName = findViewById(R.id.edUserName);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnShowRoomList = findViewById(R.id.btnShowRoomList);
        edRoomId = findViewById(R.id.edRoomId);
        edRoomTitle = findViewById(R.id.edRoomTitle);
        btnEnterRoom = findViewById(R.id.btnEnterRoom);
        btnRoomInfo = findViewById(R.id.btnRoomInfo);
        btnSignOut = findViewById(R.id.btnSignOut);

        btnLogin.setOnClickListener(v -> {
            if (edUserName.getText().toString().isEmpty()) {
                Toast.makeText(DemoActivity.this, "Input UserName first", Toast.LENGTH_SHORT).show();
            } else {
                IMKIT.login(DemoActivity.this, edUserName.getText().toString(), new IIMKIT.Login() {
                    @Override
                    public void success() {
                        Toast.makeText(DemoActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        edUserName.setEnabled(false);
                        btnLogin.setEnabled(false);
                        btnSignOut.setEnabled(true);
                    }

                    @Override
                    public void failed(String reason) {
                        Toast.makeText(DemoActivity.this, "Login Failed : " + reason, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCreateRoom.setOnClickListener(v -> IMKIT.createRoom(DemoActivity.this, true, 7000, new IIMKIT.CreateRoom() {
            @Override
            public void success(String roomId, String title) {
                Toast.makeText(DemoActivity.this, "Create Success : " + title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failed(String reason) {
                Toast.makeText(DemoActivity.this, "Create Failed : " + reason, Toast.LENGTH_SHORT).show();
            }
        }));

        btnShowRoomList.setOnClickListener(v ->
                IMKIT.showRoomList(DemoActivity.this, null, R.id.container, 7000));
//                IMKIT.showRoomList(DemoActivity.this, getSupportFragmentManager(), R.id.container, 7000));

        btnEnterRoom.setOnClickListener(v -> IMKIT.showChat(DemoActivity.this, edRoomId.getText().toString(), edRoomTitle.getText().toString(), 7000));

        btnRoomInfo.setOnClickListener(v -> IMKIT.showRoomInfo(DemoActivity.this, edRoomId.getText().toString(), edRoomTitle.getText().toString(), 7000, new IIMKIT.RoomInfo() {
            @Override
            public void success() {
                Toast.makeText(DemoActivity.this, "Fetch Info Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failed(String reason) {
                Toast.makeText(DemoActivity.this, "Fetch Info Failed : " + reason, Toast.LENGTH_SHORT).show();
            }
        }));

        btnSignOut.setEnabled(false);
        btnSignOut.setOnClickListener(v -> IMKIT.logout(() -> {
            edUserName.setText("");
            edUserName.setEnabled(true);
            btnLogin.setEnabled(true);
            btnSignOut.setEnabled(false);
        }));
    }
}
