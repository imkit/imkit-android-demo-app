package com.imkit.demo.ui.navigation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.imkit.demo.DemoUtils
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityInfoBinding
import com.imkit.demo.ui.businessChat.BusinessChatListActivity
import com.imkit.demo.ui.chatInBanking.ChatInBankingListActivity
import com.imkit.demo.ui.networkingChat.NetworkingChatListActivity
import com.imkit.demo.ui.tradingPlatform.ProductActivity
import com.imkit.sdk.IMKit
import com.imkit.sdk.IMRestCallback
import com.imkit.sdk.model.Room

class NavigateInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var demoType: DemoUtils.Type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UIUtils.setFullScreen(window) //full screen

        demoType = intent?.let {
            val ordinal = it.getIntExtra(DemoUtils.DEMO_TYPE, 0)
            DemoUtils.Type.values()[ordinal]
        } ?: throw IllegalArgumentException()

        binding.toolbar.tvTitle.setText(demoType.title)
        binding.toolbar.tvBack.setOnClickListener { finish() }
        binding.tvInfo.setText(demoType.info)
        binding.btnNext.setOnClickListener {
            when (demoType) {
                DemoUtils.Type.TRADING_PLATFORM -> {
                    startActivity(Intent(this, ProductActivity::class.java))
                }
                DemoUtils.Type.CHAT_IN_BACKING -> {
//                    IMKit.instance().createRoomWithUser("official_account_id");
                    IMKit.instance().createRoomWithUser(
                        "official_account_id",
                        object : IMRestCallback<Room?>() {
                            override fun onResult(result: Room?) {
                                IMKit.instance().createRoomWithUser(
                                    "coco_id",
                                    object : IMRestCallback<Room?>() {
                                        override fun onResult(result: Room?) {
                                            IMKit.instance().createRoomWithUser(
                                                "lora_id",
                                                object : IMRestCallback<Room?>() {
                                                    override fun onResult(result: Room?) {
                                                        IMKit.instance().createRoomWithUser(
                                                            "charle_id",
                                                            object : IMRestCallback<Room?>() {
                                                                override fun onResult(result: Room?) {
                                                                    startActivity(
                                                                        Intent(
                                                                            this@NavigateInfoActivity,
                                                                            ChatInBankingListActivity::class.java
                                                                        )
                                                                    )
                                                                    finish()
                                                                }
                                                            })
                                                    }
                                                })
                                        }
                                    })
                            }
                        })

//                    startActivity(
//                        Intent(
//                            this@NavigateInfoActivity,
//                            ChatInBankingListActivity::class.java
//                        )
//                    )
//                    finish()
                }
                DemoUtils.Type.NETWORKING_CHAT -> {
                    startActivity(Intent(this, NetworkingChatListActivity::class.java))
                    finish()
                }
                DemoUtils.Type.BUSINESS_CHAT -> {
                    startActivity(Intent(this, BusinessChatListActivity::class.java))
                    finish()
                }
            }
        }
    }
}