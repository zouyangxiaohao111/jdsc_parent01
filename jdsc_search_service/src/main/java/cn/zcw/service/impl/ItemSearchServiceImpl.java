package cn.zcw.service.impl;

import cn.zcw.domain.TbItem;
import cn.zcw.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.ArrayList;
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

        // =======================分类分组查询================================
        List<String> categoryList = new ArrayList<>();
        // 产生类似SQL select item_category from tb_item where item_keywords='手机' group by item_category
        // 主查询
        Query categoryQuery = new SimpleQuery();
        // 添加查询条件
        categoryQuery.addCriteria(new Criteria("item_keywords").is(keyword));
        // 创建GroupOptions对象
        GroupOptions groupOptions = new GroupOptions();
        // 按业务域去分组
        groupOptions.addGroupByField("item_category");
        // 设置分组
        categoryQuery.setGroupOptions(groupOptions);
        // 分组查询
        GroupPage<TbItem> categoryPage = solrTemplate.queryForGroupPage(categoryQuery, TbItem.class);
        // 获取分类的组的名称
        GroupResult<TbItem> item_category = categoryPage.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> groupEntries = item_category.getGroupEntries();
        for (GroupEntry<TbItem> tbItemGroupEntry : groupEntries.getContent()) {
            String value = tbItemGroupEntry.getGroupValue();
            categoryList.add(value);
        }
        resultMap.put("categoryList",categoryList);


        // =======================品牌分组查询================================
        List<String> brandList = new ArrayList<>();
        // 主查询
        Query brandQuery = new SimpleQuery();
        // 添加查询条件
        brandQuery.addCriteria(new Criteria("item_keywords").is(keyword));

        // 创建GroupOptions对象
        GroupOptions brandGroupOptions = new GroupOptions();
        // 按业务域去分组
        brandGroupOptions.addGroupByField("item_brand");
        // 设置分组
        brandQuery.setGroupOptions(brandGroupOptions);

        // 分组查询
        GroupPage<TbItem> brandPage = solrTemplate.queryForGroupPage(brandQuery, TbItem.class);
        // 获取分类的组的名称
        GroupResult<TbItem> item_brand = brandPage.getGroupResult("item_brand");
        Page<GroupEntry<TbItem>> brandGroupEntries = item_brand.getGroupEntries();
        for (GroupEntry<TbItem> tbItemGroupEntry : brandGroupEntries.getContent()) {
            String value = tbItemGroupEntry.getGroupValue();
            brandList.add(value);
        }
        resultMap.put("brandList",brandList);


        return resultMap;
    }
}
