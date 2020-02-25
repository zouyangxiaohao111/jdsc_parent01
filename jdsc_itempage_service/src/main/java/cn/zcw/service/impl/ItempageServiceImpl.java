package cn.zcw.service.impl;

import cn.zcw.domain.TbGoods;
import cn.zcw.domain.TbGoodsDesc;
import cn.zcw.domain.TbItem;
import cn.zcw.domain.TbItemExample;
import cn.zcw.groupentity.Goods;
import cn.zcw.mapper.TbGoodsDescMapper;
import cn.zcw.mapper.TbGoodsMapper;
import cn.zcw.mapper.TbItemCatMapper;
import cn.zcw.mapper.TbItemMapper;
import cn.zcw.service.ItempageService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItempageServiceImpl implements ItempageService {
//    注入mapper
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbGoodsMapper tbGoodsMapper;
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;


    /**
     * 通过id查找sku对象，和spu对象并返回到对象组中
     * @param goodsId
     * @return
     */
    @Override
    public Goods findByGoodsId(Long goodsId) {
        Goods goods = new Goods();
//        获取sku对象
        TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodsId);
//        设置spu对象
        goods.setTbGoods(tbGoods);
//        获取描述对象
        TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(goodsId);

//        设置描述对象
        goods.setTbGoodsDesc(tbGoodsDesc);
//        获取sku对象
        TbItemExample example = new TbItemExample();
        example.createCriteria().andGoodsIdEqualTo(tbGoods.getId());
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
//        设置sku对象
        goods.setItemList(tbItems);
//        获取分类名称,创建map集合
        Map<String,String > categoryMap = new HashMap<>();
        categoryMap.put("category1",tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName());
        categoryMap.put("category2",tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName());
        categoryMap.put("category3",tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName());
//        设置分类名称
        goods.setCategoryMap(categoryMap);



        return goods;
    }

    /**
     * 查找所有sku对象,并将其放入list集合中
     *
     * @return
     */
    @Override
    public List<Goods> findByGoodsIdAll() {
        List<Goods> goodsList = new ArrayList<>();
        List<TbGoods> goodsListAll = tbGoodsMapper.selectByExample(null);
        for (TbGoods tbGoods : goodsListAll) {
            // 调用上面的方法，获取组合类
            Goods goods = findByGoodsId(tbGoods.getId());
            goodsList.add(goods);
        }
        return goodsList;
    }
}
