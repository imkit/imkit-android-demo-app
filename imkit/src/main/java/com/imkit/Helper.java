package com.imkit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.imkit.sdk.ApiResponse;
import com.imkit.sdk.IMKit;
import com.imkit.sdk.IMRestCallback;
import com.imkit.sdk.model.Client;
import com.imkit.sdk.model.Room;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;

import static android.content.Context.TELEPHONY_SERVICE;

class Helper {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static String getClientId(String name) {
        return name.replaceAll("[^a-zA-Z0-9]+", "-");
    }

    static void authWithBindToken(Context context, final String name, String deviceId, final IIMKIT.Login callback) {
        IMKit.instance().setApiKey(context.getString(R.string.IMKIT_API_KEY));
        Client client = new Client();
        client.setId(getClientId(name));
        client.setNickname(name);
        client.setDescription(deviceId);
        IMKit.instance().createClient(client, new IMRestCallback<Client>() {
            @Override
            public void onResult(Client result) {
                String token = UUID.randomUUID().toString();
                IMKit.instance().bindTokenToClient(result.getId(), token, new IMRestCallback<Map<String, Object>>() {
                    @Override
                    public void onResult(Map<String, Object> result) {
                        IMKit.instance().setApiKey(null);
                        if (callback != null) {
                            callback.success();
                        }
                    }
                });
            }
        });
    }

    static void authWithExternalServer(final Context context, final String name, final IIMKIT.Login callback) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                String token = null;
                JSONObject json = new JSONObject();
                try {
                    json.put("id", getClientId(name));
                    json.put("nickname", name);

                    String authServerUrl = context.getString(R.string.IMKIT_AUTH_URL);
                    if (!authServerUrl.endsWith("/")) {
                        authServerUrl += "/";
                    }

                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .url(authServerUrl + "sign")
                            .post(body)
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();
                    String content = response.body() != null ? response.body().string() : null;
                    JSONObject result = new JSONObject(content);
                    token = result.getString("token");
                } catch (IOException e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.failed(e.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.failed(e.getMessage());
                    }
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                IMKit.instance().setToken(token);

                Client client = new Client();
                client.setNickname(name);
                client.setAvatarUrl("");
                client.setDescription("description la la #" + System.currentTimeMillis());
                IMKit.instance().updateMe(client, new IMRestCallback<Client>() {
                    @Override
                    public void onResult(Client result) {
                        if (callback != null) {
                            callback.success();
                        }
                    }
                });

            }
        };

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    static void createAndJoinRoom(String roomName, String invitee, final IIMKIT.CreateRoomInner callback) {
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

    static String getRoomId(ArrayList<String> userIds, String clientId) {
        ArrayList<String> temp = new ArrayList<>();
        for (String id : userIds) {
            temp.add(id);
        }
        temp.add(clientId);

        Comparator comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        Collections.sort(temp, comparator);

        StringBuilder stringBuilder = new StringBuilder();
        for (String id : temp) {
            stringBuilder.append(id);
            stringBuilder.append("-");
        }

        return stringBuilder.toString();
    }

    static String getRoomId(String userId, String clientId) {
        if (userId.compareTo(clientId) < 0) {
            return convertNameToId(userId) + "&" + convertNameToId(clientId);
        } else {
            return convertNameToId(clientId) + "&" + convertNameToId(userId);
        }
    }

    static String getDeviceId(Activity activity) {

        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, 666);
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(Build.BRAND).append("-");

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
        String imei = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            imei = telephonyManager.getImei();
        } else {
            imei = telephonyManager.getDeviceId();
        }
        if (!TextUtils.isEmpty(imei)) {
            sb.append(imei);
            return sb.toString();
        }

        String serial;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            serial = Build.getSerial();
        } else {
            serial = Build.SERIAL;
        }
        sb.append(Build.MODEL).append("-").append("-").append(serial);
        return sb.toString();
    }
}
