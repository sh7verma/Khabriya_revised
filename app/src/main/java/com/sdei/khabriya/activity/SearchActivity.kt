package com.sdei.khabriya.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdei.khabriya.AppApplication
import com.sdei.khabriya.R
import com.sdei.khabriya.adapters.RecyclAdapter
import com.sdei.khabriya.models.news.NewsResponse
import com.sdei.khabriya.network.RetrofitClient
import com.sdei.khabriya.utils.MySharedPreferences
import com.sdei.khabriya.utils.Utilities
import com.sdei.khabriya.utils.showAlertSnackBar
import com.sdei.khabriya.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.txtError
import kotlinx.android.synthetic.main.activity_search.txtSearchTitle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity(){
    private lateinit var mLayoutManager: LinearLayoutManager
    private var queryString: String? = ""
    var mNewsList = ArrayList<NewsResponse>()
    private var pageNo: Int=0
    var LIST_TYPE = 0
    var loadingMore = MutableLiveData<Boolean>()
    var mCategory_id = ""
    var mNewsTitle = "Latest News"

    var adapter: RecyclAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mCategory_id = if(MySharedPreferences.getInstance(this@SearchActivity).getString(
                MySharedPreferences.Key.CATEGORIES_CHOSEN).isNullOrBlank()){
            ""
        }else{
            Log.i("MainActivity",""+ MySharedPreferences.getInstance(this@SearchActivity).getString(
                MySharedPreferences.Key.CATEGORIES_CHOSEN))
            MySharedPreferences.getInstance(this@SearchActivity).getString(MySharedPreferences.Key.CATEGORIES_CHOSEN)
        }
        searchView.isIconified = false;
        setAdapter("")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                queryString = query
                LIST_TYPE = if (query?.isNotEmpty()!!) {
                    2
                } else {
                    0
                }
                pageNo = 0
                mNewsList = ArrayList()
                searchView.isIconified = true
                getAllNews()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                adapter.getFilter().filter(newText)
                return false
            }
        })

    }

    private fun getAllNews() {
        if (AppApplication.hasNetwork()) {
            pageNo += 1
            if (pageNo <= 1) {
                progressSearch.visibility = View.GONE
            } else {
                progressSearch.visibility = View.VISIBLE
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
                    txtSearchTitle.text = "Search Result for '" + queryString + "'"

                    RetrofitClient.instance?.getSearch(
                        queryString,
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
            showAlertSnackBar(searchView, getString(R.string.error_internet))
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
            false
        )
        news_recycler_view.adapter = adapter
    }


    override fun onBackPressed() {
        if(searchView.isIconified){
            searchView.isIconified = true
        }
        super.onBackPressed()
    }

}