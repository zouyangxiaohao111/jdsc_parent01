package cn.zcw.service;

import cn.zcw.domain.TbTypeTemplate;
import com.github.pagehelper.PageInfo;

public interface TypeTemplateService {
    PageInfo<TbTypeTemplate> findPage(int pageNum, int pageSize);

    void add(TbTypeTemplate tbTypeTemplate);

    void update(TbTypeTemplate tbTypeTemplate);

    TbTypeTemplate findOne(Long id);

    PageInfo<TbTypeTemplate> findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize);

    void delete(Long[] ids);

}
