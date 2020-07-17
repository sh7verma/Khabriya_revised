package com.sdei.khabriya.adapters

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Typeface
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.sdei.khabriya.R
import com.sdei.khabriya.activity.DetailActivity
import com.sdei.khabriya.models.ViewModel
import com.sdei.khabriya.models.news.NewsResponse
import com.sdei.khabriya.utils.Utilities
import com.sdei.khabriya.utils.loadImage

class RecyclAdapter(
    private var mNewsList: List<NewsResponse>,
    var mRegularTypeface: Typeface,
    var mContext: Activity,
    private var categoryName: String,
    private var category: String,
    var search: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    fun notifyAdapter(
        items: List<NewsResponse>,
        categoryName: String,
        category: String
    ) {
        this.category = category
        mNewsList = items
        this.categoryName = categoryName
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return if (viewType == 0) {
            ImageViewHolder(layoutInflater.inflate(R.layout.item_home_header, parent, false))
        } else {
            NewsHolder(layoutInflater.inflate(R.layout.recycler_row, parent, false))
        }

        /*  LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View listItem = layoutInflater.inflate(R.layout.recycler_row, parent, false);
        NewsHolder viewHolder = new NewsHolder(listItem);
        return viewHolder;*/
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                0
            }
            else -> {
                1
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is ImageViewHolder) {
            val data = mNewsList[0]
            var url = ""
            //        url =  data.getLink();
            url = Utilities.extractLinks(data.content.rendered)
//            if (url.isEmpty()) {
//                holder.imageView.visibility = View.GONE
//            } else {
                holder.imageView.visibility = View.VISIBLE

                holder.imageView.loadImage(url)
//
//                ImageLoader.getInstance().displayImage(
//                    url,
//                    holder.imageView,
//                    Utilities.setDisplayOptions()
//                )
//            }
            if (categoryName.isEmpty()) {
                holder.category.text = "English News"
            } else {
                holder.category.text = categoryName
            }
            if (search) {
                holder.headlinesTxt.text = "Related Articles"
            }
            holder.newsHeader.text = Html.fromHtml(
                data.title.rendered
            )
            //Adview
            var adRequest = AdRequest.Builder().build();
            holder.adView1.loadAd(adRequest);
            if (data.link.isEmpty()) {
                holder.sharewhats.setOnClickListener { view: View? -> shareWhatsApp(data.content.rendered) }
            } else {
                holder.sharewhats.setOnClickListener { view: View? -> shareWhatsApp(data.link) }
            }
            val finalUrl = url
            holder.parnetLayout.setOnClickListener {
                val name = ""
                val model =
                    ViewModel(
                        data.title.rendered, finalUrl, "", name, "", "", "",
                        data.content.rendered, data.link, data.date
                    )
                openDetailActivity(model)
            }
        } else {
            val data = mNewsList[position]
            var url = ""
            //        url =  data.getLink();
            if (data != null) {
                if(position ==3 || position==5){
                    var adRequest = AdRequest.Builder().build();
                    (holder as NewsHolder).adView.loadAd(adRequest);
                    (holder as NewsHolder).adView.visibility = VISIBLE
                }else{
                    (holder as NewsHolder).adView.visibility = GONE
                }

                url =
                    Utilities.extractLinks(data.content.rendered)
                if (url.isEmpty()) {
                    (holder as NewsHolder).imageView.visibility = View.GONE
                } else {
                    (holder as NewsHolder).imageView.visibility = View.VISIBLE
                    (holder as NewsHolder).imageView.loadImage(url)
//                    ImageLoader.getInstance().displayImage(
//                        url,
//                        holder.imageView,
//                        Utilities.setDisplayOptions()
//                    )
                }
                holder.textView.text = Html.fromHtml(
                    data.title.rendered
                )
                if (data.link.isEmpty()) {
                    holder.sharewhats.setOnClickListener { view: View? ->
                        shareWhatsApp(
                            data.content.rendered
                        )
                    }
                } else {
                    holder.sharewhats.setOnClickListener { view: View? -> shareWhatsApp(data.link) }
                }
                category = data.categories[0].toString()
            }
            val finalUrl = url
            (holder as NewsHolder).card_view.setOnClickListener {
                val name = ""
                val model =
                    ViewModel(
                        data.title.rendered, finalUrl, "", name, "", "", "",
                        data.content.rendered, data.link, data.date
                    )
                openDetailActivity(model)
            }
        }
    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    fun openDetailActivity(viewModel: ViewModel?) {
        val intent = Intent(mContext, DetailActivity::class.java)
        intent.putExtra(Utilities.EXTRA_COVER_IMAGE, viewModel?.getImage());
        intent.putExtra(Utilities.TITLE, viewModel?.getTitle());
        intent.putExtra(Utilities.EXTRA_LINK, viewModel?.getLink());
        intent.putExtra(Utilities.CONTENT, viewModel?.getContent());
        intent.putExtra(Utilities.DATE, viewModel?.getDate());
        Log.i("Category", "Tag" + category);
        intent.putExtra("category", category);
        intent.putExtra("name", viewModel?.getAuthorName());
        mContext.startActivity(intent);

    }

    protected inner class NewsHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var sharewhats: TextView
        var imageView: ImageView
        var card_view: CardView
        var adView : AdView

        init {
            imageView =
                itemView.findViewById<View>(R.id.country_photo) as ImageView
            textView = itemView.findViewById<View>(R.id.country_name) as TextView
            sharewhats = itemView.findViewById(R.id.whatsappShare)
            card_view = itemView.findViewById(R.id.card_view)
            adView = itemView.findViewById(R.id.adViewNews)
            textView.typeface = mRegularTypeface
           /* val screenWidth = Utilities.getScreenWidth(mContext) / 2
            val calculatedHeight = (screenWidth / 1.56f).toInt()
            imageView.requestLayout()
            val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                calculatedHeight
            )
            imageView.layoutParams = layoutParams
            imageView.requestLayout()*/
        }
    }

    fun shareWhatsApp(message: String?) {
        val html = Html.fromHtml(message).toString()
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/html"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, html)
        try {
            mContext.startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(mContext, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show()
        }
    }

    protected inner class ImageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var category: TextView
        var sharewhats: TextView
        var newsHeader: TextView
        var headlinesTxt: TextView
        var adView1:AdView;
        var parnetLayout: LinearLayout

        init {
            imageView = itemView.findViewById(R.id.cover_imageView)
            category = itemView.findViewById(R.id.category_name_textview)
            newsHeader = itemView.findViewById(R.id.name_textview)
            adView1 = itemView . findViewById (R.id.adView1);
            parnetLayout =
                itemView.findViewById<View>(R.id.parentLayout) as LinearLayout
            headlinesTxt = itemView.findViewById(R.id.headlinesTxt)
            sharewhats = itemView.findViewById(R.id.whatsappShare)
        }
    }

}