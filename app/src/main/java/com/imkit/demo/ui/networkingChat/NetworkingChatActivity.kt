package com.imkit.demo.ui.networkingChat

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.imkit.demo.R
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityNetworkingChatBinding
import com.imkit.sdk.IMKit
import com.imkit.widget.IMWidgetPreferences

class NetworkingChatActivity : AppCompatActivity() {

    companion object {
        const val ROOM_ID = "roomId"
        const val ROOM_TITLE = "roomTitle"
    }

    private lateinit var binding: ActivityNetworkingChatBinding
    private lateinit var roomId: String
    private lateinit var roomTitle: String

    private val colorAccent by lazy {
        ContextCompat.getColor(this, R.color.color_style3_accent)
    }

    private val colorText by lazy {
        ContextCompat.getColor(this, R.color.color_style3_text)
    }

    private val colorBlack by lazy {
        ContextCompat.getColor(this, R.color.colorBlack)
    }

    private val colorWhite by lazy {
        ContextCompat.getColor(this, R.color.colorWhite)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkingChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UIUtils.setFullScreen(window,Color.WHITE)

        roomId = intent.getStringExtra(ROOM_ID) ?: return
        roomTitle = intent.getStringExtra(ROOM_TITLE) ?: return
        val room = IMKit.instance().getRoom(roomId)

        binding.toolbar.apply {
            root.setBackgroundColor(colorWhite)
            tvBack.setOnClickListener { finish() }
            val user = room.user
            room.members.firstOrNull {
                it.id != user
            }?.also { member ->
                Glide.with(imgAvatar).load(member.avatarUrl).transform(CircleCrop()).into(imgAvatar)
                tvTitle.text = member.nickname
                tvInfo.text = member.description
                tvInfo.visibility =
                    if (TextUtils.isEmpty(member.description)) View.GONE else View.VISIBLE
            }
        }

        IMKit.instance().chatRoomType = IMKit.ChatRoomType.TYPE_3
        IMWidgetPreferences.getInstance().also {
            it.leftBubbleDrawableRes = R.drawable.chat_msg_receive_style3
            it.rightBubbleDrawableRes = R.drawable.chat_msg_send_style3
            it.incomingMessageTextColor = colorBlack
            it.outgoingTextColor = colorBlack
            it.chatRoomBackgroundColor = Color.WHITE
            it.buttonTint = colorText
            it.sendButtonTint = colorText
            it.isSendPhotoEnabled = true
            it.isSendVoiceEnabled = true
            it.isSendStickerEnabled = false
            it.isSendVideoEnabled = false
            it.isSendLocationEnabled = false
            it.isSendFileEnabled = false
            it.isRecordVideoEnabled = false
            it.stickerAssetsPath = "stickers"
        }

        val chatFragment = CustomChatFragment.newInstance(roomId, roomTitle)
        supportFragmentManager.beginTransaction()
            .add(R.id.container, chatFragment, chatFragment::class.simpleName).commit()
    }
}