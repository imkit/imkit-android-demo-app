package com.imkit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imkit.customview.JoinRoomMessageView;
import com.imkit.customview.LeaveRoomMessageView;
import com.imkit.customview.RoomView;
import com.imkit.sdk.ApiResponse;
import com.imkit.sdk.IMKit;
import com.imkit.sdk.IMRestCallback;
import com.imkit.sdk.model.Badge;
import com.imkit.sdk.model.Client;
import com.imkit.sdk.model.Room;
import com.imkit.widget.IMMessageViewHolder;
import com.imkit.widget.IMWidgetPreferences;
import com.imkit.widget.fragment.RoomListFragment;
import com.imkit.widget.utils.Utils;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Response;

public class IMKIT {

    private static String currentActiveRoomID = "";

    // Experimental feature
    public static final boolean GROUP_INVITATION_REQUIRED = false;

    public static final boolean ENABLE_ROOMLIST_DISPLAY_HOME = false;
    public static final String TOOLBAR_BACKGROUND_COLOR = "#0088D2";
    public static final String TOOLBAR_HOME_COLOR = "#FFFFFF";

    public static void init(Context context, String url, String clientKey, String bucketName, String providerAuthority) {
        IMKit.setDebugLog(false);

        IMKit.instance()
                .setUrl(url)
                .setClientKey(clientKey)
                .setBucketName(bucketName)
                .init(context);

        /**
         *  1. Demo set provider authority, please replace the authority string regarding to
         *     your AndroidManifest.xml
         *  2. Add the following entries to the FILE_PROVIDER_PATHS XML
         *      <external-path path="Download/IMKit" name="imkit" />
         *      <files-path name="photos" path="pictures"/>
         *      <files-path name="videos" path="movies"/>
         *      <files-path name="audios" path="audios"/>
         *      <files-path name="docs" path="files"/>
         */
        IMKit.instance().setProviderAuthority(providerAuthority);

        // IMKit Widget Style
        IMWidgetPreferences.getInstance().setLeftBubbleDrawableRes(R.drawable.im_bubble_left);
        IMWidgetPreferences.getInstance().setRightBubbleDrawableRes(R.drawable.im_bubble_right);
        IMWidgetPreferences.getInstance().setRoomBadgeDrawableRes(R.drawable.im_badge_bg);
        IMWidgetPreferences.getInstance().setIncomingMessageTextColor(ContextCompat.getColor(context, R.color.im_incoming_text_color));
        IMWidgetPreferences.getInstance().setOutgoingTextColor(ContextCompat.getColor(context, R.color.im_outgoing_text_color));
        IMWidgetPreferences.getInstance().setMessageReadColor(ContextCompat.getColor(context, R.color.im_message_read_text_color));
        IMWidgetPreferences.getInstance().setMessageTimeColor(ContextCompat.getColor(context, R.color.im_message_time_text_color));
        IMWidgetPreferences.getInstance().setRoomBadgeTextColor(ContextCompat.getColor(context, R.color.im_badge));
        IMWidgetPreferences.getInstance().setButtonTint(ContextCompat.getColor(context, R.color.im_button_tint));
        IMWidgetPreferences.getInstance().setSendButtonTint(ContextCompat.getColor(context, R.color.im_send_button_tint));
        IMWidgetPreferences.getInstance().setMessageControlButtonTint(ContextCompat.getColor(context, R.color.im_msg_button_tint));
        IMWidgetPreferences.getInstance().setChatRoomBackgroundColor(Color.WHITE);

        // Stickers
        IMWidgetPreferences.getInstance().setStickerAssetsPath("stickers");

        // Toggle share-media features in chat
        IMWidgetPreferences.getInstance().setSendStickerEnabled(true);
        IMWidgetPreferences.getInstance().setSendPhotoEnabled(true);
        IMWidgetPreferences.getInstance().setSendVideoEnabled(false);
        IMWidgetPreferences.getInstance().setSendVoiceEnabled(true);
        IMWidgetPreferences.getInstance().setSendLocationEnabled(true);
        IMWidgetPreferences.getInstance().setSendFileEnabled(true);
        IMWidgetPreferences.getInstance().setRecordVideoEnabled(false);

        // Custom Room View
        IMWidgetPreferences.getInstance().setRoomViewFactory(new RoomView.Factory());

        // Custom item decorations
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.im_item_decoration));
        // Set room list item decoration
//        IMWidgetPreferences.getInstance().setRoomItemDecoration(dividerItemDecoration);
        // Set member list item decoration
//        IMWidgetPreferences.getInstance().setRoomMemberItemDecoration(dividerItemDecoration);

        // Custom Message View
        IMWidgetPreferences.getInstance().registerMessageViewFactory(IMMessageViewHolder.ITEM_VIEW_TYPE_JOIN_ROOM, new JoinRoomMessageView.Factory());
        IMWidgetPreferences.getInstance().registerMessageViewFactory(IMMessageViewHolder.ITEM_VIEW_TYPE_LEAVE_ROOM, new LeaveRoomMessageView.Factory());

        IMKit.instance().setSSLTrustAll(true);
//        IMKit.instance().setHoldToRecordVoice(true);
        // Chat server connect and API request timeout, in milliseconds.
        IMKit.instance().setTimeout(120000);

        IMKit.instance().setChatRoomType(IMKit.ChatRoomType.TYPE_3);
        IMKit.instance().setRoomInfoType(IMKit.RoomInfoType.TYPE_2);
        IMKit.instance().setLocationFullMap(true);

        IMWidgetPreferences.getInstance().setShowTyping(true);

        IMWidgetPreferences.getInstance().setRoomPinEnable(true);

        IMWidgetPreferences.getInstance().setMultipleForward(true);
    }

    /**
     * Connect chat server with name, for demo purpose. Not recommend for production usage.
     * @param activity
     * @param uid   User ID
     * @param nickname
     * @param callback
     * @see {@link #login(String, String, String, IIMKIT.Login)} for production implementation
     */
    public static void login(Activity activity, String uid, String nickname, final IIMKIT.Login callback) {
        // For demo purpose, you can implement your own unique device ID getter.
        IMKit.instance().setUniqueDeviceId(Helper.getDeviceId(activity));

        // Clear cache
        IMKit.instance().clear();

        IMKit.instance().connect(uid, new IMRestCallback<Client>() {
            @Override
            public void onResult(Client client) {
                IMKit.instance().updateCurrentUserInfo(nickname /* display nickname */, null /* Avatar URL */, new IMRestCallback<Client>() {
                    @Override
                    public void onResult(Client client) {
                        callback.success();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Client>> call, Throwable throwable) {
                        callback.failed(throwable.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Client>> call, Throwable throwable) {
                callback.failed(throwable.getMessage());
            }
        });

    }

    public static void logout(IIMKIT.Logout callback) {
        IMKit.instance().unsubscribe(null);
        IMKit.instance().clear();
        IMKit.instance().setToken("");

        callback.done();
    }

    public static void createRoom(final Activity activity, final boolean autoEnter, final int requestCode, final IIMKIT.CreateRoom callback) {

        final IIMKIT.CreateRoomInner createRoomCallback = new IIMKIT.CreateRoomInner() {
            @Override
            public void success(Room room) {
                callback.success(room.getId(), Utils.getDisplayRoomTitle(activity, room));
                if (autoEnter) {
                    IMKIT.showChat(activity, room.getId(), Utils.getDisplayRoomTitle(activity, room), requestCode);
                }
            }

            @Override
            public void failed(String reason) {
                callback.failed(reason);
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Create/Join room");

        // Set up the input
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_create_room, null, false);
        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                TextView roomNameTextView = alertDialog.findViewById(R.id.dialog_create_room_name);
                String roomName = roomNameTextView.getText().toString();
                TextView inviteeTextView = alertDialog.findViewById(R.id.dialog_create_room_invitee);
                String invitee = inviteeTextView.getText().toString();
                if (!TextUtils.isEmpty(roomName)) {
                    Helper.createAndJoinRoom(roomName, invitee, createRoomCallback);
                } else {
                    createRoomCallback.failed("empty room name");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public static void showRoomList(final Activity activity, final FragmentManager fragmentManager, final int fragmentContainerId, int requestCode) {
        if (fragmentManager != null) {
            final CustomRoomListFragment fragment = CustomRoomListFragment.newInstance();
            fragment.setListener(new RoomListFragment.RoomListFragmentListener() {

                @Override
                public void onRoomSelect(Room room) {
                    IMKIT.showChat(activity, room.getId(), Utils.getDisplayRoomTitle(activity, room), 7000);
                }

                @Override
                public View onCreateRoomListEmptyView(ViewGroup parent) {
                    View view = LayoutInflater.from(activity).inflate(com.imkit.widget.R.layout.im_room_list_empty, parent, false);
                    view.findViewById(R.id.txtCreateRoom).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fragment.onCreateRoomClicked();
                        }
                    });
                    return view;
                }

                @Override
                public void onRoomListSearchClick() {

                }
            });
            fragmentManager.beginTransaction().add(fragmentContainerId, fragment, fragment.getClass().getSimpleName()).commit();
        } else {
            activity.startActivityForResult(new Intent(activity, RoomListActivity.class), requestCode);
        }
    }

    public static void showChat(Activity activity, String roomId, String title, int requestCode) {
        if (title.isEmpty()) {
            IMKit.instance().getRoom(roomId, new IMRestCallback<Room>() {
                @Override
                public void onResult(Room room) {
                    Intent intent = new Intent(activity, ChatActivity.class);
                    intent.putExtra("roomId", room.getId());
                    intent.putExtra("title", Utils.getDisplayRoomTitle(activity, room));
                    activity.startActivityForResult(intent, requestCode);
                }
            });
        } else {
            Intent intent = new Intent(activity, ChatActivity.class);
            intent.putExtra("roomId", roomId);
            intent.putExtra("title", title);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void showRoomInfo(final Activity activity, final String roomId, final String title, final int requestCode, final IIMKIT.RoomInfo callback) {
        Room room = IMKit.instance().getRoom(roomId);
        if (room == null) {
            callback.failed("Room Id not found");
        } else {
            callback.success();

            Intent intent = new Intent(activity, RoomInfoActivity.class);
            intent.putExtra("roomId", roomId);
            intent.putExtra("title", title);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * Login with accessToken. Suggested for production usage.
     * @param accessToken
     * @param userDisplayName
     * @param userAvatarUrl
     * @param callback
     */
    public static void login(String accessToken, String userDisplayName, String userAvatarUrl, final IIMKIT.Login callback) {
        // Clear cache
        IMKit.instance().clear();
        IMKit.instance().connect(null, accessToken, new IMRestCallback<Client>() {
            @Override
            public void onResult(Client client) {
                IMKit.instance().updateCurrentUserInfo(userDisplayName, userAvatarUrl, new IMRestCallback<Client>() {
                    @Override
                    public void onResult(Client client) {
                        callback.success();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Client>> call, Throwable throwable) {
                        callback.failed(throwable.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Client>> call, Throwable throwable) {
                callback.failed(throwable.getMessage());
            }
        });
    }

    public static void refreshToken(String accessToken) {
        IMKit.instance().setToken(accessToken);
        IMKit.instance().addExtraImageRequestHeader("AccessToken", accessToken);
    }

    public static String getToken() {
        return IMKit.instance().getToken();
    }

    public static void updateUser(String userDisplayName, String userAvatarUrl, String userDescription, final IIMKIT.UpdateUser callback) {
        if (IMKit.instance().getToken() == null || IMKit.instance().getToken().isEmpty()) {
            callback.failed("no token found");
            return;
        }

        Client client = new Client();
//        client.setId();
        client.setNickname(userDisplayName);
        client.setAvatarUrl(userAvatarUrl);
        client.setDescription(userDescription);

        IMKit.instance().updateMe(client, new IMRestCallback<Client>() {
            @Override
            public void onResult(Client client) {
                callback.success();
            }

            @Override
            public void onResponse(Call<ApiResponse<Client>> call, Response<ApiResponse<Client>> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ApiResponse<Client>> call, Throwable throwable) {
                super.onFailure(call, throwable);
                callback.failed(throwable.getMessage());
            }

            @Override
            public IMRestCallback<Client> next(IMRestCallback<Client> imRestCallback) {
                return super.next(imRestCallback);
            }
        });
    }

    //多人聊天室 (2人以上，會有邀請訊息)
    public static void createRoomWithUsers(Context context, ArrayList<String> userIds, final IIMKIT.CreateRoom callback) {
        if (IMKit.instance().getToken() == null || IMKit.instance().getToken().isEmpty()) {
            callback.failed("no token found");
            return;
        }

        Room room = new Room();
        IMKit.instance().createAndJoinRoom(room, userIds, true, GROUP_INVITATION_REQUIRED, new IMRestCallback<Room>() {
            @Override
            public void onResult(Room room) {
                callback.success(room.getId(), Utils.getDisplayRoomTitle(context, room));
            }
        });
    }

    //兩人聊天室 (不會有邀請訊息)
    public static void createRoomWithUser(Context context, String userId, final IIMKIT.CreateRoom callback) {
        if (IMKit.instance().getToken() == null || IMKit.instance().getToken().isEmpty()) {
            callback.failed("no token found");
            return;
        }

        Room room = new Room();
        String roomId = IMKit.instance().getDirectChatRoomId(userId);
        room.setId(roomId);
        IMKit.instance().createAndJoinRoom(room, userId, false, false, new IMRestCallback<Room>() {
            @Override
            public void onResult(Room room) {
                callback.success(room.getId(), Utils.getDisplayRoomTitle(context, room));
            }
        });
    }

    public static void getBadge(final IIMKIT.Badge callback) {
        IMKit.instance().getBadge(new IMRestCallback<Badge>() {
            @Override
            public void onResult(Badge result) {
                if (callback == null) {
                    return;
                }
                if (result != null) {
                    callback.response(result.getBadge());
                } else {
                    callback.response(0);
                }
            }
        });
    }

    public static String getCurrentActiveRoomID() {
        return currentActiveRoomID;
    }

    static void setCurrentActiveRoomID(String roomID) {
        IMKIT.currentActiveRoomID = roomID;
    }
}