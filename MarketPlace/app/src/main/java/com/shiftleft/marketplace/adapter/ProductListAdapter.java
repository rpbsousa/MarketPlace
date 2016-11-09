package com.shiftleft.marketplace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiftleft.marketplace.R;
import com.shiftleft.marketplace.model.ShopItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by rsousa on 01/11/2016.
 */

public class ProductListAdapter extends BaseAdapter {

        private List<ShopItem> list;
        private Context mContext;

        public ProductListAdapter(Context context, List<ShopItem> list) {
            this.mContext = context;
            this.list = list;
        }

        public int getCount() {
            return list == null ? 0 : list.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*if(convertView == null) {
                convertView = inflater.inflate(R.layout.spinner_flags, parent, false);
            }

            ImageView icon= (ImageView) convertView.findViewById(R.id.image);
            ViewGroup.LayoutParams params = icon.getLayoutParams();
            params.height = height;
            icon.setLayoutParams(params);
            icon.setImageResource(languages.get(position).getFlag());
            TextView text=(TextView)convertView.findViewById(R.id.imageLabel);
            text.setText(languages.get(position).getLabel());
            */
            TextView tv = new TextView(mContext);
            tv.setText(list.get(position).getDescription());
            return tv;
        }
}
