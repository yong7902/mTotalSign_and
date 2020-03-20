package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * Created by sunho_km on 2019. 12. 27..
 */

public class TextsizeVO {
    private String title;               // 리스트 타이틀
    private String sampleText;          // 글자 크기 내용
    private float textsize;               // 글자 크기 값
    private boolean isChecked;          // 선택 여부


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSampleText() {
        return sampleText;
    }

    public void setSampleText(String sampleText) {
        this.sampleText = sampleText;
    }

    public float getTextsize() {
        return textsize;
    }

    public void setTextsize(float textsize) {
        this.textsize = textsize;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
