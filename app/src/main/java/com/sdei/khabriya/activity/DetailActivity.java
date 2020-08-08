package com.sdei.khabriya.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sdei.khabriya.R;
import com.sdei.khabriya.models.ViewModel;
import com.sdei.khabriya.models.news.NewsResponse;
import com.sdei.khabriya.network.RetrofitClient;
import com.sdei.khabriya.utils.Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    AdView adView1, adView2;
    private int pageNo = 0;
    private List<NewsResponse> mNewsList = new ArrayList<>();
    private String url = "";
    private com.sdei.khabriya.models.ViewModel model;
    private String finalUrl = "";
    private TextView backTv;
    private RelativeLayout mToolBar;
    private LinearLayout alsoLayout, alsoLayout1, alsoLayout2, alsoLayout4;
    private TextView newsContent, newsContent1, newsContent2, newsContent4;
    private ImageView newsLogo, newsLogo1, newsLogo2, newsLogo4;
    private FloatingActionButton mActionButton, scrollTop;
    private NestedScrollView mNestedScrollView;
    private String category = "", categoryCode = "";
    private ImageView ivTop;
    private WebView mWebView;
    private TextView tvTitle;
    private TextView categoryTv;
    private TextView postedByTv;
    private TextView dateTv;
    private ProgressBar mProgressLayout;

//    {
//            "google.delivered_priority": "normal",
//            "google.sent_time": 1596738319747,
//            "google.ttl": 259200,
//            "google.original_priority": "normal",
//            "custom": "{\"u\":\"https:\\\/\\\/news.khabriya.in\\\/news\\\/19367\\\/\",\"i\":\"d009d657-1fe0-4914-a953-098abc897477\"}",
//            "from": "272267775926",
//            "alert": "तेलंगाना का नया सचिवालय डिजाइन मस्जिद की तरह दिखता है: भाजपा",
//            "title": "Khabriya",
//            "google.message_id": "0:1596738319765956%26e33c29f9fd7ecd",
//            "google.c.sender.id": "272267775926",
//            "androidNotificationId": -281260072
//    }

    void hitApi(String id) {
//        id = "21952";
        RetrofitClient.INSTANCE.getInstance().getNotificationNews(id).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                NewsResponse m = response.body();
                url =
                        Utilities.extractLinks(m.getContent().getRendered());
                ViewModel viewModel =
                        new ViewModel(
                                m.getTitle().getRendered(), url, "",
                                "", "", "", "",
                                m.getContent().getRendered(), m.getLink(), m.getDate()
                        );

                String title = viewModel.getTitle();
                link = viewModel.getLink();
                String coverImage = viewModel.getImage();
                String content = viewModel.getContent();
                String date = viewModel.getDate();
                String name = viewModel.getAuthorName();
                m.getCategories().get(0);
                category = m.getCategories().get(0).toString();
                loadContent(link, coverImage, title, content, date, category, name);
                getCategoryNews(category);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
//                Toast.makeText(this,t.getMessage().toString(),Toast.LENGTH_LONG).show();
                Log.d("MODEL", t.getMessage());

            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        adView1 = (AdView) findViewById(R.id.adView1);
        adView2 = findViewById(R.id.adVieww2);
        backTv = findViewById(R.id.backTv);
        mToolBar = findViewById(R.id.backTool);
        ivTop = findViewById(R.id.image);
        mProgressLayout = findViewById(R.id.progressBar);
        mWebView = findViewById(R.id.webview);
        tvTitle = findViewById(R.id.title_tv);
        categoryTv = findViewById(R.id.category_tv);
        dateTv = findViewById(R.id.date);
        postedByTv = findViewById(R.id.posted_by_tv);
        mActionButton = findViewById(R.id.floating_action_button);
        alsoLayout = findViewById(R.id.alsoLayout);
        alsoLayout1 = findViewById(R.id.alsoLayout1);
        alsoLayout2 = findViewById(R.id.alsoLayout2);
        alsoLayout4 = findViewById(R.id.alsoLayout4);
        newsContent = findViewById(R.id.newsContent);
        newsContent1 = findViewById(R.id.newsContent1);
        newsContent2 = findViewById(R.id.newsContent2);
        newsContent4 = findViewById(R.id.newsContent4);
        newsLogo = findViewById(R.id.newsLogo);
        newsLogo1 = findViewById(R.id.newsLogo1);
        newsLogo2 = findViewById(R.id.newsLogo2);
        newsLogo4 = findViewById(R.id.newsLogo4);
        scrollTop = findViewById(R.id.scrollTop);
        mNestedScrollView = findViewById(R.id.scroll);

        if (getIntent().hasExtra("customModel")) {
            String link = getIntent().getStringExtra("customModel");
            String id = getIntent().getStringExtra("customModel")
                    .replace("https://news.khabriya.in/news/", "")
                    .replace("/", "");
            hitApi(id);
        }

//        hitApi("");

//        mWebView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        scrollTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNestedScrollView.fullScroll(View.FOCUS_UP);
            }
        });
        if (!getIntent().hasExtra("notification")) {
            category = getIntent().getStringExtra("category");

            getCategoryNews(category);
        }
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra("notification")) {
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    finish();
                } else {
                    onBackPressed();
                }
            }
        });
        alsoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalUrl = Utilities.extractLinks(mNewsList.get(0).getContent().getRendered());

                model = new ViewModel(mNewsList.get(0).getTitle().getRendered(), finalUrl, "", "", "", "", "",
                        mNewsList.get(0).getContent().getRendered(), mNewsList.get(0).getLink(), mNewsList.get(0).getDate());
                openDetailActivity(model);

            }
        });

        alsoLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalUrl = Utilities.extractLinks(mNewsList.get(1).getContent().getRendered());
                model = new ViewModel(mNewsList.get(1).getTitle().getRendered(), finalUrl, "", "", "", "", "",
                        mNewsList.get(1).getContent().getRendered(), mNewsList.get(1).getLink(), mNewsList.get(1).getDate());
                openDetailActivity(model);
            }
        });

        alsoLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalUrl = Utilities.extractLinks(mNewsList.get(2).getContent().getRendered());

                model = new ViewModel(mNewsList.get(2).getTitle().getRendered(), finalUrl, "", "", "", "", "",
                        mNewsList.get(2).getContent().getRendered(), mNewsList.get(2).getLink(), mNewsList.get(2).getDate());
                openDetailActivity(model);
            }
        });
        alsoLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalUrl = Utilities.extractLinks(mNewsList.get(3).getContent().getRendered());

                model = new ViewModel(mNewsList.get(3).getTitle().getRendered(), finalUrl, "", "", "", "", "",
                        mNewsList.get(3).getContent().getRendered(), mNewsList.get(3).getLink(), mNewsList.get(3).getDate());
                openDetailActivity(model);
            }
        });
        if (!getIntent().hasExtra("notification")) {
            link = getIntent().getStringExtra(Utilities.EXTRA_LINK);
            String coverImage = getIntent().getStringExtra(Utilities.EXTRA_COVER_IMAGE);
            String title = getIntent().getStringExtra(Utilities.TITLE);
            String content = getIntent().getStringExtra(Utilities.CONTENT);
            String date = getIntent().getStringExtra(Utilities.DATE);
            category = getIntent().getStringExtra("category");
            String name = getIntent().getStringExtra("name");
            loadContent(link, coverImage, title, content, date, category, name);
        }

        String shareText = "Download app on your phone for latest news in your language : https://khabriya.in";

        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Utilities.isConnectingToInternet(DetailActivity.this)) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/*");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView2.loadAd(adRequest);

    }

    String link = "";

    void loadContent(String link, String coverImage, String title, String content, String date, String category, String name) {

        ImageLoader.getInstance().displayImage(coverImage, ivTop, Utilities.setDisplayOptions());
        if (title != null) {
            title = String.valueOf(new StringBuffer(title.replaceAll("&#8217;", "'")));
            tvTitle.setText(title);
        }
        if (name != null) {
            postedByTv.setText(name);
        }

        //Adview
        AdRequest adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);

        if (date != null) {
            String[] dateValue = date.split("T");
            String dateString = dateValue[0];
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            Date datedd = null;
            try {
                datedd = inputFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr = outputFormat.format(datedd);
            dateTv.setText(outputDateStr);
        }


//        if (getIntent().hasExtra("notification")) {
//            showWebView(getIntent().getStringExtra("notification"));
//            getCategoryNews(getIntent().getStringExtra("category"));
//        } else {
        showWebView(content);
//        }
       /* WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.loadDataWithBaseURL(null, "<style>a{color:black; text-decoration:none}img{display: inline;width: auto; height: auto;max-width: 100%;}iframe{display: inline;height: auto;max-width: 100%;}p{line-height: 40px}@font-face {font-family: \"Lato\";src: url(\'file:///android_asset/fonts/Lato.ttf\');}</style>" + content, "text/html", "UTF-8", null);
//        mWebView.loadData(content, "text/html", "UTF-8");
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebViewClient(webViewClient);*/
        ImageLoader.getInstance().displayImage(coverImage, ivTop, Utilities.setDisplayOptions());
    }

    private CustomWebViewClient webViewClient = new CustomWebViewClient();

    private class CustomWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:document.body.style.marginBottom=\"10%\"; void 0");
            mProgressLayout.setVisibility(View.GONE);
        }

    }


    void getCategoryNews(String category) {
        pageNo = 1;
        RetrofitClient.INSTANCE.getInstance().getCategoryWise(category).enqueue(new Callback<List<NewsResponse>>() {
            @Override
            public void onResponse(Call<List<NewsResponse>> call, Response<List<NewsResponse>> response) {
                try {
                    mNewsList = (List<NewsResponse>) response.body();
                    if (mNewsList != null && mNewsList.size() > 0) {
                        for (int i = 0; i < mNewsList.size(); i++) {
                            if (i == 0) {
                                final NewsResponse data = mNewsList.get(0);
                                newsContent.setText(Html.fromHtml(data.getTitle().getRendered()));
                                url = Utilities.extractLinks(data.getContent().getRendered());
                                ImageLoader.getInstance().displayImage(url, newsLogo, Utilities.setDisplayOptions());
                                alsoLayout.setVisibility(View.VISIBLE);
                            } else if (i == 1) {
                                newsContent1.setText(Html.fromHtml(mNewsList.get(1).getTitle().getRendered()));
                                url = Utilities.extractLinks(mNewsList.get(1).getContent().getRendered());
                                ImageLoader.getInstance().displayImage(url, newsLogo1, Utilities.setDisplayOptions());
                                alsoLayout1.setVisibility(View.VISIBLE);
                            } else if (i == 2) {
                                newsContent2.setText(Html.fromHtml(mNewsList.get(2).getTitle().getRendered()));
                                url = Utilities.extractLinks(mNewsList.get(2).getContent().getRendered());
                                ImageLoader.getInstance().displayImage(url, newsLogo2, Utilities.setDisplayOptions());
                                alsoLayout2.setVisibility(View.VISIBLE);
                            } else if (i == 3) {
                                newsContent4.setText(Html.fromHtml(mNewsList.get(3).getTitle().getRendered()));
                                url = Utilities.extractLinks(mNewsList.get(3).getContent().getRendered());
                                ImageLoader.getInstance().displayImage(url, newsLogo4, Utilities.setDisplayOptions());
                                alsoLayout4.setVisibility(View.VISIBLE);
                            }
                            mProgressLayout.setVisibility(View.GONE);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<NewsResponse>> call, Throwable t) {
                mProgressLayout.setVisibility(View.GONE);
            }
        });
    }

    void openDetailActivity(ViewModel viewModel) {
        Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
        intent.putExtra(Utilities.EXTRA_COVER_IMAGE, viewModel.getImage());
        intent.putExtra(Utilities.TITLE, viewModel.getTitle());
        intent.putExtra(Utilities.EXTRA_LINK, viewModel.getLink());
        intent.putExtra(Utilities.CONTENT, viewModel.getContent());
        intent.putExtra(Utilities.DATE, viewModel.getDate());
        Log.i("Category", "Tag" + category);
        intent.putExtra("category", category);
        intent.putExtra("name", viewModel.getAuthorName());
        startActivity(intent);
        finish();
    }


    void showWebView(String content) {
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        if (getIntent().hasExtra("notification")) {
//            mWebView.loadUrl(getIntent().getStringExtra("notification"));
//        } else {
//            mWebView.loadDataWithBaseURL(null, "<html><head><meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no'><script async> src: url('file:///android_asset/fonts/insta.js'); <style>a{color:black; text-decoration:none}img{display: block;width: auto; height: auto!important; max-width: 100%;}iframe{display: inline;width: 100%!important;}figure.wp-caption.aligncenter {width: auto!important;}p{line-height: 25px}@font-face {font-family: \"regular\";src: url(\'file:///android_asset/fonts/regular.ttf\');}</style>" + content+ "</body></html>", "text/html", "UTF-8", null);
        mWebView.loadDataWithBaseURL(null, "<html><head><script async defer src=\\\"https://platform.instagram.com/en_US/embeds.js\\\"></script><style>a{color:black; text-decoration:none}img{display: block;width: auto; height: auto!important; max-width: 100%;}iframe{display: inline;width: 100%!important;}figure.wp-caption.aligncenter {width: auto!important;}p{line-height: 25px}@font-face {font-family: \\\"regular\\\";src: url(\\'file:///android_asset/fonts/regular.ttf\\');}</style>" + content + "</body></html>", "text/html", "UTF-8", null);
//        }
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebViewClient(new MyBrowser());

    }

    //inner class
    class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
           /* view.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void performClick() throws Exception {
                    Log.d("LOGIN::", "Clicked");
                    Toast.makeText(DetailActivity.this, "Login clicked",
                            Toast.LENGTH_LONG).show();
                }
            }, "login");*/
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub

            System.out.println("started");
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            System.out.println("ends");
            super.onPageFinished(view, url);

        }

    }
}


