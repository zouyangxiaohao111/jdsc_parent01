package cn.zcw.service.impl;

import cn.zcw.domain.TbItem;
import cn.zcw.domain.TbItemExample;
import cn.zcw.mapper.TbItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

public class SolrListener implements MessageListener {
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private TbItemMapper tbItemMapper;

    /**
     *
     * 消费消息
     * @param message
     */
    @Override
    public void onMessage(Message message) {
       TextMessage textMessage =(TextMessage)  message;
        try {
            // 获取内容
            String sspuId = textMessage.getText();
            // 转换成long类型
            long spuId = Long.parseLong(sspuId);

            // 先查询出所有的sku的数据
            TbItemExample example = new TbItemExample();
            example.createCriteria().andGoodsIdEqualTo(spuId);
            List<TbItem> tbItemList = tbItemMapper.selectByExample(example);

            // 想solr库中保存数据
            // 批量的保存 sku
            solrTemplate.saveBeans(tbItemList);
            // 提交事务
            solrTemplate.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
