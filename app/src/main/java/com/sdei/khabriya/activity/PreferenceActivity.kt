package com.sdei.khabriya.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.sdei.khabriya.AppApplication
import com.sdei.khabriya.R
import com.sdei.khabriya.adapters.CustomAdapter
import com.sdei.khabriya.models.preference.MenuPreferenceResponse
import com.sdei.khabriya.network.RetrofitClient
import com.sdei.khabriya.utils.MySharedPreferences
import com.sdei.khabriya.utils.showAlertSnackBar
import kotlinx.android.synthetic.main.activity_genre_preference.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PreferenceActivity : AppCompatActivity() {


    private var selectedCats: String? =""
    private var customAdapter: CustomAdapter? = null
    private var mMenuList: ArrayList<MenuPreferenceResponse?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_preference)
        getMenuList()
        savePrefs.setOnClickListener {
            if(selectedCats?.isNotEmpty()!!){
                if (selectedCats != null && (selectedCats?.length!! > 0) && selectedCats?.get(selectedCats?.length!! - 1) == ',') {
                    selectedCats = selectedCats?.substring(0, selectedCats?.length!! - 1);
                }
                Log.i("PreferenceActivity", "Selected caterggoreis$selectedCats")
                MySharedPreferences.getInstance(this@PreferenceActivity).put(MySharedPreferences.Key.CATEGORIES_CHOSEN,selectedCats)
                startActivity(Intent(this@PreferenceActivity, MainActivity::class.java))
                finish()
            }
        }
    }


    private fun getMenuList() {
        if (AppApplication.hasNetwork()) {
            pref_loader.visibility = VISIBLE
            RetrofitClient.instance?.getPreferenceMenu()
                ?.enqueue(object : Callback<ArrayList<MenuPreferenceResponse?>> {
                    override fun onFailure(
                        call: Call<ArrayList<MenuPreferenceResponse?>>,
                        t: Throwable
                    ) {
                        pref_loader.visibility = GONE
                        Log.e("Menu error", "Unable to fetch menu")
                    }

                    override fun onResponse(
                        call: Call<ArrayList<MenuPreferenceResponse?>>,
                        response: Response<ArrayList<MenuPreferenceResponse?>>
                    ) {
                        pref_loader.visibility = GONE
                        mMenuList = response.body()!!
                        for(i in 0 until mMenuList.size){
                            mMenuList[i]?.selected = false
                        }
                        setMenuAdapter()
                    }
                })
        } else {
//            showAlertSnackBar(edSearch, getString(R.string.error_internet))
        }

    }

    private fun setMenuAdapter() {
        customAdapter = CustomAdapter(this, mMenuList)
        pref_cate_grid.adapter = customAdapter
        pref_cate_grid.layoutManager = GridLayoutManager(
            applicationContext, 2
        )
        customAdapter?.categoryListner = Listener@{
            selectedCats = it
        }
    }

}