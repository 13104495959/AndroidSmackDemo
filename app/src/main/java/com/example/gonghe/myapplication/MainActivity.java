/**
 * Created with IntelliJ IDEA.
 * User: gonghe
 * Date: 2018/9/25
 * Time: 下午2:47
 * To change this template use File | Settings | File Templates.
 * Description: 测试入口
 */
package com.example.gonghe.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 使用流程
        XmppService xmppService = XmppService.getInstance();
        xmppService.xmppServiceInterface = new XmppServiceImplements();
        xmppService.setServicearameter(5222, "192.168.1.34", "fss");
        xmppService.creatXmppConnection("fssadmin001-be177eec-ba9c-55fe-a51c-3552154e117f", "fssadmin001-be177eec-ba9c-55fe-a51c-3552154e117f");

    }
}

class XmppServiceImplements implements XmppServiceInterface {

    @Override
    public void getXmppServiceData(String dataString) {

        Log.d("dataString", dataString);

    }
}
