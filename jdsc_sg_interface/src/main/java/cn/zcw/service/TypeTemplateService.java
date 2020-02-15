package cn.zcw.service;

import cn.zcw.domain.TbTypeTemplate;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {
    PageInfo<TbTypeTemplate> findPage(int pageNum, int pageSize);

    void add(TbTypeTemplate tbTypeTemplate);

    void update(TbTypeTemplate tbTypeTemplate);

    TbTypeTemplate findOne(Long id);

    PageInfo<TbTypeTemplate> findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize);

    void delete(Long[] ids);

    List<TbTypeTemplate> findAll();

    List<Map> findSpecList(Long id);
}
