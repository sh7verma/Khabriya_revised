package com.sdei.khabriya.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sdei.khabriya.R
import com.sdei.khabriya.models.tv.ChannelModel
import com.sdei.khabriya.utils.loadImage
import kotlinx.android.synthetic.main.item_channel.view.*
import java.util.*

class ChannelAdapter(
    var mData: ArrayList<ChannelModel>,
    var mClick: AdapterClick
) : RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = mData[position].channel_name

        holder.imgChannel.loadImage(mData[position].image)

        holder.llMain.setOnClickListener {
            mClick.openDialog(position)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName = itemView.txtName
        var llMain = itemView.llMain
        var imgChannel = itemView.imgChannel
    }

    interface AdapterClick {
        fun openDialog(position: Int)
    }

}