package com.kolon.sign2.servicedesk;

public interface OnDocClickListener {
    void onFileClick(int position, String url);

    void onChecked(int position, boolean isChecked);
}
