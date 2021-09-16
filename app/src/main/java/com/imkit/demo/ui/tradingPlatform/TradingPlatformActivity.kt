package com.imkit.demo.ui.tradingPlatform

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.imkit.demo.R
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityTradingPlatformBinding
import com.imkit.sdk.IMKit
import com.imkit.widget.IMWidgetPreferences


class TradingPlatformActivity : AppCompatActivity() {

    companion object {
        const val ROOM_ID = "roomId"
        const val ROOM_TITLE = "roomTitle"
    }

    private lateinit var binding: ActivityTradingPlatformBinding

    private val colorText by lazy {
        ContextCompat.getColor(this, R.color.color_style1_text)
    }

    private val colorBlack by lazy {
        ContextCompat.getColor(this, R.color.colorBlack)
    }

    private val colorGrey by lazy {
        ContextCompat.getColor(this, R.color.colorDarkGrey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTradingPlatformBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UIUtils.setFullScreen(window, Color.WHITE)

        val roomId = intent.getStringExtra(ROOM_ID) ?: return
        val roomTitle = intent.getStringExtra(ROOM_TITLE) ?: return

        binding.toolbar.apply {
            root.setBackgroundColor(Color.WHITE)
            tvTitle.apply {
                text = roomTitle
                setTextColor(colorBlack)
            }
            tvBack.apply {
                setTextColor(colorGrey)
                for (drawable in compoundDrawablesRelative) {
                    if (drawable != null) {
                        drawable.mutate()
                        drawable.colorFilter =
                            PorterDuffColorFilter(
                                colorGrey, PorterDuff.Mode.SRC_IN
                            )
                    }
                }
                setOnClickListener { finish() }
            }
        }

        IMKit.instance().chatRoomType = IMKit.ChatRoomType.TradingPlatform
        IMWidgetPreferences.getInstance().also {
            it.leftBubbleDrawableRes = R.drawable.im_bubble_left_type_trading_platform
            it.rightBubbleDrawableRes = R.drawable.im_bubble_right_type_trading_platform
            it.incomingMessageTextColor = colorBlack
            it.outgoingTextColor = colorBlack
            it.isSendPhotoEnabled = true
            it.chatRoomBackgroundColor = Color.WHITE
//            it.buttonTint = colorText
//            it.sendButtonTint = colorText
//            it.isSendVoiceEnabled = true
//            it.isSendStickerEnabled = false
//            it.isSendVideoEnabled = false
//            it.isSendLocationEnabled = false
//            it.isSendFileEnabled = false
//            it.isRecordVideoEnabled = false
//            it.stickerAssetsPath = "stickers"
        }

        val chatFragment = CustomChatFragment.newInstance(roomId, roomTitle)
        supportFragmentManager.beginTransaction()
            .add(R.id.container, chatFragment, chatFragment::class.simpleName).commit()
    }
}