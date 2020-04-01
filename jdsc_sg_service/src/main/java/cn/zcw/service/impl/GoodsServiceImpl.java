package cn.zcw.service.impl;

import cn.zcw.domain.TbGoods;
import cn.zcw.domain.TbGoodsDesc;
import cn.zcw.domain.TbItem;
import cn.zcw.groupentity.Goods;
import cn.zcw.mapper.*;
import cn.zcw.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class GoodsServiceImpl implements GoodsService{

    @Autowired
    private TbGoodsMapper tbGoodsMapper;

    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbSellerMapper tbSellerMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    /**
     * 保存商品
     * @param goods
     */
    @Override
    public void add(Goods goods) {
        // 先获取到spu的对象
        TbGoods tbGoods = goods.getTbGoods();
        tbGoods.setSellerId("ceshi");
        // 设置属性
        // 表示未上架
        tbGoods.setIsMarketable("0");
        // 未审批状态
        tbGoods.setAuditStatus("0");
        // 保存spu对象
        tbGoodsMapper.insert(tbGoods);

        // ==============================================

        // 再保存商品的描述对象
        TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
        // 表的关系是一对一，主键值是相同的
        tbGoodsDesc.setGoodsId(tbGoods.getId());
        // 保存
        tbGoodsDescMapper.insert(tbGoodsDesc);

        // ==============================================

        // 保存sku的数据
        List<TbItem> itemList = goods.getItemList();
        // 判断
        if(itemList != null && itemList.size() > 0){
            // 遍历 sku对象
            for (TbItem tbItem : itemList) {
                // 把属性的值都需要设置
                // 商家的名称
                tbItem.setSeller("ceshi");
                // 商家的主键
                tbItem.setSellerId("ceshi");
                // 品牌的名称
                tbItem.setBrand(brandMapper.findOne(tbGoods.getBrandId()).getName());
                // 分类的名称
                tbItem.setCategory(tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName());

                // 先获取图片
                String itemImages = tbGoodsDesc.getItemImages();
                // [{"color":"白色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhV4UgHWAeTEhAABXg25FIxU423.jpg"},{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhV4UgIOAabhCAAARZvgKQGk169.jpg"}]
                // 判断
                if(itemImages != null && !itemImages.isEmpty()){
                    // 解析
                    List<Map> list = JSON.parseArray(itemImages, Map.class);
                    // 从list集合中获取第1个map
                    // {"color":"白色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhV4UgHWAeTEhAABXg25FIxU423.jpg"}
                    Map map = list.get(0);
                    // 获取url属性
                    String url = (String) map.get("url");
                    // 设置图片
                    tbItem.setImage(url);
                }

                // 标题  spu的名称 颜色 黄色 尺码 175
                // 例如： 小米9  颜色 白色  内存 16G

                // 先获取到spu的名称
                String goodsName = tbGoods.getGoodsName();
                // 获取spec属性的值
                String spec = tbItem.getSpec();
                // spec = {"颜色":"黄色","款式":"双肩背包"}
                // 解析
                Map map = JSON.parseObject(spec, Map.class);
                // 遍历map集合 key 颜色  款式
                for (Object key : map.keySet()) {
                    goodsName += " " + key + " " + map.get(key);
                }
                // 设置标题
                tbItem.setTitle(goodsName);

                tbItem.setCreateTime(new Date());
                tbItem.setUpdateTime(new Date());
                // 分类的id
                tbItem.setCategoryid(tbGoods.getCategory3Id());
                // spu的id
                tbItem.setGoodsId(tbGoods.getId());

                // 把sku存入数据库中
                tbItemMapper.insert(tbItem);
            }
        }
    }

    @Override
    public PageInfo<TbGoods> findPage(int pageNum, int pageCode) {
        PageHelper.startPage(pageNum,pageCode);
        // 查询所有数据   自己搞定
        List<TbGoods> list = tbGoodsMapper.selectByExample(null);
        return new PageInfo<>(list);
    }

    // 模板
    @Autowired
    private JmsTemplate jmsTemplate;

    // 主题
    @Resource(name="topic")
    private Destination destination;

    /**
     * 商品上架
     * @param val
     * @param ids
     */
    @Override
    public void updateGoods(String val, Long[] ids) {
        // 遍历ids
        for (final Long spuId : ids) {
            // 修改状态  未上架的状态，已经上架的状态
            TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(spuId);
            // 设置属性
            tbGoods.setIsMarketable(val);
            // 更新
            tbGoodsMapper.updateByPrimaryKey(tbGoods);

            // 向主题中发送消息
            jmsTemplate.send(destination, new MessageCreator() {
                // 创建消息
                @Override
                public Message createMessage(Session session) throws JMSException {
                    // 创建消息
                    TextMessage textMessage = session.createTextMessage(spuId + "");
                    return textMessage;
                }
            });
        }
    }

}
