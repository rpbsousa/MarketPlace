package com.shiftleft.marketplace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shiftleft.marketplace.R;
import com.shiftleft.marketplace.model.ShopItem;

import java.util.List;
import java.util.Map;

/**
 * Created by rsousa on 05/11/2016.
 */

public class CategoryGroupAdapter extends BaseAdapter {

    private Map<String, List<ShopItem>> map;
    private Context mContext;

    public CategoryGroupAdapter(Context context, Map<String, List<ShopItem>> map) {
        this.mContext = context;
        this.map = map;
    }

    @Override
    public int getCount() {
        return map == null ? 0 : map.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.view_shop_list_category, viewGroup, false);

            TextView text=(TextView)convertView.findViewById(R.id.labelTextView);

            ListView listView = (ListView)convertView.findViewById(R.id.categoryItems);

            String key = (String)map.keySet().toArray()[i];
            text.setText(key);
            listView.setAdapter(new ProductListAdapter(mContext, map.get(key)));
        }
        TextView tv = new TextView(mContext);
        tv.setText("OLA " + i);
        return convertView;
    }
}
