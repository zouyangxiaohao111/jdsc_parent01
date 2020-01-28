package cn.zcw.service.impl;

import cn.zcw.domain.TbGoods;
import cn.zcw.mapper.TbGoodsMapper;
import cn.zcw.service.GoodService;
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
@Service
@Transactional
public class GoodServiceImpl implements GoodService {
    @Autowired
    private TbGoodsMapper tbGoodsMapper;

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<TbGoods> findAll() {
//        条件查询。null代表查询所有
        List<TbGoods> list = tbGoodsMapper.selectByExample(null);
        return list;
    }

    /**
     * 分页查询所有数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<TbGoods> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        List<TbGoods> list = tbGoodsMapper.selectByExample(null);
        PageInfo<TbGoods> pageInfo = new PageInfo<>(list);
        return  pageInfo;
    }
    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbGoodsMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 批量修改
     * @param status
     * @param ids
     */
    @Override
    public void updateAuditStatus(String status, Long[] ids) {
        for (Long id : ids) {
//            TbGoodsExample example = new TbGoodsExample();
//            example.createCriteria();
            TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(id);
            tbGoods.setAuditStatus(status);
            tbGoodsMapper.updateByPrimaryKey(tbGoods);

        }
    }
}
