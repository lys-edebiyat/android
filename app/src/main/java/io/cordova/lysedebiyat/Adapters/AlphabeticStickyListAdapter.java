package io.cordova.lysedebiyat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import io.cordova.lysedebiyat.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class AlphabeticStickyListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] data;
    private LayoutInflater inflater;
    private Boolean isClickable = true;

    public AlphabeticStickyListAdapter(Context context, String[] data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public AlphabeticStickyListAdapter(Context context, String[] data, Boolean isClickable) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.isClickable = isClickable;
    }

    @Override
    public boolean isEnabled(int position) {
        return isClickable;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.yazar_list_item, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.eser_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(data[position]);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.yazar_list_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.yazar_header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + data[position].subSequence(0, 1).charAt(0);
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return data[position].subSequence(0, 1).charAt(0);
    }

    private class HeaderViewHolder {
        TextView text;
    }

    private class ViewHolder {
        TextView text;
    }

}