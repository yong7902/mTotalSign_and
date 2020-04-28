package com.kolon.sign2.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.kolon.sign2.R;
import com.kolon.sign2.network.NetworkConstants;
import com.kolon.sign2.utils.CommonUtils;

/**
 * Created by sunho_kim on 2019-12-02.
 */
public class NoticeActivity extends Activity implements OnClickListener {

	private WebView webView;
	private Button btnClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);

		btnClose = (Button)findViewById(R.id.btnClose);
		btnClose.setOnClickListener(this);

		webView = (WebView)findViewById(R.id.webContent);

		// content 가 있을경우 해당 값을 보여주고, url가 있을경우 해당 페이지를 보여준다.
		String contents = getIntent().getStringExtra("content");
		String url = getIntent().getStringExtra("url");

		if (!TextUtils.isEmpty(contents)) {
			// 공지내용 변환
			String html = contents.replace("&amp;", "&");
			html = html.replace("&lt;", "<");
			html = html.replace("&gt;", ">");
			html = html.replace("&quot;", "\"");
			html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><body><div style=\"margin:0px\">" + html + "</div></body></html>";

			webView.loadData(html, "text/html; charset=utf-8", "utf-8");
		}
		else if (!TextUtils.isEmpty(url)) {
			// default (asset 폴더 notice.html 보여준다.)
			//webView.loadUrl("file:///android_asset/notice.html");

			webView.loadUrl(url);
		}

		//화면 캡쳐 방지 적용
		CommonUtils.preventCapture(getBaseContext(), getWindow());
	}


	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btnClose:
				setResult(NetworkConstants.REQUEST_CODE_NOTICE_POPUP);
				finish();
				break;

		}
	}

	@Override
	public void onBackPressed() {
		// Back 버튼이 눌릴 때, 이전 화면이나 앱이 종료 되지 않도록 아래 코드 주석 처리
//		super.onBackPressed();
		return;
	}
}
