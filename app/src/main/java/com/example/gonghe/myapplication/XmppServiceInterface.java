/**
 * Created with IntelliJ IDEA.
 * User: gonghe
 * Date: 2018/9/25
 * Time: 下午2:47
 * To change this template use File | Settings | File Templates.
 * Description: xmpp接口类
 */
package com.example.gonghe.myapplication;

public interface XmppServiceInterface {

    /**
     *
     * Description: 获取dataSting
     * @param dataString  推送过来的body的字符串
     *
     * */
    void getXmppServiceData(String dataString);

}
