package cn.itcast.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/testsendSms")
@RestController
public class MQController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @GetMapping("/sendSms")
    public String SendSms(){
        Map<String,String> map = new HashMap<>();
        map.put("mobile","18719014661");
        map.put("signName","黑马");
        map.put("templateCode","SMS_125018593");
        map.put("templateParam","{\"code\":\"1234\"}");
        jmsMessagingTemplate.convertAndSend("itcast_sms",map);
        return "OK";
    }
}
