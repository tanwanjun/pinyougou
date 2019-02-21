package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.util.PhoneFormatCheckUtils;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;
import com.pinyougou.vo.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/user")
@RestController
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 注册用户
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody TbUser user,String smsCode){
        try {
            if (userService.checkSmsCode(user.getPhone(),smsCode)){
                user.setCreated(new Date());
                user.setUpdated(user.getCreated());
                user.setPassword(DigestUtils.md5Hex(user.getPassword()));
                userService.add(user);
                return Result.ok("注册成功");
            }else {
                return Result.fail("验证码不正确");
            }
               }catch (Exception e){
                   e.printStackTrace();
               }
        return Result.fail("注册失败");
    }

    /**
     * 发送短信验证码
     * @param phone
     */
    @GetMapping("/sendSmsCode")
    public Result sendSmsCode(String phone){
        try {
                   if (PhoneFormatCheckUtils.isPhoneLegal(phone)){
                       userService.sendSmsCode(phone);
                       return Result.ok("发送短信成功！");
                   }else {
                       return Result.fail("手机号码格式错误，发送短信失败！");
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
        return Result.fail("发送短信失败！");
    }

}
