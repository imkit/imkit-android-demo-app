package com.imkit.demo.ui.login

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.imkit.demo.DemoUtils
import com.imkit.demo.R
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityLoginBinding
import com.imkit.sdk.ApiResponse
import com.imkit.sdk.IMKit
import com.imkit.sdk.IMRestCallback
import com.imkit.sdk.model.Client
import com.imkit.sdk.model.Room
import retrofit2.Call
import java.util.concurrent.atomic.AtomicInteger

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val demoUsersList = DemoUtils.USER.values()
    private val demoUsersCount = AtomicInteger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UIUtils.setFullScreen(window) //full screen

        binding.btnSignIn.setOnClickListener {
            val id = binding.edClientId.text.toString()
            if (id.isEmpty()) {
                Toast.makeText(this, "Empty client id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nickName = binding.edClientNickName.text.toString()
            if (nickName.isEmpty()) {
                Toast.makeText(this, "Empty client nickname", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            doLogin(id, nickName)
        }

        val clientId = DemoUtils.getClientId(this)
        val clientName = DemoUtils.getClientName(this)
        binding.edClientId.setText(clientId)
        binding.edClientNickName.setText(clientName)
        if (!DemoUtils.isLogin(this) || TextUtils.isEmpty(clientId) || TextUtils.isEmpty(clientName))
            return

        doLogin(clientId!!, clientName!!)
    }

    private fun doLogin(clientId: String, nickName: String) {
        binding.progress.visibility = View.VISIBLE
        IMKit.instance().connect(clientId, object : IMRestCallback<Client?>() {
            override fun onResult(result: Client?) {
                IMKit.instance()
                    .updateCurrentUserInfo(nickName, null, object : IMRestCallback<Client?>() {
                        override fun onResult(result: Client?) {
                            with(this@LoginActivity) {
                                DemoUtils.setLogin(this, true)
                                DemoUtils.setClientId(this, clientId)
                                DemoUtils.setClientName(this, nickName)
                                if (!DemoUtils.isInitial(this@LoginActivity)) {
                                    addDemoRoomList()
                                    return@with
                                }

                                openAvatar()
                            }

                        }
                    })
            }

            override fun onFailure(call: Call<ApiResponse<Client?>>, t: Throwable) {
                binding.progress.visibility = View.GONE
                DemoUtils.setLogin(this@LoginActivity, false)
                AlertDialog.Builder(this@LoginActivity)
                    .setTitle("IMKit Connect error")
                    .setMessage(t.message)
                    .setPositiveButton("OK", null)
                    .show()
            }
        })
    }

    private fun addDemoRoomList() {
        if (IMKit.instance().token == null || IMKit.instance().token.isEmpty()) {
            return
        }

        demoUsersCount.set(demoUsersList.size)
        demoUsersList.forEach { user ->
            val room = Room().apply {
                id = IMKit.instance().getDirectChatRoomId(user.userId)
                name = user.roomName
            }
            IMKit.instance()
                .createAndJoinRoom(
                    room,
                    user.userId,
                    false,
                    false,
                    object : IMRestCallback<Room>() {
                        override fun onResult(room: Room) {
                            val count = demoUsersCount.decrementAndGet()
                            if (count <= 0) {
                                DemoUtils.setInitial(this@LoginActivity, true)
                                openAvatar()
                            }
                        }

                        override fun onFailure(p0: Call<ApiResponse<Room>>, p1: Throwable) {
                            super.onFailure(p0, p1)
                            binding.progress.visibility = View.GONE
                            p1.message?.also {
                                Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
        }
    }

    private fun openAvatar() {
        startActivity(Intent(this, AvatarActivity::class.java))
        finish()
    }

    //adjust
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val rect = Rect()
                v.getGlobalVisibleRect(rect)
                if (!rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm?.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}