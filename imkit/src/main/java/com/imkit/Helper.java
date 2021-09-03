package com.imkit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.imkit.sdk.ApiResponse;
import com.imkit.sdk.IMKit;
import com.imkit.sdk.IMRestCallback;
import com.imkit.sdk.model.Client;
import com.imkit.sdk.model.Room;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import androidx.core.app.ActivityCompat;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;

import static android.content.Context.TELEPHONY_SERVICE;

class Helper {

    private static final String TAG = "Helper";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static void createAndJoinRoom(String roomName, String invitee, final IIMKIT.CreateRoomInner callback) {
        final String roomId = convertNameToId(roomName);
        final String inviteeId = convertNameToId(invitee);
        Room room = new Room();
        // If roomId is left empty or null, a unique room id will be assigned by Server
        room.setId(roomId);
        room.setName(roomName);
        room.setCover("");
        room.setDescription("Demo room " + roomName);
        IMKit.instance().createAndJoinRoom(room, inviteeId, false, IMKIT.GROUP_INVITATION_REQUIRED, new IMRestCallback<Room>() {
            @Override
            public void onResult(Room room) {
                callback.success(room);
            }

            @Override
            public void onFailure(Call<ApiResponse<Room>> call, Throwable t) {
                callback.failed(t.getMessage());
            }
        });
    }

    private static String convertNameToId(String name) {
        return name.toLowerCase(Locale.US).replaceAll("[^a-zA-Z0-9]+", "-");
    }

    // For demo purpose, you can implement your own unique device ID getter.
    static String getDeviceId(Activity activity) {

        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, 666);
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(Build.BRAND).append("-");

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
        String imei = "";
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imei = telephonyManager.getImei();
            } else {
                imei = telephonyManager.getDeviceId();
            }
            if (!TextUtils.isEmpty(imei)) {
                sb.append(imei);
                return sb.toString();
            }
        } catch(Exception e) {
            Log.e(TAG, "get IMEI error", e);
        }

        String serial = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
        } catch(Exception e) {
            Log.e(TAG, "get serial error", e);
        }
        sb.append(Build.MODEL).append("-").append("-").append(serial);
        return sb.toString();
    }
}
