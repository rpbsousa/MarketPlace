package com.shiftleft.marketplace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shiftleft.marketplace.R;
import com.shiftleft.marketplace.model.NavigationItem;
import com.shiftleft.marketplace.model.ShopItem;

import java.util.List;
import java.util.Map;

/**
 * Created by rsousa on 05/11/2016.
 */

public class ShopItemGridAdapter extends BaseAdapter {

    private List<NavigationItem> list;
    private Context mContext;

    public ShopItemGridAdapter(Context context, List<NavigationItem> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            convertView = inflater.inflate(R.layout.grid_shop_item_nav, viewGroup, false);

            TextView text=(TextView)convertView.findViewById(R.id.textView);

            text.setText("" + list.get(i).getDescription());
        }
        return convertView;
    }
}
