package com.imkit.demo.ui.networkingChat

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.imkit.demo.R
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityNetworkingChatListBinding
import com.imkit.sdk.model.Room
import com.imkit.widget.IMWidgetPreferences
import com.imkit.widget.fragment.RoomListFragment
import com.imkit.widget.utils.Utils


class NetworkingChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNetworkingChatListBinding

    private val colorBlack by lazy {
        ContextCompat.getColor(this, R.color.colorBlack)
    }

    private val colorWhite by lazy {
        ContextCompat.getColor(this, R.color.colorWhite)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkingChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UIUtils.setFullScreen(window, Color.WHITE)

        binding.toolbar.apply {
            root.setBackgroundColor(colorWhite)
            tvTitle.apply {
                setText(R.string.navigate_networking_chat)
                setTextColor(colorBlack)
            }
            tvBack.apply {
                setOnClickListener { finish() }
            }
        }

        IMWidgetPreferences.getInstance().also {
            it.isRoomListSearchEnabled = false
            it.roomViewFactory = NetworkChatRoomView.Factory()
        }
        val roomListFragment = CustomRoomListFragment.newInstance(null)
        roomListFragment.setListener(object : RoomListFragment.RoomListFragmentListener {
            override fun onRoomSelect(room: Room?) {
                room?.also { select ->
                    val context = this@NetworkingChatListActivity
                    startActivity(
                        Intent(
                            context,
                            NetworkingChatActivity::class.java
                        ).apply {
                            putExtra(NetworkingChatActivity.ROOM_ID, select.id)
                            putExtra(
                                NetworkingChatActivity.ROOM_TITLE,
                                Utils.getDisplayRoomTitle(context, select)
                            )
                        }
                    )
                }
            }

            override fun onCreateRoomListEmptyView(p0: ViewGroup?): View {
                val padding = (60 * resources.displayMetrics.density).toInt()
                return LayoutInflater.from(this@NetworkingChatListActivity)
                    .inflate(com.imkit.widget.R.layout.im_room_list_empty, null, false).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        ).apply {
                            gravity = Gravity.CENTER
                            setMargins(0, 0, 0, padding)
                        }
                    }
            }

            override fun onRoomListSearchClick() {

            }
        })
        supportFragmentManager.beginTransaction()
            .add(R.id.container, roomListFragment, roomListFragment::class.simpleName).commit()
    }
}