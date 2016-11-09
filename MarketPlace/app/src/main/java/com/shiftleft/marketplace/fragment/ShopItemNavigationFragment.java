package com.shiftleft.marketplace.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.shiftleft.marketplace.R;
import com.shiftleft.marketplace.adapter.ShopItemGridAdapter;
import com.shiftleft.marketplace.lib.NetUtils;
import com.shiftleft.marketplace.model.NavigationItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;


public class ShopItemNavigationFragment extends DialogFragment {
    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_MESSAGE = "arg_message";
    private static final String ARG_ACTIONS = "arg_actions";

    public interface NoticeDialogListener {
        void onDialogAction(DialogFragment dialog, int action);
    }

    private NoticeDialogListener mListener;

    private LinearLayout navLayout;
    private LinearLayout breadCrumbLayout;
    private HorizontalScrollView horizontalScrollView;
    private NavigationItem rootNav;
    private GridView productGridView;

    private NavigationItem recursiveThing(JSONObject json) throws JSONException {
        NavigationItem item = new NavigationItem(json.getString("term"));
        if(json.has("subTerms")) {
            JSONArray array = json.getJSONArray("subTerms");
            item.setTerms(new ArrayList<NavigationItem>());
            for(int i = 0; i < array.length(); i++) {
                Object entry = array.get(i);
                if(entry instanceof JSONObject) {
                    item.getTerms().add(recursiveThing((JSONObject)entry));
                } else {
                    item.getTerms().add(new NavigationItem(entry.toString()));
                }
            }
        }
        return item;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.view_category_nav, null);

        JSONArray array = NetUtils.loadJSONFromAsset(getActivity().getAssets(), "categories.json");
        rootNav = new NavigationItem("home");
        rootNav.setTerms(new ArrayList<NavigationItem>());
        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject json = (JSONObject)array.get(i);
                rootNav.getTerms().add(recursiveThing(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        navLayout = (LinearLayout)view.findViewById(R.id.navLayout);
        breadCrumbLayout = (LinearLayout)view.findViewById(R.id.breadCrumbLayout);
        horizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.horizontalScrollView);
        productGridView = (GridView)view.findViewById(R.id.productGridView);

        drawNavIteration(rootNav.getTerms());

        builder.setView(view);
        //builder.setTitle("TITLE");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogAction(ShopItemNavigationFragment.this, 1);
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogAction(ShopItemNavigationFragment.this, 2);
            }
        });
        return builder.create();
    }

    private void changeNavigationPath(Object _tag) {
        if(_tag instanceof NavigationItem) {
            NavigationItem tag  = (NavigationItem)_tag;
            if(breadCrumbLayout.getChildCount() == 0) {
                Button btn = new Button(getActivity());
                btn.setText("home");
                btn.setTag(rootNav);
                btn.setOnClickListener(breadCrumbBtnClickListener);
                breadCrumbLayout.addView(btn);
            }

            if(tag.getTerms() != null) {
                Button btn = new Button(getActivity());
                btn.setText(tag.getDescription());
                btn.setTag(tag);
                btn.setOnClickListener(breadCrumbBtnClickListener);
                breadCrumbLayout.addView(btn);
                drawNavIteration(tag.getTerms());
                horizontalScrollView.postDelayed(new Runnable() {
                    public void run() {
                        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                }, 200L);
            } else {
                productGridView.setAdapter(new ShopItemGridAdapter(getActivity(), rootNav.getTerms()));
                productGridView.setVisibility(View.VISIBLE);
                //navLayout.removeAllViews();
            }
        }
    }

    private View.OnClickListener toggleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavigationItem item = (NavigationItem)view.getTag();
            Toast.makeText(getActivity(), "clicked: " + item.getDescription(), Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener breadCrumbBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavigationItem item = (NavigationItem)view.getTag();
            drawNavIteration(item.getTerms());
            ViewGroup parent = ((ViewGroup)view.getParent());
            int index = parent.indexOfChild(view);
            Log.d("MarketPlace", "Pos: " + index);
            if(index > 0) {
                while (parent.getChildCount() > index + 1) {
                    Log.d("MarketPlace", "\tremove: ");
                    parent.removeViewAt(parent.getChildCount() - 1);
                }
            } else {
                parent.removeAllViews();
            }
        }
    };

    private void drawNavIteration(List<NavigationItem> nav) {
        navLayout.removeAllViews();
        for(NavigationItem item : nav) {
            if(item.getTerms() != null) {
                Button b = new Button(getActivity());
                b.setTag(item);
                b.setText(item.getDescription());
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeNavigationPath(view.getTag());
                    }
                });
                navLayout.addView(b);
            } else {
                ToggleButton b = new ToggleButton(getActivity());
                b.setTag(item);
                b.setText(item.getDescription());
                b.setTextOn(item.getDescription());
                b.setTextOff(item.getDescription());
                b.setOnClickListener(toggleButtonClickListener);
                navLayout.addView(b);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public static void createPopup(FragmentManager fragmentManager, int title, int message, int actions) {
        DialogFragment dialog = new ShopItemNavigationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, title);
        args.putInt(ARG_MESSAGE, message);
        args.putInt(ARG_ACTIONS, actions);
        dialog.setArguments(args);
        dialog.show(fragmentManager, "NoticeDialogFragment");
    }
    public static void onDialogAction(Activity activity, DialogFragment dialog, int action) {
        dialog.dismiss();
    }
}
