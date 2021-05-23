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
import com.imkit.widget.recyclerview.RoomRecyclerViewHolder;
import com.imkit.widget.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Custom room item view demo. Not required
 */

public class RoomView extends RoomRecyclerViewHolder {

    private static final String TAG = "roomView";

    static public class Factory implements IMRoomViewHolder.Factory {
        @Override
        public RoomView instantiate(Context context, ViewGroup container, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.im_room_list_item, container, false);
            return new RoomView(itemView);
        }
    }

    public RoomView(View itemView) {
        super(itemView);
    }
}
