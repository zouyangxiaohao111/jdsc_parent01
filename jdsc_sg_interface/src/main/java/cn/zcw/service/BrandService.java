package cn.zcw.service;

import cn.zcw.domain.Brand;
import com.github.pagehelper.PageInfo;
/**
 * 商品服务接口
 */
import java.util.List;
import java.util.Map;

public interface BrandService {
//    查询所有商品
     List<Brand> findAll();
//    分页查询 返回数据并通过pageInfo封装
    PageInfo<Brand> findPage(int pageNum, int pageSize);
//    通过主键查询
    Brand findOne(Long id);
//    修改
    void update(Brand brand);
//    添加
    void save (Brand brand);

    void delete(Long[] ids);

    List<Map<String,Object>> findBrandList();

}
