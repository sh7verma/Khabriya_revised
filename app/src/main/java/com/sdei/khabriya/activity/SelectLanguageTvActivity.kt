package com.sdei.khabriya.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdei.khabriya.AppApplication
import com.sdei.khabriya.R
import com.sdei.khabriya.adapters.TvLanguageAdapter
import com.sdei.khabriya.models.tv.TvLanguageModel
import com.sdei.khabriya.network.RetrofitClient
import com.sdei.khabriya.utils.showAlertSnackBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_select_language_tv.*
import kotlinx.android.synthetic.main.activity_select_language_tv.progress
import kotlinx.android.synthetic.main.activity_select_language_tv.swRefresh
import kotlinx.android.synthetic.main.layout_bottom.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectLanguageTvActivity : AppCompatActivity(), TvLanguageAdapter.AdapterClick {

    var mLanguageList = arrayListOf<TvLanguageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_language_tv)
        getLanguageList()

        llNews.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        backTv.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        swRefresh.setOnRefreshListener {
            getLanguageList()
        }
    }

    private fun getLanguageList() {
        progress.visibility = View.VISIBLE

        if (AppApplication.hasNetwork()) {
            RetrofitClient.instance?.getTvLanguageList()
                ?.enqueue(object : Callback<ArrayList<TvLanguageModel>> {
                    override fun onFailure(call: Call<ArrayList<TvLanguageModel>>, t: Throwable) {
                        progress.visibility = View.GONE
                        swRefresh.isRefreshing=false
                    }

                    override fun onResponse(
                        call: Call<ArrayList<TvLanguageModel>>,
                        response: Response<ArrayList<TvLanguageModel>>
                    ) {
                        progress.visibility = View.GONE
                        swRefresh.isRefreshing=false
                        mLanguageList = response.body()!!
                        setAdapter()
                    }
                })
        } else {
            showAlertSnackBar(rvTvLanguage, getString(R.string.error_internet))
        }
    }

    fun setAdapter() {
        rvTvLanguage.layoutManager = LinearLayoutManager(this)
        rvTvLanguage.adapter = TvLanguageAdapter(mLanguageList, this)
    }

    override fun openDialog(position: Int) {
        val intent = Intent(this, ChannelListActivity::class.java)
        intent.putExtra("language", mLanguageList[position].insert_language)
        startActivity(intent)
    }

}