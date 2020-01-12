package cn.zcw.service;

import cn.zcw.domain.TbItemCat;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ItemCatService {
    List<TbItemCat> findByParentId(Long pid);

    PageInfo<TbItemCat> findPage(int pageNum, int pageSize);

    TbItemCat findOne(Long id);

    void add(TbItemCat tbItemCat);

    void update(TbItemCat tbItemCat);

    List<TbItemCat> findAll();

    PageInfo<TbItemCat> findPage(TbItemCat itemCat, int pageNum, int pageSize);

    void delete(Long[] ids);

}
