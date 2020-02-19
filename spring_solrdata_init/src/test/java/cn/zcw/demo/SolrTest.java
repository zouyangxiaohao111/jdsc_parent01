package cn.zcw.demo;


import cn.zcw.domain.TbItem;
import cn.zcw.mapper.TbItemMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext*.xml")
public class SolrTest {
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private TbItemMapper tbItemMapper;

    /**
     * 删除所有solr中数据
     */
    @Test
    public void delete(){
//        查询Solr中所有数据
        SolrDataQuery query = new SimpleQuery("*:*");
//        删除所有数据
        solrTemplate.delete(query);
//        提交事务
        solrTemplate.commit();

    }

    /**
     * 查询sku中所有数据并批量存储
     */
    @Test
    public void saveAll(){

//        查询所有sku
        List<TbItem> list = tbItemMapper.selectByExample(null);
//        储存数据（sku)
        solrTemplate.saveBeans(list);
//        提交
        solrTemplate.commit();
    }




}
