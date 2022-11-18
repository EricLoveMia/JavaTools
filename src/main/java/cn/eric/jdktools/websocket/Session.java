package cn.eric.jdktools.websocket;

import java.io.IOException;

/**
 * @version 1.0.0
 * @description: 会话
 * @author: eric
 * @date: 2022-11-18 15:33
 **/
public interface Session {

    void sendMessage(TextMessage textMessage) throws IOException;
}
