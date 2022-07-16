package util;


import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NotifyWebSocketSessionUtils {
    /**
     * session缓存
     */
    private static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    /**
     * 保存连接
     */
    public static void add(String sessionId, Session session) {
        sessions.put(sessionId, session);
    }

    /**
     * 获取连接
     */
    public static Session get(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * 移除连接
     */
    public static void remove(String sessionId) {
        sessions.remove(sessionId);
    }

    /**
     * 清空连接
     */
    public static void clear() {
        sessions.clear();
    }

    /**
     * 获取连接数
     */
    public static int getconnections() {
        return sessions.size();
    }

    /**
     * 是否存在
     */
    public static boolean exists(String sessionId) {
        if (sessions.containsKey(sessionId)) {
            return true;
        }
        return false;
    }

    /**
     * 发送消息到客户端
     */
    public static void sendMessage(String message, String machineId) {
        for (Map.Entry<String, Session> pair : sessions.entrySet()) {
            try {
                if (!pair.getKey().startsWith(Const.ALL) && !pair.getKey().startsWith(machineId)) {
                    continue;
                }

                if (pair.getValue().isOpen()) {
                    pair.getValue().getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                remove(pair.getKey());
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
