package com.imkit.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.imkit.R;
import com.imkit.sdk.model.Message;
import com.imkit.widget.IMMessageViewHolder;
import com.imkit.widget.fragment.ChatFragment;

/**
 * Leave-Room Message View Sample
 */

public class LeaveRoomMessageView extends IMMessageViewHolder {

    static public class Factory implements IMMessageViewHolder.Factory {

        @Override
        public IMMessageViewHolder instantiate(Context context, ViewGroup container, int viewType, ChatFragment chatFragment) {
            return null;
        }

        @Override
        public IMMessageViewHolder instantiate(Context context, ViewGroup container, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.message_io_room, container, false);
            return new LeaveRoomMessageView(itemView);
        }
    }

    private TextView messageTextView;
    private ImageView iconView;

    public LeaveRoomMessageView(View itemView) {
        super(itemView);

        messageTextView = (TextView) itemView.findViewById(R.id.im_message_text);
        iconView = (ImageView) itemView.findViewById(R.id.im_message_icon);

        iconView.setImageResource(R.drawable.ic_person);
        iconView.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryDark));
    }

    @Override
    public void setMessage(Message message) {
        String name = message.getSender().getNickname();

        messageTextView.setText(itemView.getContext().getString(R.string.im_member_leave, name));
    }

    @Override
    public void setKeyword(String s) {

    }

    @Override
    public void setNumRead(int count) {

    }

    @Override
    public void setForwardVisibility(boolean isVisible) {

    }

    @Override
    public void setUrlPreview(String images, String title, String description) {

    }

    @Override
    public void setUrlPreview(Message message) {

    }
}
