package com.sdei.khabriya.activity

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdei.khabriya.AppApplication
import com.sdei.khabriya.R
import com.sdei.khabriya.adapters.RecyclAdapter
import com.sdei.khabriya.models.news.NewsResponse
import com.sdei.khabriya.network.RetrofitClient
import com.sdei.khabriya.utils.MySharedPreferences
import com.sdei.khabriya.utils.Utilities
import com.sdei.khabriya.utils.showAlertSnackBar
import com.sdei.khabriya.utils.showToast
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    private lateinit var mLayoutManager: LinearLayoutManager
    private var queryString: String? = ""
    var mNewsList = ArrayList<NewsResponse>()
    private var pageNo: Int = 0
    var LIST_TYPE = 0
    var loadingMore = MutableLiveData<Boolean>()
    var mCategory_id = ""
    var mNewsTitle = "Latest News"

    var adapter: RecyclAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mCategory_id = if (MySharedPreferences.getInstance(this@SearchActivity).getString(
                MySharedPreferences.Key.CATEGORIES_CHOSEN
            ).isNullOrBlank()
        ) {
            ""
        } else {
            Log.i(
                "MainActivity", "" + MySharedPreferences.getInstance(this@SearchActivity).getString(
                    MySharedPreferences.Key.CATEGORIES_CHOSEN
                )
            )
            MySharedPreferences.getInstance(this@SearchActivity)
                .getString(MySharedPreferences.Key.CATEGORIES_CHOSEN)
        }
        setAdapter("")
        initScrollListener()

        edSearch.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (edSearch.text.isNotEmpty()) {
                    LIST_TYPE = 2
                } else {
                    LIST_TYPE = 0
                }
                pageNo = 0
                mNewsList = ArrayList()
                loadingMore.value = true
                getAllNews()
            }
            false
        }

        imgClose.setOnClickListener {
            finish()
        }

        loadingMore.observe(this, Observer {
            if (it) {
                if (pageNo > 1) {
                    progress.visibility = View.VISIBLE
                    progressSearch.visibility = View.GONE
                } else {
                    progress.visibility = View.GONE
                    progressSearch.visibility = View.VISIBLE
                }
            } else {
                progress.visibility = View.GONE
                progressSearch.visibility = View.GONE
            }
        })

    }

    private fun getAllNews() {
        if (AppApplication.hasNetwork()) {
            pageNo += 1
            if (pageNo <= 1) {
                progress.visibility = View.GONE
                progressSearch.visibility = View.VISIBLE
            } else {
                progress.visibility = View.VISIBLE
                progressSearch.visibility = View.GONE
            }
            when (LIST_TYPE) {
                0 -> {
                    txtSearchTitle.visibility = View.GONE
                    RetrofitClient.instance?.getAllNews("true", pageNo.toString())
                        ?.enqueue(object : Callback<List<NewsResponse?>?> {
                            override fun onFailure(call: Call<List<NewsResponse?>?>, t: Throwable) {
                            }

                            override fun onResponse(
                                call: Call<List<NewsResponse?>?>,
                                response: Response<List<NewsResponse?>?>
                            ) {
                                try {
                                    val temp = response.body() as ArrayList<NewsResponse>
                                    mNewsList.addAll(temp)
                                } catch (e: Exception) {
                                    showToast(getString(R.string.error_no_stories))
                                }
                                notifyAdapter()
                            }
                        })
                }
                1 -> {
                    txtSearchTitle.visibility = View.GONE
                    RetrofitClient.instance?.getData("", pageNo.toString(), mCategory_id)
                        ?.enqueue(object : Callback<List<NewsResponse?>?> {
                            override fun onFailure(call: Call<List<NewsResponse?>?>, t: Throwable) {
                            }

                            override fun onResponse(
                                call: Call<List<NewsResponse?>?>,
                                response: Response<List<NewsResponse?>?>
                            ) {
                                try {
                                    val temp = response.body() as ArrayList<NewsResponse>
                                    mNewsList.addAll(temp)
                                } catch (e: Exception) {
                                    showToast(getString(R.string.error_no_stories))
                                }
                                notifyAdapter()
                            }
                        })
                }
                2 -> {
                    txtSearchTitle.visibility = View.VISIBLE
                    txtSearchTitle.text = "Search Result for '" + edSearch.text.toString() + "'"

                    RetrofitClient.instance?.getSearch(
                        edSearch.text.toString(),
                        pageNo.toString()
                    )?.enqueue(object : Callback<List<NewsResponse?>?> {
                        override fun onFailure(call: Call<List<NewsResponse?>?>, t: Throwable) {
                        }

                        override fun onResponse(
                            call: Call<List<NewsResponse?>?>,
                            response: Response<List<NewsResponse?>?>
                        ) {
                            try {
                                val temp = response.body() as ArrayList<NewsResponse>
                                mNewsList.addAll(temp)
                            } catch (e: Exception) {
                                showToast(getString(R.string.error_no_stories))
                            }
                            notifyAdapter()
                        }
                    })
                }
            }
        } else {
            showAlertSnackBar(edSearch, getString(R.string.error_internet))
            notifyAdapter()
        }

    }


    fun notifyAdapter() {
        adapter!!.notifyAdapter(mNewsList, mNewsTitle, mNewsTitle)
        loadingMore.value = false
        if (mNewsList.size == 0) {
            txtError.text = resources.getString(R.string.no_data_available)
            txtError.visibility = View.VISIBLE
        } else {
            txtError.text = resources.getString(R.string.loading)
            txtError.visibility = View.GONE
        }
    }

    private fun setAdapter(title: String) {
        mLayoutManager = LinearLayoutManager(this@SearchActivity)
        news_recycler_view.layoutManager = mLayoutManager
        adapter = RecyclAdapter(
            mNewsList,
            Utilities.getThin(this@SearchActivity),
            this@SearchActivity,
            title,
            title,
            true
        )
        news_recycler_view.adapter = adapter
    }

    private fun initScrollListener() {
        news_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (AppApplication.hasNetwork()) {
                    if (!loadingMore.value!!) {
                        if (mNewsList.isNotEmpty()) {
                            if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mNewsList.size - 1) {
                                //bottom of list!
                                getAllNews()
                                loadingMore.value = true
                            }
                        }
                    }
                }
            }
        })
    }


}