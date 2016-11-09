package com.shiftleft.marketplace.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.shiftleft.marketplace.lib.NetUtils;
import com.shiftleft.marketplace.model.ShopItem;
import com.shiftleft.marketplace.model.ShopListBid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by rsousa on 06/11/2016.
 */

public class RequestShopListPrice extends IntentService {

    public RequestShopListPrice() {
        super("RequestShopListPrice");
    }
    public RequestShopListPrice(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        try {
            Thread.currentThread().sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Map<String, List<ShopItem>> map = (Map<String, List<ShopItem>>)workIntent.getExtras().getSerializable("PARAM");

        List<ShopItem> list = new ArrayList<>();
        if(map != null) {
            for(String key : map.keySet()) {
                list.addAll(map.get(key));
            }
        }




        List<ShopListBid> bids = new ArrayList<>();


        JSONArray arr = NetUtils.loadJSONFromAsset(this.getAssets(), "response.json");
        for (int i = 0; i < arr.length(); i++) {
            ShopListBid bid = new ShopListBid();

            Random rand = new Random();
            List<ShopItem> items = new ArrayList<>();
            List<ShopItem> alternative = new ArrayList<>();
            List<ShopItem> unavailable = new ArrayList<>();
            for(ShopItem item : list) {
                ShopItem _i = item.clone();
                int randomNum = rand.nextInt(26);
                if(randomNum == 3 || randomNum == 5) { //substituição
                    _i.setStatus((short)1);
                    ShopItem subst = _i.clone();
                    subst.setDescription(subst.getDescription() + "(subs)");
                    _i.setAlternative(subst);
                    alternative.add(_i);
                } else if(randomNum == 5) { //nao existe substiuição
                    _i.setStatus((short)10);
                    unavailable.add(_i);
                } else {
                    _i.setDescription(_i.getDescription() + " (entrada de produto original)");
                    items.add(_i);
                }
            }
            unavailable.addAll(alternative);
            unavailable.addAll(items);
            items = unavailable;
            try {
                JSONObject val = arr.getJSONObject(i);
                bid.setBrand(val.getString("brand"));
                bid.setPrice(val.getDouble("price"));
                bid.setItems(items);

                //bid.setPromotions(createShopList(val.getJSONArray("promotion")));

                bid.setStatus(0);
                if(unavailable.size() > 0) {
                    bid.setStatus(10);
                }
                if(alternative.size() > 0) {
                    bid.setStatus(bid.getStatus() + 1);
                }

                bids.add(bid);
              } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Intent localIntent = new Intent("BROADCAST")
                .putExtra("STATUS", "1")
                .putExtra("RESULT", (Serializable)bids)
                .putExtra("REQUEST", (Serializable)list);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);



    }

    private List<ShopItem> createShopList(JSONArray array) throws JSONException {
        List<ShopItem> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            ShopItem item = createShopItem(array.getJSONObject(i));
            list.add(item);
        }
        return list;
    }

    private ShopItem createShopItem(JSONObject product) throws JSONException {
        ShopItem item = new ShopItem();
        item.setBrand(product.getString("brand"));
        item.setDescription(product.getString("description"));
        return item;
    }
}
