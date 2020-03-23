package cn.zcw.service.impl;

import cn.zcw.domain.TbUser;
import cn.zcw.entity.Result;
import cn.zcw.mapper.TbUserMapper;
import cn.zcw.util.HttpClient;
import cn.zcw.util.RandomCode;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {
//    注入mapper
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbUserMapper tbUserMapper;

    /**
     * 发送短信验证码
     * @param phone
     */
    @Override
    public void sendCode(String phone){
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

    /**
     *
     * 注册
     * @param code
     * @param tbUser
     */
    @Override
    public Result register(String code, TbUser tbUser) {

//        获取redis缓存中的验证码
        String redisCode = (String) redisTemplate.boundValueOps(tbUser.getPhone()).get();

        if (redisCode == null|| !redisCode.equals(code)){
            return new Result(false,"验证码错误");
        }
//        设置创建时间
        tbUser.setCreated(new Date());
//        设置更新时间
        tbUser.setUpdated(new Date());
//        获得密码并加密
        String pword = tbUser.getPassword();
        String password = DigestUtils.md5Hex(pword);
//        重新放入tbUser
        tbUser.setPassword(password);
//        注册用户
        tbUserMapper.insert(tbUser);
        return new Result(true,"注册成功");


    }


}
