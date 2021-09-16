package com.imkit.demo.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.imkit.demo.R
import com.imkit.demo.UIUtils
import com.imkit.demo.databinding.ActivityAvatarBinding
import com.imkit.demo.ui.navigation.NavigateActivity

class AvatarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAvatarBinding
    private val avatarAdapter = AvatarAdapter()
    private val avatarList = listOf(
        R.drawable.ic_avatar_woman,
        R.drawable.ic_avatar_woman2,
        R.drawable.ic_avatar_man
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvatarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            adapter = avatarAdapter
            layoutManager =
                StaggeredGridLayoutManager(avatarList.size, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, NavigateActivity::class.java))
            finish()
        }

        avatarAdapter.setAvatarList(avatarList)

        UIUtils.setFullScreen(window) //full screen
    }
}

class AvatarAdapter : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    private val avatarList = ArrayList<Int>()
    private var selectIndex = 0

    fun setAvatarList(list: List<Int>) {
        avatarList.clear()
        avatarList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_avatar, null, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.avatar.setImageResource(avatarList[position])
        holder.select.visibility = if (position == selectIndex) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            val tag = it.tag as Int
            selectIndex = tag
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return avatarList.size
    }

    class AvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val select: ImageView = itemView.findViewById(R.id.imgSelect)
    }
}