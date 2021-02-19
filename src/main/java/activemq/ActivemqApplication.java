package activemq;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.sql.SQLException;

public class ActivemqApplication {
    private static Logger log = Logger.getLogger(ActivemqApplication.class.getName());

    public static void main(String[] args) throws SQLException, InterruptedException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        log.info("ApplicationContext getting");

        DB db = context.getBean("db", DB.class);
        try{
            db.createDB();
        } catch (SQLException e) {
            log.error("Exception: {}", e);
        }

        Producer producer = context.getBean("producer", Producer.class);
        Consumer consumer = context.getBean("consumer", Consumer.class);
        producer.start();
        log.info("producerThread starting ");
        consumer.start();
        log.info("consumerThread starting ");


    }
}
