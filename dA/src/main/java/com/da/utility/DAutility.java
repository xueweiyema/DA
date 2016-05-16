package com.da.utility;

import com.da.DAuser;
import com.da.service.UpdateService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class DAutility {
	// 读取vercode
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo("com.da", 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e("getVerCode", e.getMessage());
		}
		return verCode;
	}

	// 读取vername
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getApplicationContext().getPackageManager().getPackageInfo("com.da", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e("getVerName", e.getMessage());
		}
		return verName;
	}

	// 保存设置
	public static void WriteDASharedPreferences(Context context, DAuser user) {
		SharedPreferences mteam = context.getSharedPreferences("da", 0);
		SharedPreferences.Editor editor = mteam.edit();
		int mT = user.getmT();
		int mS = user.getmS();
		editor.putInt("mT", mT);
		editor.putInt("mS", mS);
		editor.commit();
	}

	// 读取保存的设置
	public static DAuser ReadDASharedPreferences(Context context) {
		SharedPreferences da = context.getSharedPreferences("da", 0);
		int mT = da.getInt("mT", 0);
		int mS = da.getInt("mS", 0);
		DAuser user = new DAuser(context);
		user.setmTeam(mT);
		user.setmStation(mS);
		return user;
	}

	public static void doNewVersionUpdate(final Context context, final String apkUrl, String vername) {
		// int verCode = getVerCode();
		String verName = getVerName(context);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(",\n发现新版本:");
		sb.append(vername);
		sb.append(",\n是否更新?");
		Dialog dialog = new AlertDialog.Builder(context).setTitle("软件更新").setMessage(sb.toString())
		// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 调用更新线程进行下载更新
								Intent intent = new Intent();
								intent.setClass(context, UpdateService.class);
								intent.putExtra("url", apkUrl);
								context.startService(intent);
							}
						}).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 点击"取消"按钮之后退出程序
					}
				}).create();// 创建
		// 显示对话框
		dialog.show();
	}

}
