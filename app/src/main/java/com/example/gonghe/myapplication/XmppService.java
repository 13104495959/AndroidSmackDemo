/**
 * Created with IntelliJ IDEA.
 * User: gonghe
 * Date: 2018/9/25
 * Time: 下午2:47
 * To change this template use File | Settings | File Templates.
 * Description: xmpp工具类
 */
package com.example.gonghe.myapplication;

import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.packet.Message;
import org.minidns.record.A;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

public class XmppService {

    /**
     *
     * xmppService 对象
     *
     **/
    private static XmppService xmppService = new XmppService();

    /**
     *
     * xmpp连接对象
     *
     * */
    private AbstractXMPPConnection abstractXMPPConnection = null;

    /**
     *
     * xmpp连接参数
     *
     * */
    private int SERVER_PORT = 5222;
    private String SERVER_HOST = "192.168.1.34";
    private String SERVER_DOMAINNAME = "fss";

    /**
     *
     * 连接监听器
     *
     * */
    private ConnectionListener connectionListener;

    /**
     *
     * 接受数据监听器
     *
     * */
    private StanzaListener stanzaListener;

    /**
     *
     * 接口变量
     *
     * */
    public XmppServiceInterface xmppServiceInterface;

    /**
     * Description: 单例模式
     *
     * @return xmppService
     */
    public synchronized static XmppService getInstance() {

        return xmppService;

    }

    /**
     *
     * Description: 必须先设置这个方法,否则会连接默认的测试环境192.168.1.34: 5222 fss
     *
     * @param SERVER_PORT  端口
     * @param SERVER_HOST  ip
     * @param SERVER_NAME  domain
     *
     * */
    public void setServicearameter(int SERVER_PORT, String SERVER_HOST, String SERVER_NAME) {

        this.SERVER_PORT = SERVER_PORT;
        this.SERVER_HOST = SERVER_HOST;
        this.SERVER_DOMAINNAME = SERVER_NAME;

    }

    /**
     *
     * 设置xmpp连接
     *
     * */
    public boolean setXmppConnection(String account, String password) {

        try {

            if (null == abstractXMPPConnection || !abstractXMPPConnection.isAuthenticated()) {

                SmackConfiguration.DEBUG = true;
                XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
                //设置openfire主机IP
                config.setHostAddress(InetAddress.getByName(SERVER_HOST));
                //设置openfire服务器名称
                config.setXmppDomain(SERVER_DOMAINNAME);
                //设置端口号：默认5222
                config.setPort(SERVER_PORT);
                //禁用SSL连接
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).setCompressionEnabled(false);
                //设置离线状态
                config.setSendPresence(true);
                // 开启通讯压缩
                config.setCompressionEnabled(true);

                abstractXMPPConnection = new XMPPTCPConnection(config.build());
                abstractXMPPConnection.connect();// 连接到服务器

                loginXmpp(account,password);

                return true;

            }

        } catch (XMPPException | SmackException | IOException | InterruptedException xe) {

            xe.printStackTrace();
            abstractXMPPConnection = null;

        }

        return false;

    }

    /**
     *
     * 创建xmpp连接
     *
     * */
    public AbstractXMPPConnection creatXmppConnection(final String account, final String password) {

        if (abstractXMPPConnection == null) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    setXmppConnection(account, password);

                }

            }).start();

        }

        return abstractXMPPConnection;

    }

    /**
     * 登录
     *
     * @param account  登录帐号
     * @param password 登录密码
     * @return true登录成功
     */
    public boolean loginXmpp(String account, String password) {
        try {
            if (creatXmppConnection(account, password) == null) {

                return false;

            } else {

                creatXmppConnection(account, password).login(account, password);

                // 添加接受监听器

                // 注意 手动导入 import org.jivesoftware.smack.packet.Message; 头文件
                StanzaTypeFilter filter = new StanzaTypeFilter(Message.class);

                stanzaListener = new StanzaListener() {
                    @Override
                    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {

                        Message message = (Message) packet;
                        if (message.getBody() != null) {

                            String bodyString = String.valueOf(message.getBody());

                            if (bodyString != null) {

                                Log.d("--------------", bodyString);

                                xmppServiceInterface.getXmppServiceData(bodyString);

                            }

                        }

                    }
                };

                abstractXMPPConnection.addSyncStanzaListener(stanzaListener, filter);

                return true;

            }

        } catch (XMPPException | IOException | SmackException | InterruptedException xe) {

            xe.printStackTrace();

        }

        return false;

    }

}










