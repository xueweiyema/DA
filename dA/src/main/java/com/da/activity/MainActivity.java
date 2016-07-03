package com.da.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.da.DAuser;
import com.da.R;
import com.da.handler.MainActivityHandler;
import com.da.utility.DAutility;
import com.squareup.timessquare.CalendarPickerView;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CalendarPickerView calendar;
    private Handler handler = new MainActivityHandler(MainActivity.this);
    private TextView DAtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DAtv = (TextView) findViewById(R.id.da_tv);
        Thread startUpdate = new Thread() {
            @Override
            public void run() {
                getDAupdateServier(MainActivity.this);
            }
        };
        startUpdate.start();
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);

        // 实例化DAuser
        DAuser dauser = DAutility.ReadDASharedPreferences(this);
        DAtv.setText(dauser.getmStation() + dauser.getmTeam());
        calendar.setDauser(dauser);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new Date();
        try {
            date = format.parse("2015-02-15");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // String mDuty = dauser.getDuty(date);
        // Log.i(TAG, "mDuty=" + mDuty);

        calendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());

        // 打日志测试获取版本问题
        Log.i(TAG + " VerCode ", DAutility.getVerCode(this) + "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int mS = 0, mT = 0;

        int id = item.getItemId();
        switch (id) {
            case R.id.team01:
                mS = 0;
                mT = 0;
                break;
            case R.id.team02:
                mS = 0;
                mT = 1;
                break;
            case R.id.team03:
                mS = 0;
                mT = 2;
                break;
            case R.id.team04:
                mS = 0;
                mT = 3;
                break;
            case R.id.team05:
                mS = 0;
                mT = 4;
                break;
            case R.id.team11:
                mS = 1;
                mT = 0;
                break;
            case R.id.team12:
                mS = 1;
                mT = 1;
                break;
            case R.id.team13:
                mS = 1;
                mT = 2;
                break;
            case R.id.team14:
                mS = 1;
                mT = 3;
                break;
            case R.id.team15:
                mS = 1;
                mT = 4;
                break;
            case R.id.team21:
                mS = 2;
                mT = 0;
                break;
            case R.id.team22:
                mS = 2;
                mT = 1;
                break;
            case R.id.team23:
                mS = 2;
                mT = 2;
                break;
            case R.id.team24:
                mS = 2;
                mT = 3;
                break;
            case R.id.team25:
                mS = 2;
                mT = 4;
                break;
            case R.id.team26:
                mS = 2;
                mT = 5;
                break;
            case R.id.team27:
                mS = 2;
                mT = 6;
                break;
            case R.id.team28:
                mS = 2;
                mT = 7;
                break;
            default:
                return true;

        }
        DAuser dauser = new DAuser(MainActivity.this);
        dauser.setmStation(mS);
        dauser.setmTeam(mT);
        Log.i(TAG, "mS=" + mS + "mT=" + mT);
        Toast.makeText(MainActivity.this, "已选择" + dauser.getmStation() + dauser.getmTeam(), Toast.LENGTH_SHORT).show();
        DAutility.WriteDASharedPreferences(MainActivity.this, dauser);
        DAtv.setText(dauser.getmStation() + dauser.getmTeam());
        calendar.setDauser(dauser);
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        calendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
        return super.onOptionsItemSelected(item);
    }

    // 先发送信息给服务器再获取服务器信息
    private void getDAupdateServier(final Context context) {
        new Thread() {
            public void run() {
                String url = "http://www.dawechat.top/index.php?s=/Home/Index/daandroidupdate";
                Log.i(TAG, url);
                try {
                    URL u = new URL(url);
                    URLConnection connection = u.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    // 装配JSON
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("version", DAutility.getVerName(context));
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    Log.i("jsonObject====>", jsonObject.toString());
                    connection.setRequestProperty("da", jsonObject.toString());
                    InputStream is = connection.getInputStream(); // 获取输入流，此时才真正建立链接
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bufferReader = new BufferedReader(isr);
                    String inputLine = "";
                    String strResult = "";
                    while ((inputLine = bufferReader.readLine()) != null) {
                        strResult += inputLine + "\n";
                    }
                    try {
                        JSONObject jobj = new JSONObject(strResult);
                        // String mysql_error =
                        // jobj.getString("mysql_error");
                        String apk_url = jobj.getString("apk_url");
                        int serverVercode = jobj.getInt("apk_vercode");
                        String notice_text = jobj.getString("notice_text");
                        String apk_vername = jobj.getString("apk_vername");
                        Message msg = new Message();
                        msg.what = MainActivityHandler.REFRESH;
                        Bundle bundle = new Bundle();
                        bundle.putString("notice", notice_text);
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                        bundle = new Bundle();
                        bundle.putInt("serverVercode", serverVercode);
                        bundle.putString("apkUrl", apk_url);
                        bundle.putString("vername", apk_vername);
                        msg = new Message();
                        msg.what = MainActivityHandler.UPDATE;
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Log.i("strResult====>", strResult);
                } catch (MalformedURLException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
