package activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;

public class Consumer extends Thread  {
    private static Logger log = Logger.getLogger(Consumer.class.getName());
    private String brokerUrl;
    private DB db;

    public Consumer() throws Exception {
    }

    public String getBrokerUrl() { return brokerUrl;}

    public void setBrokerUrl(String brokerUrl) {this.brokerUrl = brokerUrl;}

    public DB getDb() {return db;}

    public void setDb(DB db) {this.db = db;}

    public void run() {
        try {


            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    brokerUrl);
            Connection connection = connectionFactory.createConnection();

            connection.start();
            log.info("Consumer connection starting");
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("TEST.FOO");
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new ConsumerMessageListener(db));

            Thread.sleep(3000);
            consumer.close();
            session.close();
            connection.close();
            log.info("Consumer connection finishing");
        } catch (Exception e) {
            log.error("Exception: {}", e);
        }
    }
}