package com.hiddenramblings.tagmo.settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hiddenramblings.tagmo.Preferences_;
import com.hiddenramblings.tagmo.R;
import com.hiddenramblings.tagmo.TagUtil;
import com.hiddenramblings.tagmo.amiibo.Amiibo;

import java.util.ArrayList;
import java.util.Collections;

class SettingsAmiiboAdapter extends BaseAdapter {

    Preferences_ prefs;

    ArrayList<Amiibo> items;

    SettingsAmiiboAdapter(Preferences_ prefs, ArrayList<Amiibo> items) {
        this.prefs = prefs;
        this.items = items;
        Collections.sort(items);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).id;
    }

    @Override
    public Amiibo getItem(int i) {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.amiibo_compact_card, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String tagInfo = null;
        String amiiboHexId = "";
        String amiiboName = "";
        String amiiboSeries = "";
        String amiiboType = "";
        String gameSeries = "";
//        String character = "";
        String amiiboImageUrl;

        Amiibo amiibo = getItem(position);
        amiiboHexId = TagUtil.amiiboIdToHex(amiibo.id);
        amiiboImageUrl = amiibo.getImageUrl();
        if (amiibo.name != null)
            amiiboName = amiibo.name;
        if (amiibo.getAmiiboSeries() != null)
            amiiboSeries = amiibo.getAmiiboSeries().name;
        if (amiibo.getAmiiboType() != null)
            amiiboType = amiibo.getAmiiboType().name;
        if (amiibo.getGameSeries() != null)
            gameSeries = amiibo.getGameSeries().name;
//        if (amiibo.getCharacter() != null)
//            character = amiibo.getCharacter().name;

        holder.txtError.setVisibility(android.view.View.GONE);
        setAmiiboInfoText(holder.txtName, amiiboName, false);
        setAmiiboInfoText(holder.txtTagId, amiiboHexId, tagInfo != null);
        setAmiiboInfoText(holder.txtAmiiboSeries, amiiboSeries, tagInfo != null);
        setAmiiboInfoText(holder.txtAmiiboType, amiiboType, tagInfo != null);
        setAmiiboInfoText(holder.txtGameSeries, gameSeries, tagInfo != null);
//        setAmiiboInfoText(holder.txtCharacter, character, tagInfo != null);
        holder.txtPath.setVisibility(android.view.View.GONE);

        if (holder.imageAmiibo != null) {
            holder.imageAmiibo.setVisibility(android.view.View.GONE);
            Glide.with(convertView).clear(holder.target);
            if (amiiboImageUrl != null) {
                Glide.with(convertView)
                        .setDefaultRequestOptions(new RequestOptions().onlyRetrieveFromCache(onlyRetrieveFromCache(convertView)))
                        .asBitmap()
                        .load(amiiboImageUrl)
                        .into(holder.target);
            }
        }

        return convertView;
    }

    void setAmiiboInfoText(TextView textView, CharSequence text, boolean hasTagInfo) {
        if (hasTagInfo) {
            textView.setText("");
        } else if (text.length() == 0) {
            textView.setText("Unknown");
            textView.setTextColor(Color.RED);
        } else {
            textView.setText(text);
            textView.setTextColor(textView.getTextColors().getDefaultColor());
        }
    }

    boolean onlyRetrieveFromCache(View view) {
        String imageNetworkSetting = prefs.imageNetworkSetting().get();
        if (SettingsFragment.IMAGE_NETWORK_NEVER.equals(imageNetworkSetting)) {
            return true;
        } else if (SettingsFragment.IMAGE_NETWORK_WIFI.equals(imageNetworkSetting)) {
            ConnectivityManager cm = (ConnectivityManager)
                    view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork == null || activeNetwork.getType() != ConnectivityManager.TYPE_WIFI;
        } else {
            return false;
        }
    }

    class ViewHolder {
        TextView txtError;
        TextView txtName;
        TextView txtTagId;
        TextView txtAmiiboSeries;
        TextView txtAmiiboType;
        TextView txtGameSeries;
        TextView txtCharacter;
        TextView txtPath;
        ImageView imageAmiibo;

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                imageAmiibo.setVisibility(android.view.View.GONE);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                imageAmiibo.setVisibility(android.view.View.GONE);
            }

            @Override
            public void onResourceReady(@NonNull Bitmap resource, Transition transition) {
                imageAmiibo.setImageBitmap(resource);
                imageAmiibo.setVisibility(android.view.View.VISIBLE);
            }
        };

        public ViewHolder(View view) {
            this.txtError = view.findViewById(R.id.txtError);
            this.txtName = view.findViewById(R.id.txtName);
            this.txtTagId = view.findViewById(R.id.txtTagId);
            this.txtAmiiboSeries = view.findViewById(R.id.txtAmiiboSeries);
            this.txtAmiiboType = view.findViewById(R.id.txtAmiiboType);
            this.txtGameSeries = view.findViewById(R.id.txtGameSeries);
            this.txtCharacter = view.findViewById(R.id.txtCharacter);
            this.txtPath = view.findViewById(R.id.txtPath);
            this.imageAmiibo = view.findViewById(R.id.imageAmiibo);

            view.setTag(this);
        }
    }
}
