package cn.zcw.service;

import cn.zcw.domain.TbContentCategory;
import com.github.pagehelper.PageInfo;

public interface CategoryService {
    PageInfo<TbContentCategory> findPage(int pageNum, int pageSize);

    void update(TbContentCategory tbContentCategory);

    void save(TbContentCategory tbContentCategory);


    TbContentCategory  findOne(Long id);

    void delete(Long[] ids);
}
