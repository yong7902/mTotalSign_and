package com.kolon.sign2.approval;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.TextDialog;

/**
 * 본문 보기 - 가로
 */
public class ApprovalFormActivity extends AppCompatActivity {

    private String TAG = ApprovalFormActivity.class.getSimpleName();

    private SubsamplingScaleImageView form_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_form);

        Intent it = getIntent();
        if (it == null) return;

        String url = it.getStringExtra("url");

        Log.d(TAG, "#### form url:" + url);
        ImageView closeBtn = (ImageView) findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        form_image = (SubsamplingScaleImageView) findViewById(R.id.form_image);
        form_image.setMaxScale(7);
        form_image.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {
            }

            @Override
            public void onImageLoaded() {
                form_image.post(new Runnable() {
                    @Override
                    public void run() {
                        showFormFullScreen();
                    }
                });
            }

            @Override
            public void onPreviewLoadError(Exception e) {
                viewMessage("onPreviewLoadError:"+e);
            }

            @Override
            public void onImageLoadError(Exception e) {
                viewMessage("onImageLoadError:"+e);
            }

            @Override
            public void onTileLoadError(Exception e) {
                viewMessage("onTileLoadError:"+e);
            }

            @Override
            public void onPreviewReleased() {
                viewMessage("onPreviewReleased");
            }
        });

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(0, 0);
        RequestManager mGlideRequestManager = Glide.with(this);
        mGlideRequestManager
                .asBitmap()
                .load(url)
                .apply(options)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        form_image.setImage(ImageSource.bitmap(resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        viewMessage("onLoadCleared");
                    }
                });
    }

    private void showFormFullScreen() {
        float baseWidth = form_image.getSWidth();
        float photoViewWidth = form_image.getWidth();

        double adjScale = photoViewWidth / baseWidth;
        adjScale = Math.round(adjScale * 100.0) / 100.0;

        PointF center = new PointF();
        form_image.setScaleAndCenter((float) adjScale, center);
    }

    private void viewMessage(String errMsg){
        TextDialog dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
        dialog.setCancelable(false);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show(getSupportFragmentManager());
    }
}
