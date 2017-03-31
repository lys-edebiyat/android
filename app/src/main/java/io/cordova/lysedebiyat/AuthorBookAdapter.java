package io.cordova.lysedebiyat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class AuthorBookAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[][] data;
    private LayoutInflater inflater;

    public AuthorBookAdapter(Context context, String data[][]) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
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

        holder.text.setText(data[position][1]);

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

        holder.text.setText(data[position][0]);

        return convertView;
    }


    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return data[position][0].subSequence(0, 1).charAt(0);
    }

    private class HeaderViewHolder {
        TextView text;
    }

    private class ViewHolder {
        TextView text;
    }
}