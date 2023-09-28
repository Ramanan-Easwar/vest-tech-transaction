package com.org.investechtransaction.helper;

import com.org.investechtransaction.db.TransactionDbDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;


public class RabbitMqListener implements MessageListener {

    Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);
    TransactionDbDao transactionDbDao;
    @Override
    public void onMessage(Message message) {
        logger.info("We've received the following: {}", message.getMessageProperties());
        byte[] body = message.getBody();
        logger.info("getting string manually: {}", transactionDbDao.getTransactionByUser(
                new String(body)));
    }
}
