package com.kolon.sign2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kolon.sign2.BuildConfig;
import com.kolon.sign2.R;
import com.kolon.sign2.vo.AttachFileListVO;

import java.io.File;

public class AttachFileViewerDialog extends Dialog {

    private AttachFileListVO data;
    private Context mContext;
    private String key;

    public AttachFileViewerDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();

        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        lpWindow.dimAmount = 0.8f;
        lpWindow.windowAnimations = R.style.AnimationPopupStyle;
        getWindow().setAttributes(lpWindow);

        getWindow().setContentView(R.layout.dialog_attach_file_viewer);

        String url = BuildConfig.DocViewerURL + "SynapDocViewServer/viewer/doc.html?key="+key+"&contextPath=/SynapDocViewServer";

        Log.d("AttachFileViewerDialog", "#### url:"+url);
        LinearLayout close = (LinearLayout) findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        WebView mWebView = (WebView) findViewById(R.id.file_webview);
        mWebView.setInitialScale(1);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        webSettings.setAppCacheEnabled(true);
        File appCacheDir = new File(mContext.getCacheDir(), "appCache");
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
        }
        webSettings.setAppCachePath(appCacheDir.getAbsolutePath());
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadsImagesAutomatically(true);

        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClientClass());

        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(data.getName());

    }

    public void setData(AttachFileListVO data, String key) {
        this.data = data;
        this.key = key;
    }


    private class WebViewClientClass extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}
