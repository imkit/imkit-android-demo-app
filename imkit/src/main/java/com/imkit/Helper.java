package com.imkit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.imkit.sdk.ApiResponse;
import com.imkit.sdk.IMKit;
import com.imkit.sdk.IMRestCallback;
import com.imkit.sdk.model.Room;

import java.util.Locale;
import java.util.UUID;

import okhttp3.MediaType;
import retrofit2.Call;

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

    public static String getDeviceId(Activity activity) {
        final String generalAndroidId = "9774d56d682e549c";
        String deviceId = getSavedDeviceId(activity);
        if (deviceId == null || deviceId.isEmpty()) {
            String uniqueID = "";
            try {
                uniqueID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch(Exception e) {
                Log.e(TAG, "getDeviceId error", e);
            }
            if (TextUtils.isEmpty(uniqueID) || uniqueID.equals(generalAndroidId)) {
                uniqueID = UUID.randomUUID().toString();
            }
            saveDeviceId(activity, Build.BRAND + "-" + uniqueID);
            deviceId = uniqueID;
        }

        return deviceId;
    }

    private static final String PREF_APP = "imkit";
    private static final String KEY_DEVICE_ID = "deviceId";

    static String getSavedDeviceId(@NonNull Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);

        return preferences.getString(KEY_DEVICE_ID, null);
    }

    static void saveDeviceId(@NonNull Context context, @NonNull String uniqueID) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DEVICE_ID, uniqueID);

        editor.apply();
    }
}
