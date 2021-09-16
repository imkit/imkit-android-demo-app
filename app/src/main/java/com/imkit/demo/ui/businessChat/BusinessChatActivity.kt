package com.imkit.demo.ui.businessChat

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.imkit.demo.R
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityBusinessChatBinding
import com.imkit.sdk.IMKit
import com.imkit.widget.IMWidgetPreferences


class BusinessChatActivity : AppCompatActivity() {

    companion object {
        const val ROOM_ID = "roomId"
        const val ROOM_TITLE = "roomTitle"
    }

    private lateinit var binding: ActivityBusinessChatBinding

    private val colorText by lazy {
        ContextCompat.getColor(this, R.color.color_style4_text)
    }

    private val colorDarkGreen by lazy {
        ContextCompat.getColor(this, R.color.colorDarkGreen)
    }

    private val colorBlack by lazy {
        ContextCompat.getColor(this, R.color.colorBlack)
    }

    private val colorWhite by lazy {
        ContextCompat.getColor(this, R.color.colorWhite)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UIUtils.setFullScreen(window, colorText)

        val roomId = intent.getStringExtra(ROOM_ID) ?: return
        val roomTitle = intent.getStringExtra(ROOM_TITLE) ?: return

        binding.toolbar.apply {
            root.setBackgroundColor(colorText)
            tvTitle.apply {
                text = roomTitle
                setTextColor(colorWhite)
            }
            tvBack.apply {
                text = null
                setTextColor(colorWhite)
                for (drawable in compoundDrawablesRelative) {
                    if (drawable != null) {
                        drawable.mutate()
                        drawable.colorFilter =
                            PorterDuffColorFilter(
                                colorWhite, PorterDuff.Mode.SRC_IN
                            )
                    }
                }
                setOnClickListener { finish() }
            }
        }

        IMKit.instance().chatRoomType = IMKit.ChatRoomType.TYPE_3
        IMWidgetPreferences.getInstance().also {
            it.leftBubbleDrawableRes = R.drawable.chat_msg_receive_style4
            it.rightBubbleDrawableRes = R.drawable.chat_msg_send_style4
            it.incomingMessageTextColor = colorBlack
            it.outgoingTextColor = colorBlack
            it.chatRoomBackgroundColor = Color.WHITE
            it.buttonTint = colorDarkGreen
            it.sendButtonTint = colorDarkGreen
            it.isSendPhotoEnabled = true
            it.isSendVoiceEnabled = true
            it.isSendStickerEnabled = true
            it.isSendVideoEnabled = false
            it.isSendLocationEnabled = false
            it.isSendFileEnabled = true
            it.isRecordVideoEnabled = false
            it.stickerAssetsPath = "stickers"
        }

        val chatFragment = CustomChatFragment.newInstance(roomId, roomTitle)
        supportFragmentManager.beginTransaction()
            .add(R.id.container, chatFragment, chatFragment::class.simpleName).commit()
    }
}