package com.shiftleft.marketplace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shiftleft.marketplace.lib.NetUtils;
import com.shiftleft.marketplace.lib.ShopItemUtils;
import com.shiftleft.marketplace.model.ShopListBid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class ShopListResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list_response);

        List<ShopListBid> bids = (List<ShopListBid>)getIntent().getSerializableExtra("RESULT");


        JSONArray arr = NetUtils.loadJSONFromAsset(this.getAssets(), "response.json");

        LinearLayout responseListLayout = (LinearLayout)findViewById(R.id.responseListLayout);

        for(ShopListBid bid : bids) {
            View view = getLayoutInflater().inflate(R.layout.view_bid_response, null, false);
            LinearLayout listLayout = (LinearLayout)view.findViewById(R.id.listLayout);
            TextView text = (TextView)view.findViewById(R.id.description);
            TextView price = (TextView)view.findViewById(R.id.price);
            TextView messageHolder = (TextView)view.findViewById(R.id.messageHolder);


            Button buyBtn = (Button)view.findViewById(R.id.buyBtn);
            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ShopListResponseActivity.this, BuyActivity.class);
                    startActivity(i);
                }
            });

            Button details = (Button)view.findViewById(R.id.drawListBtn);
            details.setTag(bid);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShopListBid bid = (ShopListBid)view.getTag();
                    ViewGroup vg = (ViewGroup)view.getParent().getParent().getParent().getParent();
                    LinearLayout listLayout = (LinearLayout)vg.findViewById(R.id.listLayout);
                    if(listLayout.getChildCount() == 0) {
                        ShopItemUtils.doList(ShopListResponseActivity.this, bid.getItems(),
                                getLayoutInflater(), listLayout);
                        listLayout.setVisibility(View.VISIBLE);
                    } else {
                        if(listLayout.getVisibility() == View.VISIBLE) {
                            listLayout.setVisibility(View.GONE);
                        } else {
                            listLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            text.setText(bid.getBrand());
            price.setText("Euro: " + bid.getPrice());

            TextView messageView = null;
            String label = "Cabaz a 100%";
            if(bid.getStatus() == 0) {
                messageView = (TextView)view.findViewById(R.id.messageAllOk);
            } else if(bid.getStatus() == 1) {
                messageView = (TextView)view.findViewById(R.id.messageAllChanged);
            } else {
                label = "Cabaz a " + "n" + "%";
                messageView = (TextView)view.findViewById(R.id.messageMissing);
            }
            messageView.setText(label);
            messageView.setVisibility(View.VISIBLE);

            messageHolder.setText("Price: " + bid.getPrice() + " (" + bid.getStatus() + ")");

            responseListLayout.addView(view);
        }
    }
}
