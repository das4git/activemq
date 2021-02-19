package activemq;

import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.sql.SQLException;
import java.util.Random;

public class ConsumerMessageListener implements MessageListener {
    private static Logger log = Logger.getLogger(ConsumerMessageListener.class.getName());
    private String resultMessage;
    private String messageId;
    public DB db;

    public ConsumerMessageListener(DB db) {
        this.db = db;
    }

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
                messageId = message.getJMSMessageID();
                resultMessage = textMessage.getText();

                log.info("Received: " + resultMessage);
                insertResult(db, resultMessage, messageId);

        } catch (JMSException | SQLException e) {
            log.error("Exception: {}", e);
        }
    }

    public void insertResult(DB db, String resultMessage, String messageId) throws SQLException {
        Random r = new Random();
        int text_id = r.nextInt();
        db.insertFields1(text_id, resultMessage);
        db.insertFields2(r.nextInt(), text_id, messageId);
        log.info("Values inserted into DB");
    }
}
