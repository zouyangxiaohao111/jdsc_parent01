package cn.zcw.service;

import cn.zcw.domain.TbGoods;
import cn.zcw.groupentity.Goods;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface GoodService {
    List<TbGoods> findAll();

    PageInfo<TbGoods> findPage(int pageNum, int pageSize);

    void delete(Long[] ids);

    void updateAuditStatus(String status, Long[] ids);

    void add(Goods goods);

}
