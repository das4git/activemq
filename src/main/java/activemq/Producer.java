package activemq;

import org.apache.log4j.Logger;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Producer extends Thread  {
    private static Logger log = Logger.getLogger(Producer.class.getName());

    private  String brokerUrl;

    public String getBrokerUrl() {return brokerUrl;}

    public void setBrokerUrl(String brokerUrl) {this.brokerUrl = brokerUrl;}

    public void run() {
        try {

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    brokerUrl);

            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("TEST.FOO");
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss,SSS");

            String text = "Hello, World! From: " + Thread.currentThread().getName() +
                    "  time="+ formater.format(new Date());
            TextMessage message = session.createTextMessage(text);

            for (int i=0; i<10; i++){
                producer.send(message);
                log.info("Sent: "+((TextMessage)message).getText());
            }


            session.close();
            connection.close();
        } catch (Exception e) {
            log.error("Exception: {}",e);
        }
    }
}