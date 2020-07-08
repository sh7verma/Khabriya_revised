package com.sdei.khabriya.adapters;

import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sdei.khabriya.R;
import com.sdei.khabriya.models.ViewModel;
import com.sdei.khabriya.models.news.NewsResponse;
import com.sdei.khabriya.utils.Utilities;

import java.util.List;

import static android.view.View.GONE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder0> implements View.OnClickListener {

    private List<NewsResponse> mNewsList;

    Typeface mRegularTypeface;
    AppCompatActivity mContext;
    private String category;

    public RecyclerViewAdapter(List<NewsResponse> items, Typeface regularTypeface, AppCompatActivity context, String category) {
        this.mNewsList = items;
        mRegularTypeface = regularTypeface;
        mContext = context;
        this.category = category;
    }
    @Override
    public RecyclerViewAdapter.ViewHolder0 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder0(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder0 holder, int position) {
        final NewsResponse data = mNewsList.get(position);
        String url = "";
        url = Utilities.extractLinks(mNewsList.get(0).getContent().getRendered());

        if (url.isEmpty()) {
            holder.imageView.setVisibility(GONE);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(url, holder.imageView, Utilities.setDisplayOptions());
        }

        holder.textView.setText(Html.fromHtml(data.getTitle().getRendered()));

        final String finalUrl = url;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "";
                ViewModel model = new ViewModel(data.getTitle().getRendered(), finalUrl, "", name, "", "", "",
                        data.getContent().getRendered(), data.getLink(), data.getDate());
                openDetailActivity(model);
            }
        });
    }

    void openDetailActivity(ViewModel viewModel) {
//        Intent intent = new Intent(mContext, DetailActivity.class);
//        intent.putExtra(Utilities.EXTRA_COVER_IMAGE, viewModel.getImage());
//        intent.putExtra(Utilities.TITLE,viewModel.getTitle());
//        intent.putExtra(Utilities.EXTRA_LINK, viewModel.getLink());
//        intent.putExtra(Utilities.CONTENT, viewModel.getContent());
//        intent.putExtra(Utilities.DATE, viewModel.getDate());
//        intent.putExtra("category",category);
//        intent.putExtra("name",viewModel.getAuthorName());
//        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    @Override
    public void onClick(final View v) {

//        DetailActivity.navigate(this, view.findViewById(R.id.image), viewModel);

        // Give some time to the ripple to finish the effect
//        if (onItemClickListener != null) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    onItemClickListener.onItemClick(v, (Example) v.getTag());
//                }
//            }, 200);
//        }
    }

    protected class ViewHolder0 extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder0(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.country_photo);
            textView = (TextView) itemView.findViewById(R.id.country_name);
            textView.setTypeface(mRegularTypeface);
            int screenWidth = Utilities.getScreenWidth(mContext) / 2;
            int calculatedHeight = (int) (screenWidth / 1.56F);
            imageView.requestLayout();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, calculatedHeight);
            imageView.setLayoutParams(layoutParams);
            imageView.requestLayout();
        }
    }
    public void refeshAdapter(List<NewsResponse> mNewsList) {
        this.mNewsList = mNewsList;
        notifyDataSetChanged();
    }
}
