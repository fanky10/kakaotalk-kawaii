package com.kakao.talk.theme.kakaofriendsa;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String KAKAOTALK_SETTINGS_THEME_URI = "kakaotalk://settings/theme/";
	private static final String MARKET_URI = "market://details?id=";
	private static final String KAKAO_TALK_PACKAGE_NAME = "com.kakao.talk";
	
	private boolean isCurrentVersionKakaoTalk = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		findViewById(R.id.cancel_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		final boolean isInstalled = isInstalledKakaoTalk();
		initApplyButton(isInstalled);
		initDescriptionText(isInstalled);
	}

	private void initDescriptionText(boolean isInstalled) {
		final TextView description = (TextView) findViewById(R.id.description);
		description.setText(isInstalled ? R.string.ask_for_apply_theme : R.string.label_for_install_kakaotalk_first);
	}

	private void initApplyButton(boolean isInstalled) {
		final Button applyButton = (Button) findViewById(R.id.apply_button);
		
		applyButton.setText(!isCurrentVersionKakaoTalk ? R.string.update_kakaotalk : (isInstalled ? R.string.apply : R.string.install_kakaotalk));
		applyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isCurrentVersionKakaoTalk && isInstalledKakaoTalk()) {
					applyTheme();
					return;
				}
				
				goToMarket();
			}
		});
	}
	
	private  boolean isInstalledKakaoTalk(){
		try {
			getPackageManager().getPackageInfo(KAKAO_TALK_PACKAGE_NAME, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	private void applyTheme() {
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(KAKAOTALK_SETTINGS_THEME_URI + getPackageName()));
		
		try {
			startActivity(intent);
			finish();
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, R.string.label_for_update_kakaotalk, Toast.LENGTH_SHORT).show();;
			isCurrentVersionKakaoTalk = false;
			initApplyButton(isInstalledKakaoTalk());
		}
	}

	private void goToMarket() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URI + KAKAO_TALK_PACKAGE_NAME));
		startActivity(intent);
		finish();
	}
}
