package cn.zcw.service.impl;

import cn.zcw.domain.TbSpecificationOption;
import cn.zcw.domain.TbSpecificationOptionExample;
import cn.zcw.domain.TbTypeTemplate;
import cn.zcw.domain.TbTypeTemplateExample;
import cn.zcw.mapper.TbSpecificationOptionMapper;
import cn.zcw.mapper.TbTypeTemplateMapper;
import cn.zcw.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 模板类型实现类
 * @service 将服务暴露在内网中
 * @Transactional 开启事务管理器
 */
@Transactional
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {
//    注入mapper
    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;
    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;


    @Override
    public PageInfo<TbTypeTemplate> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
//        查询所有 null表示查询所有
        List<TbTypeTemplate> list = typeTemplateMapper.selectByExample(null);
        PageInfo<TbTypeTemplate> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void add(TbTypeTemplate tbTypeTemplate) {
        typeTemplateMapper.insert(tbTypeTemplate);
    }

    @Override
    public void update(TbTypeTemplate tbTypeTemplate) {
        typeTemplateMapper.updateByPrimaryKey(tbTypeTemplate);
    }

    @Override
    public TbTypeTemplate findOne(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }
    /**
     * 条件查询
     * @param typeTemplate
     * @param pageNum 当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageInfo<TbTypeTemplate> findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
        // 设置当前页和每页条数
        PageHelper.startPage(pageNum, pageSize);

        // 拼接查询条件
        TbTypeTemplateExample example=new TbTypeTemplateExample();
        TbTypeTemplateExample.Criteria criteria = example.createCriteria();

        if(typeTemplate!=null){
            if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
                criteria.andNameLike("%"+typeTemplate.getName()+"%");
            }
            if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
                criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
            }
            if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
                criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
            }
            if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
                criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
            }
        }

        // 查询所有
        List<TbTypeTemplate> list = typeTemplateMapper.selectByExample(example);
        // 封装数据
        PageInfo<TbTypeTemplate> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            typeTemplateMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<TbTypeTemplate> findAll() {
        return typeTemplateMapper.selectByExample(null);
    }

    @Override
    public List<Map> findSpecList(Long id) {
        // 通过模板的主键查询模板对象
        TbTypeTemplate template = typeTemplateMapper.selectByPrimaryKey(id);
        // 获取规格冗余字段的值
        String specIds = template.getSpecIds();
        // 使用fastjson工具类把json字符串转换回对象
        List<Map> list = JSON.parseArray(specIds, Map.class);
        // 遍历集合
        for (Map map : list) {
            // 获取到的规格的主键值
            String specId = map.get("id")+"";
            // 转换成long类型
            long sid = Long.parseLong(specId);
            // 查询规格项的数据

            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            example.createCriteria().andSpecIdEqualTo(sid);
            // 通过外键查询
            List<TbSpecificationOption> options = tbSpecificationOptionMapper.selectByExample(example);
            // 存入到map中
            map.put("options",options);
        }
        return list;
    }
}
