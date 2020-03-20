package com.kolon.sign2.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by sunho_kim on 2019-12-02.
 */

/**
 * SharedPreferenceManager 를 인스턴스로 관리하는 매니저
 */
public class SharedPreferenceManager {

	private static SharedPreferenceManager instance;
	private SharedPreferences sp = null;
	private static final String DELEMETER = "}'";

	/**
	 * 내부생성자
	 *
	 */
	private SharedPreferenceManager(Context ctx) {
		loadPreference(ctx);
	}

	/**
	 * SharedPreferenceManager 객체를 얻는다.
	 *
	 * @return SharedPreferenceManager SharedPreferenceManager 객체
	 */
	public synchronized static SharedPreferenceManager getInstance(Context ctx) {
		if (instance == null)
			instance = new SharedPreferenceManager(ctx);
		return instance;
	}

	/**
	 * 포로퍼티를 로드한다.
	 */
	private void loadPreference(Context ctx) {
		setSharedProperties(ctx.getSharedPreferences(Constants.PREF_NAME, Activity.MODE_PRIVATE));
	}

	private SharedPreferences getSharedPreference() {
		return this.sp;
	}

	private void setSharedProperties(SharedPreferences sp) {
		this.sp = sp;
	}

	public void setIntegerPreference(String key, int value) {
		SharedPreferences.Editor edit = getSharedPreference().edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public void setFloatPreference(String key, float value) {
		SharedPreferences.Editor edit = getSharedPreference().edit();
		edit.putFloat(key, value);
		edit.commit();
	}

	public void setStringPreference(String key, String value) {
		SharedPreferences.Editor edit = getSharedPreference().edit();
		edit.putString(key, value);
		edit.commit();
	}

	public void setBooleanPreference(String key, boolean value) {
		SharedPreferences.Editor edit = getSharedPreference().edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public int getIntegerPreference(String key) {
		return getSharedPreference().getInt(key, 0);
	}
	public float getFloatPreference(String key, float defaultValue) {
		return getSharedPreference().getFloat(key, defaultValue);
	}

	public String getStringPreference(String key) {
		return getSharedPreference().getString(key, "");
	}

	public boolean getBooleanPreference(String key) {
		return getSharedPreference().getBoolean(key, false);
	}

	public ArrayList<String> getArrayPreference(String key) {
		ArrayList<String> result = new ArrayList<String>();
		String str = getSharedPreference().getString(key, "");
		StringTokenizer st = new StringTokenizer(str, DELEMETER);
		while(st.hasMoreTokens()) {
			result.add(st.nextToken());
		}
		return result;
	}

	public void addArrayItemPreference(String key, String item) {
		String str = getSharedPreference().getString(key, "");
		StringTokenizer st = new StringTokenizer(str, DELEMETER);
		boolean isExist = false;
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if(item.equalsIgnoreCase(token)) {
				isExist = true;
				break;
			}
		}
		if(isExist == false) {
			SharedPreferences.Editor edit = getSharedPreference().edit();
			edit.putString(key, str+""+item+DELEMETER);
			edit.commit();
		}
	}

}