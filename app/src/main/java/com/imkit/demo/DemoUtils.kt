package com.imkit.demo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.annotation.StringRes

class DemoUtils {

    companion object {
        public const val DEMO_TYPE = "demo_type"
        private const val INITIAL = "initial"
        private const val LOGIN = "login"
        private const val CLIENT_ID = "client_id"
        private const val CLIENT_NAME = "client_name"

        fun isLogin(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences("DEMO", MODE_PRIVATE)
            return sharedPreferences.getBoolean(LOGIN, false)
        }

        fun setLogin(context: Context, login: Boolean) {
            val sharedPreferences = context.getSharedPreferences("DEMO", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(LOGIN, login).apply()
        }

        fun getClientId(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("DEMO", MODE_PRIVATE)
            return sharedPreferences.getString(CLIENT_ID, null)
        }

        fun setClientId(context: Context, clientId: String) {
            val sharedPreferences = context.getSharedPreferences("DEMO", MODE_PRIVATE)
            sharedPreferences.edit().putString(CLIENT_ID, clientId).apply()
        }

        fun getClientName(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("DEMO", MODE_PRIVATE)
            return sharedPreferences.getString(CLIENT_NAME, null)
        }

        fun setClientName(context: Context, clientName: String) {
            val sharedPreferences = context.getSharedPreferences("DEMO", MODE_PRIVATE)
            sharedPreferences.edit().putString(CLIENT_NAME, clientName).apply()
        }

        fun isInitial(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences("DEMO", MODE_PRIVATE)
            return sharedPreferences.getBoolean(INITIAL, false)
        }

        fun setInitial(context: Context, initial: Boolean) {
            val sharedPreferences = context.getSharedPreferences("DEMO", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(INITIAL, initial).apply()
        }
    }

    enum class Type(@StringRes val title: Int, @StringRes val info: Int) {
        TRADING_PLATFORM(
            R.string.navigate_trading_platform,
            R.string.navigate_trading_platform_info
        ),
        CHAT_IN_BACKING(
            R.string.navigate_chat_in_banking,
            R.string.navigate_chat_in_banking_info
        ),
        NETWORKING_CHAT(
            R.string.navigate_networking_chat,
            R.string.navigate_networking_chat_info
        ),
        BUSINESS_CHAT(
            R.string.navigate_business_chat,
            R.string.navigate_business_chat_info
        )
    }

    enum class USER(val userId: String, val roomName: String) {
        COCO(
            "coco_id",
            "Coco"
        ),
        LORA(
            "lora_id",
            "Lora"
        ),
        CHARLE(
            "charle_id",
            "Charle"
        )
    }

}