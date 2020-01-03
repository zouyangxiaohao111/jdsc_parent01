package cn.zcw.mapper;

import cn.zcw.domain.Brand;
/**
 * 商品Mapper接口
 */
import java.util.List;

public interface BrandMapper {
//    查询所有信息
    public List<Brand> findAll();
//    通过主键查询
    Brand findOne(Long id);
//    通过主键进行数据修改
    void update(Brand brand);
//    添加
    void insert(Brand brand);
//    删除
    void delete(Long id);
}
