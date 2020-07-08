package com.sdei.khabriya.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sdei.khabriya.R
import com.sdei.khabriya.models.MenuResponse
import kotlinx.android.synthetic.main.list_single.view.*
import java.util.*

class MenuAdapter(
    var mData: ArrayList<MenuResponse>,
    var mClick: MenuItemClick
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_single, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = mData[position].title
        holder.llMain.setOnClickListener {
            mClick.onMenuItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle = itemView.txtTitle!!
        val llMain = itemView.llMain!!
    }

    interface MenuItemClick {
        fun onMenuItemClick(pos: Int)
    }

}