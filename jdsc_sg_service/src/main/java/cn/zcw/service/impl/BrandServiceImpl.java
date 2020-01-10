package cn.zcw.service.impl;

import cn.zcw.domain.Brand;
import cn.zcw.mapper.BrandMapper;
import cn.zcw.service.BrandService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品服务接口实现类
 *
 * @Service 实现服务的转发
 *
 */
//事务开启的注解
@Transactional
@Service
public class BrandServiceImpl implements BrandService {
//    从IOC容器中注入mapper对象
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 接口方法的实现,查询所有数据
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandMapper.findAll();
    }

    /**
     * 通过pageHelper插件进行分页
     * @param pageNum
     * @param pageSize
     * @return pageInfo
     */
    @Override
    public PageInfo<Brand> findPage(int pageNum, int pageSize) {

//        初始化pageHelper获取第pageNum页pageSize条数据  pageNum代表当前页，pageSize代表当前页所显示的数量
        PageHelper.startPage(pageNum,pageSize);
//        查询所有数据
        List<Brand> list = brandMapper.findAll();
//        将查询的数据进行封装,此时有pageHelper中pageinfo完成
        PageInfo<Brand> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 通过主键进行查询数据
     * @param id
     * @return
     */
    @Override
    public Brand findOne(Long id) {
        return brandMapper.findOne(id);
    }

    /**
     * 更新
     * @param brand
     */
    @Override
    public void update(Brand brand) {
            brandMapper.update(brand);
    }

    /**
     * 保存
     * @param brand
     */
    @Override
    public void save(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.delete(id);
        }
    }

    @Override
    public List<Map<String, Object>> findBrandList() {
        List<Brand> list = brandMapper.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Brand brand : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",brand.getId());
            map.put("text",brand.getName());
            mapList.add(map);
        }
        return mapList;

    }


}
