package com.padmavathy.mylibrary.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.activity.BottomNavActivity;
import com.padmavathy.mylibrary.adapter.CustomGrid;
import com.padmavathy.mylibrary.database.DatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.model.BookOnlineSearch;

import java.util.ArrayList;

public class FragmentWebView extends Fragment {

    private BottomNavActivity mainActivity;
    private Toolbar toolbar;
    WebView mWebView;
    TextView toolbar_tit;


    public FragmentWebView(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (BottomNavActivity)activity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View postView2 = inflater.inflate(R.layout.fragment_web_view, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_fragment);
        mWebView = (WebView) postView2.findViewById(R.id.webviewer);
        toolbar_tit = (TextView)postView2.findViewById(R.id.toolbar_title_view_isbn);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_fragment);

        toolbar_tit.setText("Image");

        String image__url = getArguments().getString("Key");
        Log.d("EDIT",image__url);
        loadUrlInWebView(image__url);
        return postView2;
    }

    private void loadUrlInWebView(String imageurl) {

        mWebView.loadUrl(imageurl);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setUseWideViewPort (true);

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e("TAG", "error code " + errorCode);
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    // handle back button

                    return true;

                }

                return false;
            }
        });
    }
}
