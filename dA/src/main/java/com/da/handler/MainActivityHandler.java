package com.da.handler;

import com.da.activity.MainActivity;
import com.da.utility.DAutility;

import android.os.Handler;
import android.os.Message;

public class MainActivityHandler extends Handler {
	public static final int REFRESH = 0;
	public static final int UPDATE = 1;
	public static final int INCREASE = 2;
	private MainActivity context;

	public MainActivityHandler(MainActivity context) {
		this.context = context;
	}

	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case REFRESH:
			break;
		case UPDATE:
			int mVercode = DAutility.getVerCode(context);
			int serverVercode = msg.getData().getInt("serverVercode");
			String apkUrl = msg.getData().getString("apkUrl");
			String vername = msg.getData().getString("vername");
			if (serverVercode > mVercode) {
				DAutility.doNewVersionUpdate(context, apkUrl, vername); // 更新新版本
			} else {
			}
			break;
		}
	}
}
