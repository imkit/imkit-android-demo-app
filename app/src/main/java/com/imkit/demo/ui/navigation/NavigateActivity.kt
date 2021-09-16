package com.imkit.demo.ui.navigation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.imkit.demo.DemoUtils
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityNavigateBinding
import com.imkit.demo.ui.login.LoginActivity
import com.imkit.sdk.IMKit

class NavigateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStyle1.setOnClickListener {
            openNavigateInfo(DemoUtils.Type.TRADING_PLATFORM)
        }

        binding.btnStyle2.setOnClickListener {
            openNavigateInfo(DemoUtils.Type.CHAT_IN_BACKING)
        }

        binding.btnStyle3.setOnClickListener {
            openNavigateInfo(DemoUtils.Type.NETWORKING_CHAT)
        }

        binding.btnStyle4.setOnClickListener {
            openNavigateInfo(DemoUtils.Type.BUSINESS_CHAT)
        }

        binding.btnExit.setOnClickListener {
            IMKit.instance().run {
                unsubscribe(null)
                clear()
                token = ""
            }
            DemoUtils.setLogin(this, false)
            DemoUtils.setInitial(this, false)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.tvVersion.text = String.format(
            "SDK v%1\$s.%2\$s",
            com.imkit.sdk.BuildConfig.VERSION_NAME,
            com.imkit.sdk.BuildConfig.VERSION_CODE
        )
        UIUtils.setFullScreen(window) //full screen
    }

    private fun openNavigateInfo(type: DemoUtils.Type) {
        startActivity(Intent(this, NavigateInfoActivity::class.java).apply {
            putExtra(DemoUtils.DEMO_TYPE, type.ordinal)
        })
    }
}