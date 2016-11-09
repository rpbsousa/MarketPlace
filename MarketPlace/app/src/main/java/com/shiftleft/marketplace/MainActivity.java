package com.shiftleft.marketplace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.shiftleft.marketplace.adapter.CategoryGroupAdapter;
import com.shiftleft.marketplace.adapter.ProductListAdapter;
import com.shiftleft.marketplace.fragment.ShopItemNavigationFragment;
import com.shiftleft.marketplace.lib.NetUtils;
import com.shiftleft.marketplace.lib.ShopItemUtils;
import com.shiftleft.marketplace.listener.ShopItemGesturesListener;
import com.shiftleft.marketplace.model.ShopItem;
import com.shiftleft.marketplace.service.RequestShopListPrice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ShopItemNavigationFragment.NoticeDialogListener {

    //private GestureDetectorCompat mDetector;

    private Map<String, List<ShopItem>> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mDetector = new GestureDetectorCompat(this, new ShopItemGesturesListener());


        //ListView listView = (ListView)findViewById(R.id.listView);
        JSONArray arr = NetUtils.loadJSONFromAsset(this.getAssets(), "cabaz.json");
        map = new HashMap<>();

        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject val = arr.getJSONObject(i);
                ShopItem item = new ShopItem();
                item.setCategory(val.getString("category"));
                item.setSubCategory(val.getString("subCategory"));
                item.setDescription(val.getString("description"));
                item.setBrand(val.getString("brand"));

                if(val.has("section")) {
                    item.setSection(val.getString("section"));
                }
                if(val.has("price")) {
                    item.setPrice(val.getDouble("price"));
                }
                if(val.has("quantity")) {
                    item.setQuantity(val.getDouble("quantity"));
                }
                List<ShopItem> categList = map.get(item.getCategory());

                if(categList == null) {
                    categList = new ArrayList<>();
                    map.put(item.getCategory(), categList);
                }
                categList.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(String key : map.keySet()) {
            View view = getLayoutInflater().inflate(R.layout.view_shop_list_category, null, false);
            TextView text = (TextView)view.findViewById(R.id.labelTextView);
            LinearLayout listView = (LinearLayout)view.findViewById(R.id.categoryItems);
            text.setText(key);
            List<ShopItem> list = map.get(key);
            ShopItemUtils.doList(MainActivity.this, list, getLayoutInflater(), listView);
            LinearLayout listLayout = (LinearLayout)findViewById(R.id.listLayout);
            listLayout.addView(view);
        }
    }

//    private void doList(String key, List<ShopItem> list, LayoutInflater inflater, LinearLayout listView) {
//        for(int i = 0; i < list.size(); i++) {
//            ShopItem item = list.get(i);
//            View v = inflater.inflate(R.layout.view_shop_list_item, null, false);
//            v.setOnTouchListener(new ShopItemGesturesListener(MainActivity.this));
//            TextView price = (TextView)v.findViewById(R.id.price);
//            TextView description = (TextView)v.findViewById(R.id.description);
//            TextView brand = (TextView)v.findViewById(R.id.brand);
//            price.setText("" + item.getPrice());
//            description.setText(item.getDescription());
//            brand.setText(item.getBrand());
//
//            Button lessBtn = (Button)v.findViewById(R.id.lessBtn);
//            lessBtn.setTag(new Object[]{new Integer(i), key});
//            lessBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ShopItem item = getShopItemFromTag((Object[])view.getTag());
//                    if(item.getQuantity() - 1 >= 0) {
//                        item.setQuantity(item.getQuantity() - 1);
//                    }
//                    TextView tv = (TextView)((View)view.getParent()).findViewById(R.id.quantity);
//                    tv.setText("" + (int)item.getQuantity());
//                }
//            });
//            Button moreBtn = (Button)v.findViewById(R.id.moreBtn);
//            moreBtn.setTag(new Object[]{new Integer(i), key});
//            moreBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ShopItem item = getShopItemFromTag((Object[])view.getTag());
//                    item.setQuantity(item.getQuantity() + 1);
//                    TextView tv = (TextView)((View)view.getParent()).findViewById(R.id.quantity);
//                    tv.setText("" + (int)item.getQuantity());
//                }
//            });
//            listView.addView(v);
//        }
//    }
//    private ShopItem getShopItemFromTag(Object[] tag) {
//        int index = (Integer)tag[0];
//        String key = (String)tag[1];
//        ShopItem item = map.get(key).get(index);
//        return item;
//    }
    public void gerarEncomenda(View v) {
        Intent mServiceIntent = new Intent(MainActivity.this, RequestShopListPrice.class);
        mServiceIntent.putExtra("PARAM", (Serializable)map);

        MainActivity.this.startService(mServiceIntent);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver,
                new IntentFilter("BROADCAST"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Serializable bids = intent.getSerializableExtra("RESULT");
            Serializable request = intent.getSerializableExtra("REQUEST");

            Intent i = new Intent(context, ShopListResponseActivity.class);
            i.putExtra("RESULT", bids);
            i.putExtra("REQUEST", request);
            startActivity(i);

            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mMessageReceiver);

        }
    };

    @Override
    public void onDialogAction(DialogFragment dialog, int action) {
        ShopItemNavigationFragment.onDialogAction(this, dialog, action);
    }
//


    public void addItem(View v) {
        ShopItemNavigationFragment.createPopup(getSupportFragmentManager(), R.string.app_name, R.string.hello_blank_fragment, 1);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//        this.mDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }

//    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//        private static final String DEBUG_TAG = "Gestures";
//
//        @Override
//        public boolean onDown(MotionEvent event) {
//            Log.d(DEBUG_TAG,"onDown: " + event.toString());
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent event1, MotionEvent event2,
//                               float velocityX, float velocityY) {
//            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
//            return true;
//        }
//    }

}
