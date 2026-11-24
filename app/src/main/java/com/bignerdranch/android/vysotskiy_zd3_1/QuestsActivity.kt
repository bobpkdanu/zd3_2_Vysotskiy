package com.bignerdranch.android.vysotskiy_zd3_1

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class QuestsActivity : FragmentActivity() {
    private val channels = listOf(
        "Action" to "action",
        "Comedy" to "comedy",
        "Drama" to "drama",
        "Horror" to "horror",
        "Sci-Fi" to "space",
        "Romance" to "love"
    )

    private val menuItems = ArrayList<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quests)

        val menu: LinearLayout = findViewById(R.id.menu_container)
        channels.forEachIndexed { index, channel ->
            val item = layoutInflater.inflate(R.layout.item_menu, menu, false) as TextView
            item.text = channel.first
            item.setOnClickListener { openChannel(index) }
            menu.addView(item)
            menuItems.add(item)
        }

        if (savedInstanceState == null) openChannel(0)
    }

    private fun openChannel(index: Int) {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChannelFragment.newInstance(channels[index].second))
            .commit()

        menuItems.forEachIndexed { i, tv -> tv.isSelected = (i == index) }
    }
}