package cn.zcw.controller;

import cn.zcw.domain.TbItem;
import cn.zcw.groupentity.Goods;
import cn.zcw.service.ItempageService;
import com.alibaba.dubbo.config.annotation.Reference;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPageListener implements MessageListener {
    @Reference
    private ItempageService itempageService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
//            将订阅模式中的消息进行转化
            Long goodId = Long.parseLong(textMessage.getText());
            //调用业务层
            Goods goods = itempageService.findByGoodsId(goodId);
            // 生成详情页
            // 获取配置对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            List<TbItem> itemList = goods.getItemList();
            for (TbItem tbItem : itemList) {
                Map map = new HashMap();
                map.put("goods",goods);
                map.put("tbItem",tbItem);
                FileWriter fileWriter = new FileWriter(new File("D:\\pages\\"+tbItem.getId()+".html" ) );
                template.process(map,fileWriter);
                fileWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
