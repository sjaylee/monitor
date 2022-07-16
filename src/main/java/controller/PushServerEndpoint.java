package controller;

import com.alibaba.fastjson.JSON;
import dto.MachineMessage;
import util.Const;
import util.FileRW;
import util.NotifyWebSocketSessionUtils;
import util.PushWebSocketSessionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * @author lijie
 * @Class: PushServerEndpoint
 * @Description: 简单websocket demo
 */

@ServerEndpoint(value = "/ws/push/{machineId}")
public class PushServerEndpoint {

    private static String machineId;

    //连接时执行
    @OnOpen
    public void onOpen(@PathParam("machineId") String machineId, Session session) throws IOException {
        this.machineId = machineId;
        System.out.println(MessageFormat.format("新连接：{0}", machineId));

        PushWebSocketSessionUtils.add(machineId, session);
    }

    //关闭时执行
    @OnClose
    public void onClose() throws IOException {

        System.out.println(MessageFormat.format("新连接：{0}", machineId));

        Session session = PushWebSocketSessionUtils.get(machineId);
        if (session != null && session.isOpen()) {
            session.close();
        }
        PushWebSocketSessionUtils.remove(machineId);
    }

    //收到消息时执行
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        System.out.println(MessageFormat.format("收到机器{0}的消息{1}", machineId, message));
        session.getBasicRemote().sendText("收到 " + this.machineId + " 的消息 "); //回复用户

        // Once receive a message form some machine, then transform the message to the observer user.
        // It is supposed have only one observer, if exists more than one observer, please send message to all observers.
        NotifyWebSocketSessionUtils.sendMessage(message, machineId);


        // Save the messages. It should be designed save into the DB, but for demo, I save the message to the local file.
        saveMessage(message, machineId);
    }

    private void saveMessage(String coreInfoString, String machinId) throws IOException {
        File folder = new File(this.getClass().getClassLoader().getResource(Const.SINGLE_MACHINE_MESSAGE_FOLDER).getPath());
        String fileName = MessageFormat.format(Const.SINGLE_MACHINE_MESSAGE_FILE_FORMAT, machineId);
        Path finaPath = Paths.get(folder.getPath(), fileName);
        File file = new File(finaPath.toString());
        if (!file.exists()) {
            file.createNewFile();
        }
        try {
            MachineMessage messageObj = JSON.parseObject(coreInfoString, MachineMessage.class);
            FileRW.appendLinesToFile(file.getPath(), Arrays.asList(JSON.toJSONString(messageObj)));
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
        }
    }

    //连接错误时执行
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println(MessageFormat.format("机器id为：{0}的连接发送错误", machineId));
        error.printStackTrace();

        PushWebSocketSessionUtils.remove(machineId);
    }

}