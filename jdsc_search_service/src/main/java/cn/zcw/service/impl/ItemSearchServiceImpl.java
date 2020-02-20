package cn.zcw.service.impl;

import cn.zcw.domain.TbItem;
import cn.zcw.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;


    /**
     * 使用solr进行优化
     * 并查询信息
     * @param paramMap
     * @return
     */
    @Override
    public Map search(Map paramMap) {
//        返回结果集
        Map resultMap = new HashMap();
//        =======================主条件查询========================
//        获取keyword查询条件
        String  keyword =(String) paramMap.get("keyword");
//        创建查询条件
        Query query = new SimpleQuery();
//        添加条件
        query.addCriteria(new Criteria("item_keywords").is(keyword));

        // 设置分页查询
        int pageNum = (int) paramMap.get("page");
        // 起始位置
        query.setOffset((pageNum - 1)*60);
        // 每页显示条数
        query.setRows(60);

        // 分页条件查询
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        // 分页的数据
        List<TbItem> tbItemList = page.getContent();
        // 把数据存入到resultMap

        resultMap.put("rows",tbItemList);
        // 存储总页数
        resultMap.put("totalPage",page.getTotalPages());
        // 存储总条数
        resultMap.put("total",page.getTotalElements());


        return resultMap;
    }
}
