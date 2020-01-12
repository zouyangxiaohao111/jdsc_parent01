package cn.zcw.service.impl;

import cn.zcw.domain.TbItemCat;
import cn.zcw.domain.TbItemCatExample;
import cn.zcw.mapper.TbItemCatMapper;
import cn.zcw.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Service 开启服务，将接口暴露在内网中
 * @Transactional 开启事务
 */
@Transactional
@Service
public class ItemCatServiceImpl implements ItemCatService {
//    通过spring容器注入mapper
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    /**
     * 通过父id查询
     * @param pid
     * @return
     */
    @Override
    public List<TbItemCat> findByParentId(Long pid) {
        TbItemCatExample example = new TbItemCatExample();
        example.createCriteria().andParentIdEqualTo(pid);
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
        return list;
    }

    /**
     * 分页查询逻辑
     * 数据分页即可
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<TbItemCat> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<TbItemCat> list = tbItemCatMapper.selectByExample(null);
        PageInfo<TbItemCat> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(id);
        return tbItemCat;
    }

    /**
     * 添加
     * @param tbItemCat
     */
    @Override
    public void add(TbItemCat tbItemCat) {
        tbItemCatMapper.insert(tbItemCat);
    }

    /**
     * 更新
     * @param tbItemCat
     */
    @Override
    public void update(TbItemCat tbItemCat) {
        tbItemCatMapper.updateByPrimaryKey(tbItemCat);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<TbItemCat> findAll() {
//        null表示查询所有数据
        List<TbItemCat> list = tbItemCatMapper.selectByExample(null);
        return list;
    }
    /**
     * 条件分页查询
     *
     * @param itemCat
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageInfo<TbItemCat> findPage(TbItemCat itemCat, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
//        创建模糊查询sql语句
        if (itemCat != null) {
            if (itemCat.getName() != null && itemCat.getName().length() > 0) {
                criteria.andNameLike("%" + itemCat.getName() + "%");
            }

        }
        // 条件查询
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbItemCatMapper.deleteByPrimaryKey(id);
        }

    }
}
