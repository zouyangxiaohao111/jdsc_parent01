package cn.zcw.service.impl;

import cn.zcw.mapper.TbUserMapper;
import cn.zcw.util.HttpClient;
import cn.zcw.util.RandomCode;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {
//    注入mapper
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public void sendCode(String phone)    {
//        获得随机生成的6位数
        String code = RandomCode.genCode();

//        将六位数存进redis缓存中,code验证码，5分钟,TimeUnit是单位
        redisTemplate.boundValueOps(phone).set(code,5, TimeUnit.MINUTES);

//        发送http请求到sms服务中
        HttpClient httpClient = new HttpClient("http://localhost:8087/sms/send/"+phone+"/"+code);

//        发送请求
        try {
            httpClient.get();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
