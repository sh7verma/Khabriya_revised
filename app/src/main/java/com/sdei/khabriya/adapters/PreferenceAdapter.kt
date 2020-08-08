package com.sdei.khabriya.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdei.khabriya.R
import com.sdei.khabriya.models.preference.MenuPreferenceResponse


class CustomAdapter(ctx: Context, var imageModelArrayList: ArrayList<MenuPreferenceResponse?>) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {
    private var tempStr: String =""
    var categoryListner : ((info: String?)-> Unit)? = null
    private val inflater: LayoutInflater = LayoutInflater.from(ctx)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view: View = inflater.inflate(R.layout.item_genre, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.checkBox.isChecked = imageModelArrayList[position]?.selected!!
        holder.tvText.text = imageModelArrayList[position]?.title

        // holder.checkBox.setTag(R.integer.btnplusview, convertView);
        holder.checkBox.tag = position

        if(imageModelArrayList[position]?.selected!!){
            tempStr += imageModelArrayList[position]?.object_id.toString()+","
            Log.i("PreferenceAdapter","Added"+tempStr)
        }

        holder.checkBox.setOnClickListener {
            val pos = holder.checkBox.tag as Int
            imageModelArrayList[pos]?.selected = !imageModelArrayList[pos]?.selected!!

            if(imageModelArrayList[pos]?.selected!!){
                    tempStr += imageModelArrayList[pos]?.object_id.toString()+","
                Log.i("PreferenceAdapter","Added"+tempStr)
                categoryListner?.invoke(tempStr)
            }else{

                if (tempStr.contains(imageModelArrayList[pos]?.object_id.toString())){
                   tempStr = tempStr.replace(","+imageModelArrayList[pos]?.object_id.toString()+",","")
                }
                Log.i("PreferenceAdapter","Removed"+tempStr)
                categoryListner?.invoke(tempStr)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox = itemView.findViewById(R.id.checkCategory)
        val tvText: TextView = itemView.findViewById(R.id.item_text)

    }

}
