package com.sdei.khabriya.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.sdei.khabriya.AppApplication
import com.sdei.khabriya.R
import com.sdei.khabriya.adapters.MenuAdapter
import com.sdei.khabriya.adapters.RecyclAdapter
import com.sdei.khabriya.models.MenuResponse
import com.sdei.khabriya.models.news.NewsResponse
import com.sdei.khabriya.network.RetrofitClient
import com.sdei.khabriya.utils.Utilities
import com.sdei.khabriya.utils.showAlertSnackBar
import com.sdei.khabriya.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progress
import kotlinx.android.synthetic.main.layout_bottom.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), MenuAdapter.MenuItemClick {

    private var mAdIsLoading: Boolean = true
    private lateinit var mInterstitialAd: InterstitialAd
    var mNewsList = ArrayList<NewsResponse>()
    var mSearchNewsList = ArrayList<NewsResponse>()
    var mMenuList = ArrayList<MenuResponse>()

    private var mLayoutManager: LinearLayoutManager? = null
    var adapter: RecyclAdapter? = null

    var pageNo = 0
    var loadingMore = MutableLiveData<Boolean>()
    var mNewsTitle = "Latest News"
    var mCategory_id = ""
    var LIST_TYPE = 0
    // 0  Latest news
    // 1  Category
    // 2  Search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAdapter(mNewsTitle)
        getMenuList()
        getAllNews()
        initScrollListener()

        loadingMore.observe(this, Observer {
            if (it) {
                if (pageNo > 1) {
                    progress.visibility = View.VISIBLE
                } else {
                    progress.visibility = View.GONE
                }
            } else {
                progress.visibility = View.GONE
            }
        })

        imgDrawer.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        txtLatest.setOnClickListener {
            drawer.closeDrawer(Gravity.LEFT)
            LIST_TYPE = 0
            pageNo = 0
            mNewsTitle = txtLatest.text.toString()
            mNewsList = ArrayList()
            swRefresh.isRefreshing = true

            getAllNews()
        }

        llLiveTv.setOnClickListener {
            var intent = Intent(this, SelectLanguageTvActivity::class.java)
            startActivity(intent)
            finish()
        }

        settingsImg.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    SettingActivity::class.java
                )
            )
        }

        edSearch.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (edSearch.text.isNotEmpty()) {
                    LIST_TYPE = 2
                } else {
                    LIST_TYPE = 0
                }
                pageNo = 0
                mNewsList = ArrayList()
                swRefresh.isRefreshing = true
                getAllNews()
            }
            false
        }


        edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(
                s: CharSequence?, start: Int,
                before: Int, count: Int
            ) {
                if (count == 0) {
                    LIST_TYPE = 0
                    pageNo = 0
                    swRefresh.isRefreshing = true

                    mNewsList = ArrayList()
                    getAllNews()
                }
            }
        })
        recycler_view.setOnClickListener {
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            }
        }

        swRefresh.setOnRefreshListener {
            getMenuList()
            getAllNews()
        }
        swRefresh.isRefreshing = false
    }

    private fun hideSplash() {
        mInterstitialAd = InterstitialAd(this)

        initializeInterstitialAd("ca-app-pub-5555315701324132~6870727858")

        loadInterstitialAd("ca-app-pub-5555315701324132/1665484702")
        runAdEvents()

        /* Handler().postDelayed({
             if(adTime<1800000){
                 adTime+=6000
             }
             if (mInterstitialAd.isLoaded) {
                 mInterstitialAd.show()
             }
         },adTime)*/

    }


    private fun getMenuList() {
        if (AppApplication.hasNetwork()) {
            RetrofitClient.instance?.getMenu()?.enqueue(object : Callback<ArrayList<MenuResponse>> {
                override fun onFailure(call: Call<ArrayList<MenuResponse>>, t: Throwable) {
                    Log.e("Menu error", "Unable to fetch menu")
                }

                override fun onResponse(
                    call: Call<ArrayList<MenuResponse>>,
                    response: Response<ArrayList<MenuResponse>>
                ) {
                    mMenuList = response.body()!!
                    setMenuAdapter()
                }
            })
        } else {
            showAlertSnackBar(edSearch, getString(R.string.error_internet))
        }

    }

    private fun getAllNews() {
        if (AppApplication.hasNetwork()) {
            pageNo += 1
            if (pageNo <= 1) {
                progress.visibility = View.GONE
            } else {
                progress.visibility = View.VISIBLE
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
                    )
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
            }
        } else {
            showAlertSnackBar(edSearch, getString(R.string.error_internet))
            notifyAdapter()
        }

    }


    private fun initScrollListener() {
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (AppApplication.hasNetwork()) {
                    if (!loadingMore.value!! && mSearchNewsList.size == 0) {
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

    private fun setAdapter(title: String) {
        mLayoutManager = LinearLayoutManager(this@MainActivity)
        recycler_view.layoutManager = mLayoutManager
        adapter = RecyclAdapter(
            mNewsList,
            Utilities.getThin(this@MainActivity),
            this@MainActivity,
            title,
            title,
            false
        )
        recycler_view.adapter = adapter
    }

    fun notifyAdapter() {
        swRefresh.isRefreshing = false
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

    fun setMenuAdapter() {
        rvMenuList.layoutManager = LinearLayoutManager(this@MainActivity)
        rvMenuList.adapter = MenuAdapter(mMenuList, this)
    }

    override fun onMenuItemClick(pos: Int) {
        drawer.closeDrawer(Gravity.LEFT)
        LIST_TYPE = 1
        pageNo = 0
        swRefresh.isRefreshing = true

        mCategory_id = mMenuList[pos].object_id
        mNewsTitle = mMenuList[pos].title
        mNewsList = ArrayList()
        getAllNews()
    }


    private fun initializeInterstitialAd(appUnitId: String) {

        MobileAds.initialize(this, "ca-app-pub-5555315701324132~6870727858")

    }

    private fun loadInterstitialAd(interstitialAdUnitId: String) {

        mInterstitialAd.adUnitId = interstitialAdUnitId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun runAdEvents() {

        mInterstitialAd.adListener = object : AdListener() {

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.i("Test", "" + mInterstitialAd.isLoaded)

            }


            // If user clicks on the ad and then presses the back, s/he is directed to DetailActivity.
            override fun onAdClicked() {
                super.onAdOpened()
                mInterstitialAd.adListener.onAdClosed()
            }

            // If user closes the ad, s/he is directed to DetailActivity.
            override fun onAdClosed() {
//                startActivity(Intent(this@MainActivity, DetailActivity::class.java))
//                finish()
            }
        }
    }

}