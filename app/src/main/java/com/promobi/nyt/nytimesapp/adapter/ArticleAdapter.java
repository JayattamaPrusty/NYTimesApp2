package com.promobi.nyt.nytimesapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.promobi.nyt.nytimesapp.R;
import com.promobi.nyt.nytimesapp.activity.DetailActivity;
import com.promobi.nyt.nytimesapp.api.ApiService;
import com.promobi.nyt.nytimesapp.model.Doc;
import com.promobi.nyt.nytimesapp.model.Multimedium;
import com.promobi.nyt.nytimesapp.util.MyDate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.promobi.nyt.nytimesapp.activity.DashboardActivity.EXTRA_ARTICLE_URL;

/**
 * Created by dw on 24/02/17.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private ArrayList<Doc> mData;
    private Context mContext;

    public ArticleAdapter(ArrayList<Doc> data, Context context) {
        mData = data;
        mContext = context;
    }

    // Called when a new view for an item must be created. This method does not return the view of
    // the item, but a ViewHolder, which holds references to all the elements of the view.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // The view for the item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        // Create a ViewHolder for this view and return it
        return new ViewHolder(itemView);
    }

    // Populate the elements of the passed view (represented by the ViewHolder) with the data of
    // the item at the specified position.
    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Doc article = mData.get(position);

        vh.tvTitle.setText(getSafeString(article.getHeadline().getMain()));
        vh.tvType.setText(getSafeString(article.getTypeOfMaterial()));
        vh.tvPagecount.setText(getSafeString(article.getPrintPage()));

        if (article.getPubDate() != null) {
            vh.tvDate.setVisibility(View.VISIBLE);
            MyDate date = new MyDate(article.getPubDate());
            vh.tvDate.setText(date.format1());
        } else
            vh.tvDate.setVisibility(View.GONE);

        ArrayList<Multimedium> multimedia = (ArrayList<Multimedium>) article.getMultimedia();
        String thumbUrl = "";
        for (Multimedium m : multimedia) {
            if (m.getType().equals("image") && m.getSubtype().equals("thumbnail")) {
                thumbUrl = ApiService.API_IMAGE_BASE_URL + m.getUrl();
                break;
            }
        }
        if (!thumbUrl.isEmpty())
            // TODO: Glide seems to not cache most of these images but load them from the URL each time
            Glide.with(mContext)
                    .load(thumbUrl)
                    // Save original image in cache (less fetching from server)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.placeholder_thumb)
                    .error(R.drawable.error_thumb)
                    .into(vh.ivThumb);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private String getSafeString(String str) {
        if (str == null)
            return "";
        else
            return str;
    }

    public void clearArticles() {
        mData.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }

    public void appendArticles(List<Doc> articles) {
        int oldSize = getItemCount();
        mData.addAll(articles);
        notifyItemRangeInserted(oldSize, articles.size());
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivThumb;
        TextView tvDate;
        TextView tvTitle,tvType,tvPagecount,tvShare;
        ImageButton overflow;

        // Create a viewHolder for the passed view (item view)
        ViewHolder(View view) {
            super(view);
            ivThumb = (ImageView) view.findViewById(R.id.ivThumb);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvType = (TextView) view.findViewById(R.id.txt_cat);
            tvPagecount = (TextView) view.findViewById(R.id.tvPagecount);
            tvShare = (TextView) view.findViewById(R.id.tvShare);
            overflow = itemView.findViewById(R.id.overflow);
            overflow.setOnClickListener(this);
            tvShare.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.putExtra(EXTRA_ARTICLE_URL, mData.get(getLayoutPosition()).getWebUrl());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
             if (v == overflow) {
                //Creating the instance of PopupMenu
                @SuppressLint("RestrictedApi") ContextThemeWrapper ctw = new ContextThemeWrapper(mContext, R.style.CustomPopupTheme);

                PopupMenu popup = new PopupMenu(mContext, overflow);
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.news_overflow, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        return true;
                    }
                });

                popup.show();
            }else if(v==tvShare){
                 Intent i = new Intent(android.content.Intent.ACTION_SEND);
                 i.setType("text/plain");
                 i.putExtra(android.content.Intent.EXTRA_TEXT, mData.get(getLayoutPosition()).getWebUrl());
                 mContext.startActivity(Intent.createChooser(i, "Share link using"));
             }
        }
    }

}
