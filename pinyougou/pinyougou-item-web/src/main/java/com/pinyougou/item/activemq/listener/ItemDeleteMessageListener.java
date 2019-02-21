package com.pinyougou.item.activemq.listener;

import com.pinyougou.vo.Goods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.File;

public class ItemDeleteMessageListener extends
        AbstractAdaptableMessageListener {

    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        ObjectMessage objectMessage = (ObjectMessage)message;
        Long[] goodsIds = (Long[]) objectMessage.getObject();
        for (Long goodsId : goodsIds){
            File file = new File(ITEM_HTML_PATH + goodsId +".html");
            if (file.exists()){
                file.delete();
            }
        }
    }
}
