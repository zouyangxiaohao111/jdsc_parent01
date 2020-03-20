package cn.zcw.service;

import cn.zcw.domain.TbGoods;
import cn.zcw.groupentity.Goods;
import com.github.pagehelper.PageInfo;

/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
public interface GoodsService {

    // 保存商品
    void add(Goods goods);

    PageInfo<TbGoods> findPage(int pageNum, int pageCode);

    void updateGoods(String val, Long[] ids);
}
