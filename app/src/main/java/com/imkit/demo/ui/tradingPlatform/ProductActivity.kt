package com.imkit.demo.ui.tradingPlatform

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.imkit.demo.R
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityProductBinding
import com.imkit.sdk.IMKit
import com.imkit.sdk.IMRestCallback
import com.imkit.sdk.model.Room
import com.imkit.widget.utils.Utils

class ProductActivity : AppCompatActivity() {

    companion object {
        const val userId = "trading_platform_id"
        const val roomTitle = "Porsche 356 C"
    }

    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UIUtils.setFullScreen(window) //full screen

        binding.toolbar.tvTitle.setText(R.string.navigate_trading_platform)
        binding.toolbar.tvBack.setOnClickListener { finish() }
        binding.btnNext.setOnClickListener {
            if (IMKit.instance().token == null || IMKit.instance().token.isEmpty()) {
                return@setOnClickListener
            }

            val room = Room()
            val roomId = IMKit.instance().getDirectChatRoomId(userId)
            room.id = roomId
            room.name = roomTitle
            IMKit.instance()
                .createAndJoinRoom(
                    room,
                    userId,
                    false,
                    false,
                    object : IMRestCallback<Room>() {
                        override fun onResult(room: Room) {
                            startActivity(
                                Intent(
                                    this@ProductActivity,
                                    TradingPlatformActivity::class.java
                                ).apply {
                                    putExtra(TradingPlatformActivity.ROOM_ID, room.id)
                                    putExtra(
                                        TradingPlatformActivity.ROOM_TITLE,
                                        Utils.getDisplayRoomTitle(this@ProductActivity, room)
                                    )
                                })
                            finish()
                        }
                    })
        }
    }
}