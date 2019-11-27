/*
package data.sysapi.service.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class activeMQ {
    */
/**
     * activeMQ发送消息.
     * @param msg        消息
     * @param topicName topic名称
     * @param sendId 地址
     *//*

    public static void sendMsg(String msg,String topicName,String sendId) throws Exception{
        //1、创建工厂连接对象，需要制定ip和端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(sendId);
        //2、使用连接工厂创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、使用连接对象创建会话（session）对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
        Topic topic = session.createTopic(topicName);
        //6、使用会话对象创建生产者对象
        MessageProducer producer = session.createProducer(topic);
        //7、使用会话对象创建一个消息对象
        TextMessage textMessage = session.createTextMessage(msg);
        //8、发送消息
        producer.send(textMessage);
        //9、关闭资源
        producer.close();
        session.close();
        connection.close();
    }
}
*/
