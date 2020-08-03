package com.sdei.khabriya.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdei.khabriya.AppApplication
import com.sdei.khabriya.R
import com.sdei.khabriya.adapters.ChannelAdapter
import com.sdei.khabriya.models.tv.ChannelModel
import com.sdei.khabriya.network.RetrofitClient
import com.sdei.khabriya.utils.showAlertSnackBar
import kotlinx.android.synthetic.main.activity_channel_list.*
import kotlinx.android.synthetic.main.layout_bottom.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChannelListActivity : AppCompatActivity(), ChannelAdapter.AdapterClick {

    var mList = arrayListOf<ChannelModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_list)
        getChannelList()

        llNews.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        backTv.setOnClickListener {
            finish()
        }

        swRefresh.setOnRefreshListener {
            getChannelList()
        }


    }

    private fun getChannelList() {
        progress.visibility = View.VISIBLE
        if (AppApplication.hasNetwork()) {
            RetrofitClient.instance?.getChannelList(intent.getStringExtra("language"))
                ?.enqueue(object : Callback<ArrayList<ChannelModel>> {
                    override fun onFailure(call: Call<ArrayList<ChannelModel>>, t: Throwable) {
                        progress.visibility = View.GONE
                        swRefresh.isRefreshing = false
                        setAdapter()
                    }

                    override fun onResponse(call: Call<ArrayList<ChannelModel>>,response: Response<ArrayList<ChannelModel>> ) {
                        progress.visibility = View.GONE
                        swRefresh.isRefreshing = false
                        mList = response.body()!!
                        setAdapter()
                    }
                })
        } else {
            showAlertSnackBar(rvChannelList, getString(R.string.error_internet))
        }
    }


    fun setAdapter() {
        rvChannelList.layoutManager = LinearLayoutManager(this)
        rvChannelList.adapter = ChannelAdapter(mList, this)

        if (mList.size > 0) {
            txtNoData.visibility = View.GONE
        } else {
            txtNoData.visibility = View.VISIBLE
        }

    }


    override fun openDialog(position: Int) {
        val intent = Intent(this, TvStreamingActivity::class.java)
        intent.putExtra("url", mList[position].stream_url)
        startActivity(intent)
    }


}