package cn.zcw.service.boot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class DubboSearchService {
    public static void main(String[] args) throws IOException {
        // 只需要加载Spring的配置文件即可
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        System.in.read();
    }
}
