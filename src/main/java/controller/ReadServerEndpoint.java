package controller;

import org.apache.commons.collections4.CollectionUtils;
import util.Const;
import util.FileRW;
import util.NotifyWebSocketSessionUtils;
import util.PushWebSocketSessionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author lijie
 * @Class: ReadServerEndpoint 获得指定机器的消息列表
 * @Description: ReadServerEndpoint 暴漏给前端的websocket接口
 * <p>
 * machineId  指定机器编码，或者全部请求是all
 * requestUserId  请求消息的运维人员编号
 */

@ServerEndpoint(value = "/ws/read/{machineId}/{requestUserId}")
public class ReadServerEndpoint {

    private static String machineId;
    private static String requestUserId;

    //连接时执行
    @OnOpen
    public void onOpen(@PathParam("machineId") String machineId,
                       @PathParam("requestUserId") String requestUserId,
                       Session session) throws IOException {


        System.out.println(MessageFormat.format("新连接：{0}", requestUserId));
        this.machineId = machineId;
        this.requestUserId = requestUserId;
        NotifyWebSocketSessionUtils.add(MessageFormat.format("{0}|{1}", machineId, requestUserId), session);

        File folder = new File(this.getClass().getClassLoader().getResource(Const.SINGLE_MACHINE_MESSAGE_FOLDER).getPath());

        // 获得所有机器的消息文件列表
        if (machineId == Const.ALL) {

            String[] fileNames = folder.list();
            for (String fileName : fileNames) {
                Path p = Paths.get(folder.getPath(), fileName);
                List<String> fileLines = FileRW.getFileLines(p.toString());
                for (String line : fileLines) {
                    NotifyWebSocketSessionUtils.sendMessage(line, machineId);
                }
            }

        } else {
            // 获得指定机器的消息文件列表
            String fileName = MessageFormat.format(Const.SINGLE_MACHINE_MESSAGE_FILE_FORMAT, machineId);
            Path finaPath = Paths.get(folder.getPath(), fileName);

            if (!Files.exists(finaPath)) {
                NotifyWebSocketSessionUtils.sendMessage(MessageFormat.format("No messages for {0}", machineId), machineId);
                return;
            }
            List<String> fileLines = FileRW.getFileLines(finaPath.toString());
            if (CollectionUtils.isEmpty(fileLines)) {
                NotifyWebSocketSessionUtils.sendMessage(MessageFormat.format("No messages for {0}", machineId), machineId);
                return;
            }
            for (String line : fileLines) {
                NotifyWebSocketSessionUtils.sendMessage(line, machineId);
            }
        }
    }

    //关闭时执行
    @OnClose
    public void onClose() {

        System.out.println(MessageFormat.format("新连接：{0}", machineId));

        Session session = PushWebSocketSessionUtils.get(requestUserId);
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        NotifyWebSocketSessionUtils.remove(requestUserId);
    }

    //收到消息时执行
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        System.out.println(MessageFormat.format("收到机器{0}的消息{1}", machineId, message));
        session.getBasicRemote().sendText("收到 " + this.machineId + " 的消息 "); //回复用户
    }

    //连接错误时执行
    @OnError
    public void onError(Session session, Throwable error) {

        System.out.println(MessageFormat.format("机器id为：{0}的连接发送错误", machineId));
        error.printStackTrace();


        NotifyWebSocketSessionUtils.remove(requestUserId);
    }

}