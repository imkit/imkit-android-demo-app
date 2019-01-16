package com.imkit.customview;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imkit.sdk.IMKit;
import com.imkit.sdk.model.Client;
import com.imkit.sdk.model.Message;
import com.imkit.sdk.model.Room;
import com.imkit.widget.CircleImageView;
import com.imkit.widget.GlideApp;
import com.imkit.widget.IMGlideModule;
import com.imkit.widget.IMRoomViewHolder;
import com.imkit.widget.IMWidgetPreferences;
import com.imkit.widget.R;
import com.imkit.widget.daimajia.swipe.SwipeLayout;
import com.imkit.widget.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Custom room item view demo. Not required
 */

public class RoomView extends IMRoomViewHolder {

    private static final String TAG = "roomView";

    static public class Factory implements IMRoomViewHolder.Factory {
        @Override
        public RoomView instantiate(Context context, ViewGroup container, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.im_room_list_item, container, false);
            return new RoomView(itemView);
        }
    }

    SwipeLayout swipeLayout;
    View viewSwipeItem;
    View viewPin;
    ImageView imgPin;
    TextView txtPin;
    CircleImageView avatarImageView;
    TextView avatarPlaceholderTextView;
    ImageView imgPinStatus;
    TextView titleTextView;
    TextView subtitleTextView;
    TextView lastMessageTextView;
    TextView lastMessageTimeTextView;
    TextView badgeTextView;

    private Context mContext;
    private Room mRoom;

    public RoomView(View itemView) {
        super(itemView);

        mContext = itemView.getContext();
        bindViews();
    }

    private void bindViews() {
        swipeLayout = itemView.findViewById(R.id.swipeLayout);
        viewSwipeItem = itemView.findViewById(R.id.viewSwipeItem);
        viewPin = itemView.findViewById(R.id.viewPin);
        imgPin = itemView.findViewById(R.id.imgPin);
        txtPin = itemView.findViewById(R.id.txtPin);
        avatarImageView = (CircleImageView) itemView.findViewById(R.id.im_avatar);
        avatarPlaceholderTextView = (TextView) itemView.findViewById(R.id.im_avatar_placeholder);
        imgPinStatus = itemView.findViewById(R.id.imgPinStatus);
        titleTextView = (TextView) itemView.findViewById(R.id.im_room_title);
        subtitleTextView = (TextView) itemView.findViewById(R.id.im_room_subtitle);
        lastMessageTextView = (TextView) itemView.findViewById(R.id.im_room_last_message);
        lastMessageTimeTextView = (TextView) itemView.findViewById(R.id.im_room_last_message_time);
        badgeTextView = (TextView) itemView.findViewById(R.id.im_room_badge);
    }

    @Override
    public void setRoom(Room room) {
        mRoom = room;

        setSwipeLayout(room);
        setAvatar(room);
        setTitle(room);
        setSubTitle(room);
        setLastMessage(room);
        setBadge(room);
    }

    protected void setSwipeLayout(final Room room) {
//        swipeLayout.setSwipeEnabled(IMWidgetPreferences.getInstance().isRoomPinEnable());
        swipeLayout.setSwipeEnabled(false);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewSwipeItem);
        swipeLayout.setRightSwipeEnabled(false);
        swipeLayout.close(true);

        final long[] lastSwipe = {0L};
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                lastSwipe[0] = System.currentTimeMillis();
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }

            @Override
            public void onClose(SwipeLayout layout) {
                lastSwipe[0] = System.currentTimeMillis();
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
            }
        });

        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - lastSwipe[0] < 100) {
                    // do nothing
                } else {
                    if (getItemListener() != null) {
                        getItemListener().onClickItem(RoomView.this, room);
                    }
                }
            }
        });

        viewPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItemListener() != null) {
                    getItemListener().onClickItemPin(RoomView.this, room);
                }
            }
        });

        imgPin.setImageResource(room.getPriority() == 0 ? R.drawable.ic_pin : R.drawable.ic_unpin);
        txtPin.setText(room.getPriority() == 0 ? R.string.im_room_list_pin : R.string.im_room_list_unpin);
    }

    protected void setAvatar(Room room) {
        imgPinStatus.setVisibility((!IMWidgetPreferences.getInstance().isRoomPinEnable() || room == null || room.getPriority() == 0) ? View.GONE : View.VISIBLE);

        if (room == null) {
            avatarImageView.setImageBitmap(null);
            return;
        }
        if (!TextUtils.isEmpty(room.getCover())) {
            GlideApp.with(mContext).load(IMGlideModule.buildGlideUrl(room.getCover()))
                    .fitCenter().dontAnimate().into(avatarImageView);
            return;
        }

        Client currentClient = IMKit.instance().currentClient();
        if (room.getMembers() != null) {
            for (Client member : room.getMembers()) {
                if (currentClient != null && member.getId().equalsIgnoreCase(currentClient.getId())) {
                    continue;
                }
                if (!TextUtils.isEmpty(member.getAvatarUrl())) {
                    GlideApp.with(mContext).load(IMGlideModule.buildGlideUrl(member.getAvatarUrl()))
                            .fitCenter().dontAnimate().into(avatarImageView);
                }
                break;
            }
        }
    }

    protected void setTitle(Room room) {
        titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, (IMWidgetPreferences.getInstance().isRoomPinEnable() && room.getPriority() != 0) ? R.drawable.ic_pinned : 0, 0);

        String title = Utils.getDisplayRoomTitle(room);
        if (room.getMembers().size() > 3) {
            title = title + String.format(Locale.getDefault(), "(%d)", room.getMembers().size());
        }
        titleTextView.setText(title);

        if (room == null) {
            avatarPlaceholderTextView.setText("");
            return;
        }
        if (!TextUtils.isEmpty(room.getName())) {
            avatarPlaceholderTextView.setText(room.getName().substring(0, 1));
            return;
        }
        avatarPlaceholderTextView.setText("");
    }

    protected void setSubTitle(Room room) {
        Client currentClient = IMKit.instance().currentClient();
        StringBuilder sb = new StringBuilder();
        if (room.getMembers() != null) {
            for (Client member : room.getMembers()) {
                if (currentClient != null && member.getId().equalsIgnoreCase(currentClient.getId())) {
                    continue;
                }
                if (!TextUtils.isEmpty(member.getNickname())) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(member.getNickname());
                }
            }
            sb.append(" (").append(room.getMembers().size()).append(")");
        }
        if (!TextUtils.isEmpty(room.getDescription())) {
            sb.append('\n').append(room.getDescription());
        }

        subtitleTextView.setText(sb.toString());
    }

    protected void setLastMessage(Room room) {
        if (room == null || room.getLastMessage() == null) {
            lastMessageTextView.setText("");
            lastMessageTimeTextView.setText("");
            return;
        }
        Message lastMessage = mRoom.getLastMessage();

        CharSequence timeStr = DateUtils.getRelativeTimeSpanString(lastMessage.getMessageTimeMS());
        lastMessageTimeTextView.setText(timeStr);

        String nickname = "";
        if (lastMessage.getSender() != null && !TextUtils.isEmpty(lastMessage.getSender().getNickname())) {
            nickname = lastMessage.getSender().getNickname();
        }
        String messageType = lastMessage.getType();
        if (messageType.equalsIgnoreCase(Message.MESSAGE_TYPE_IMAGE)) {
            lastMessageTextView.setText(itemView.getContext().getString(R.string.im_sent_a_photo, nickname));
        } else if (messageType.equalsIgnoreCase(Message.MESSAGE_TYPE_VIDEO)) {
            lastMessageTextView.setText(itemView.getContext().getString(R.string.im_sent_a_video, nickname));
        } else if (messageType.equalsIgnoreCase(Message.MESSAGE_TYPE_STICKER)) {
            lastMessageTextView.setText(itemView.getContext().getString(R.string.im_sent_a_sticker, nickname));
        } else if (messageType.equalsIgnoreCase(Message.MESSAGE_TYPE_AUDIO)) {
            lastMessageTextView.setText(itemView.getContext().getString(R.string.im_sent_a_audio, nickname));
        } else if (messageType.equalsIgnoreCase(Message.MESSAGE_TYPE_LOCATION)) {
            lastMessageTextView.setText(itemView.getContext().getString(R.string.im_sent_a_location, nickname));
        } else if (messageType.equalsIgnoreCase(Message.MESSAGE_TYPE_FILE)) {
            lastMessageTextView.setText(itemView.getContext().getString(R.string.im_sent_a_file, nickname));
        } else if (messageType.equalsIgnoreCase(Message.MESSAGE_TYPE_ADD_MEMBERS)) {
            try {
                JSONArray array = new JSONArray(lastMessage.getMessage());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject member = array.getJSONObject(i);
                    if (member.has("nickname")) {
                        sb.append(member.getString("nickname")).append(", ");
                    }
                }
                if (sb.length() >= 2) {
                    sb.setLength(sb.length() - 2);
                }
                lastMessageTextView.setText(itemView.getContext().getString(R.string.im_add_members, nickname, sb.toString()));
            } catch (JSONException e) {
                Log.e(TAG, "setAddMembers", e);
            }
        } else {
            lastMessageTextView.setText(lastMessage.getMessage());
        }
    }

    protected void setBadge(Room room) {
        badgeTextView.setTextColor(IMWidgetPreferences.getInstance().getRoomBadgeTextColor());
        badgeTextView.setBackgroundResource(IMWidgetPreferences.getInstance().getRoomBadgeDrawableRes());
        if (room == null || room.getUnread() == 0) {
            badgeTextView.setText("0");
            badgeTextView.setVisibility(View.GONE);
//            itemView.setBackgroundColor(Color.parseColor( "#f9f9f9f9"));
            return;
        }
        badgeTextView.setVisibility(View.VISIBLE);
        badgeTextView.setText(String.format("%d", room.getUnread()));
        itemView.setBackgroundResource(R.drawable.im_clickable_item_bg);
    }
}
