package cn.zcw.service.impl;

import cn.zcw.domain.TbGoods;
import cn.zcw.domain.TbGoodsDesc;
import cn.zcw.domain.TbItem;
import cn.zcw.groupentity.Goods;
import cn.zcw.mapper.*;
import cn.zcw.service.GoodService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Service 开启服务，将接口暴露在内网中
 * @Transactional 开启事务
 */
@Service
@Transactional
public class GoodServiceImpl implements GoodService {
//    注入容器内的mapper
    @Autowired
    private TbGoodsMapper tbGoodsMapper;
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private TbSellerMapper tbSellerMapper;
    @Autowired
    private TbItemMapper tbItemMapper;

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

    @Override
    public void add(Goods goods) {

//        获取tbgoos对象
        TbGoods tbGoods = goods.getTbGoods();
//        设置sellId
        tbGoods.setSellerId("ceshi");
//        设置未审批
        tbGoods.setAuditStatus("0");
//        设置未上架
        tbGoods.setIsMarketable("0");
//        保存spu
        tbGoodsMapper.insert(tbGoods);
//        获得sku对象
        TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
//        设置外键依赖
        tbGoodsDesc.setGoodsId(tbGoods.getId());
//        保存spu
        tbGoodsDescMapper.insert(tbGoodsDesc);

//        SKU
        List<TbItem> itemList = goods.getItemList();

        if (itemList != null && itemList.size()>0){

//            设置属性
            for (TbItem tbItem : itemList) {
//                设置时间
                tbItem.setCreateTime(new Date());
//                价格
                tbItem.setPrice(tbGoods.getPrice());
//                goodId
                tbItem.setGoodsId(tbGoodsDesc.getGoodsId());
//                设置分类名称
                tbItem.setCategory(tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName());
//                设置分类ID
                tbItem.setCategoryid(tbGoods.getCategory3Id());
//                设置商品名称
                tbItem.setBrand(brandMapper.findOne(tbGoods.getBrandId()).getName());
//                设置商家Id
                tbItem.setSellerId(tbGoods.getSellerId());

//                商家名称
                tbItem.setSeller(tbSellerMapper.selectByPrimaryKey(tbGoods.getSellerId()).getName());
//                获取图片地址
                String itemImages = tbGoodsDesc.getItemImages();
                if (itemImages != null && !itemImages.isEmpty()){
//                  获取字符数组"url":"http://192.168.25.133/group1...,并转换为map
                    List<Map> mapList = JSON.parseArray(itemImages, Map.class);
                    Map map = mapList.get(0);
//                    获得url
                    String url = (String) map.get("url");
                    tbItem.setImage(url);
                }
//                获取spu名字
                String goodsName = tbGoods.getGoodsName();
//                获取Spc，此数据由前端传入
                String spec = tbItem.getSpec();
                Map map = JSON.parseObject(spec, Map.class);

                for (Object key : map.keySet()) {
                    goodsName += " "+key+" "+map.get(key);

                }
//                设置标题
                tbItem.setTitle(goodsName);
//                设置更新时间
                tbItem.setUpdateTime(new Date());
                // 把sku存入数据库中
                tbItemMapper.insert(tbItem);
            }
        }
    }



}
