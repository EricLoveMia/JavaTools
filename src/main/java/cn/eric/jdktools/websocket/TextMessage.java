package cn.eric.jdktools.websocket;

/**
 * @version 1.0.0
 * @description: 发送的实体
 * @author: eric
 * @date: 2022-11-18 15:34
 **/
public class TextMessage {

    public TextMessage(String message) {
        this.message = message;
    }

    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
