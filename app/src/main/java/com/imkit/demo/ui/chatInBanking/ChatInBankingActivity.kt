package com.imkit.demo.ui.chatInBanking

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.imkit.demo.DemoUtils.Companion.MESSAGE_TYPE_REQUEST
import com.imkit.demo.DemoUtils.Companion.MESSAGE_TYPE_TRANSFER
import com.imkit.demo.R
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityChatInBankingBinding
import com.imkit.sdk.IMKit
import com.imkit.sdk.model.Message
import com.imkit.widget.IMWidgetPreferences
import com.imkit.widget.utils.MessageDirection
import org.json.JSONObject


class ChatInBankingActivity : AppCompatActivity() {

    companion object {
        const val ROOM_ID = "roomId"
        const val ROOM_TITLE = "roomTitle"
    }

    private lateinit var binding: ActivityChatInBankingBinding

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
        binding = ActivityChatInBankingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UIUtils.setFullScreen(window, Color.WHITE)

        val roomId = intent.getStringExtra(ROOM_ID) ?: return
        val roomTitle = intent.getStringExtra(ROOM_TITLE) ?: return

        binding.toolbar.apply {
            root.setBackgroundColor(Color.WHITE)
            tvTitle.apply {
                text = roomTitle
                setTextColor(Color.parseColor("#F178B6"))
            }
            tvBack.apply {
                setTextColor(Color.parseColor("#F178B6"))
                for (drawable in compoundDrawablesRelative) {
                    if (drawable != null) {
                        drawable.mutate()
                        drawable.colorFilter =
                            PorterDuffColorFilter(
                                Color.parseColor("#F178B6"), PorterDuff.Mode.SRC_IN
                            )
                    }
                }
                setOnClickListener { finish() }
            }
        }


        IMKit.instance().chatRoomType = IMKit.ChatRoomType.ChatInBanking
        IMWidgetPreferences.getInstance().also {
            it.leftBubbleDrawableRes = R.drawable.im_bubble_left_type_chat_in_banking
            it.rightBubbleDrawableRes = R.drawable.im_bubble_right_type_chat_in_banking
            it.incomingMessageTextColor = colorBlack
            it.outgoingTextColor = colorBlack
            it.chatRoomBackgroundColor = Color.WHITE
            it.buttonTint = Color.parseColor("#F178B6")
            it.sendButtonTint = Color.parseColor("#F178B6")
            it.chatBackgroundColor = Color.parseColor("#FFF6FB")
//            it.isSendPhotoEnabled = false
//            it.isSendVoiceEnabled = false
//            it.isSendVideoEnabled = false
//            it.isSendLocationEnabled = false
//            it.isSendFileEnabled = true
            it.isSendStickerEnabled = true
            it.isRecordVideoEnabled = false
            it.stickerAssetsPath = "stickers"
            it.registerMessageViewFactory(
                MESSAGE_TYPE_TRANSFER,
                MessageDirection.received,
                ReceivedTransferMessageView.Factory()
            )
            it.registerMessageViewFactory(
                MESSAGE_TYPE_TRANSFER,
                MessageDirection.sent,
                SentTransferMessageView.Factory()
            )
            it.registerMessageViewFactory(
                MESSAGE_TYPE_REQUEST,
                MessageDirection.received,
                ReceivedRequestMessageView.Factory()
            )
            it.registerMessageViewFactory(
                MESSAGE_TYPE_REQUEST,
                MessageDirection.sent,
                SentRequestMessageView.Factory()
            )
        }

        val chatFragment = CustomChatFragment.newInstance(roomId, roomTitle)

        // 加入額外按鈕- Transfer
        chatFragment.addAdditionalChatType3Item(
            getString(R.string.chat_in_banking_transfer),
            R.drawable.ic_transfer
        ) {
            var message = Message()
            message.room = roomId
            message.message = "Transferring Money Succeed"
            message.type = MESSAGE_TYPE_TRANSFER
            val extra = JSONObject("{\"money\":199}")
            message.setExtra(extra)
            message = IMKit.instance().presendMessage(message)
            IMKit.instance().sendMessage(message)
        }

        // 加入額外按鈕- Request
        chatFragment.addAdditionalChatType3Item(
            getString(R.string.chat_in_banking_request),
            R.drawable.ic_request
        ) {
            var message = Message()
            message.room = roomId
            message.message = "Payment Requested"
            message.type = MESSAGE_TYPE_REQUEST
            val extra = JSONObject("{\"request\":991}")
            message.setExtra(extra)
            message = IMKit.instance().presendMessage(message)
            IMKit.instance().sendMessage(message)
        }


        supportFragmentManager.beginTransaction()
            .add(R.id.container, chatFragment, chatFragment::class.simpleName).commit()
    }
}