package com.kolon.sign2.setting.adapter;

/**
 * Created by sunho_kim on 2019-12-31.
 */

public interface ListEditInterface {
    void refreshScreen();
    void deleteList(int position);
    void defaultList();
    void selectPosition(int position);//계정선택
    void addAccount();//계정추가
    void errMessage(String msg);//계정삭제 조건이 아닐시 메세지표시
}
