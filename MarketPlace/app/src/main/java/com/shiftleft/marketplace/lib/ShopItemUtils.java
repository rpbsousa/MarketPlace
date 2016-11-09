package com.shiftleft.marketplace.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shiftleft.marketplace.MainActivity;
import com.shiftleft.marketplace.R;
import com.shiftleft.marketplace.listener.ShopItemGesturesListener;
import com.shiftleft.marketplace.model.ShopItem;

import java.util.List;

/**
 * Created by rsousa on 07/11/2016.
 */

public class ShopItemUtils {

    public static void doList(Context ctx, List<ShopItem> list, LayoutInflater inflater, LinearLayout listView) {
        for(int i = 0; i < list.size(); i++) {
            ShopItem item = list.get(i);

            if(item.getAlternative() != null) {
                item = item.getAlternative();
            }

            View v = inflater.inflate(R.layout.view_shop_list_item, null, false);
            v.setOnTouchListener(new ShopItemGesturesListener(ctx));
            TextView price = (TextView)v.findViewById(R.id.price);
            TextView description = (TextView)v.findViewById(R.id.description);
            TextView brand = (TextView)v.findViewById(R.id.brand);

            LinearLayout includedLayout = (LinearLayout)v.findViewById(R.id.includedLayout);
            LinearLayout acceptLayout = (LinearLayout)v.findViewById(R.id.acceptLayout);

            if(item.getStatus() == 1) {
                acceptLayout.setVisibility(View.VISIBLE);
            } else if(item.getStatus() == 0) {
                includedLayout.setVisibility(View.VISIBLE);
            }

            price.setText("" + item.getPrice() + " (" + item.getStatus() + ")");
            description.setText(item.getDescription());
            brand.setText(item.getBrand());

            Button lessBtn = (Button)v.findViewById(R.id.lessBtn);
            lessBtn.setTag(item);
            lessBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShopItem item = (ShopItem)view.getTag();
                    if(item.getQuantity() - 1 >= 0) {
                        item.setQuantity(item.getQuantity() - 1);
                    }
                    TextView tv = (TextView)((View)view.getParent()).findViewById(R.id.quantity);
                    tv.setText("" + (int)item.getQuantity());
                }
            });
            Button moreBtn = (Button)v.findViewById(R.id.moreBtn);
            moreBtn.setTag(item);
            moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShopItem item = (ShopItem)view.getTag();
                    item.setQuantity(item.getQuantity() + 1);
                    TextView tv = (TextView)((View)view.getParent()).findViewById(R.id.quantity);
                    tv.setText("" + (int)item.getQuantity());
                }
            });
            listView.addView(v);
        }
    }
}
