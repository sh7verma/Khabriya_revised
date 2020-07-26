package com.sdei.khabriya.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sdei.khabriya.R
import com.sdei.khabriya.models.tv.TvLanguageModel
import kotlinx.android.synthetic.main.item_tv_language.view.*
import java.util.ArrayList

class TvLanguageAdapter(
    var mData: ArrayList<TvLanguageModel>,
    var mClick: AdapterClick
) : RecyclerView.Adapter<TvLanguageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_tv_language, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text=mData[position].insert_language

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
    }

    interface AdapterClick {
        fun openDialog(position: Int)
    }

}