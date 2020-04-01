package cn.zcw.service.impl;

import cn.zcw.domain.TbItem;
import cn.zcw.domain.TbOrderItem;
import cn.zcw.groupentity.Cart;
import cn.zcw.mapper.TbItemMapper;
import cn.zcw.service.CatService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
public class CatServiceImpl implements CatService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbItemMapper tbItemMapper;

    /**
     * 查询购物车
     * @param key
     * @return
     */
    @Override
    public List<Cart> findCartList(String key) {
//        从redis中查询数据
        // 查询数据
        String cartListStr = (String) redisTemplate.boundValueOps(key).get();
        // 判断
        if(cartListStr == null || cartListStr.isEmpty()){
            // 给 cartListStr 赋值 给空数组值
            cartListStr = "[]";
        }
        // 解析该数据
        List<Cart> cartList = JSON.parseArray(cartListStr, Cart.class);
        return cartList;
    }

    /**
     * 数据保存到redis中
     * @param key
     * @param cartList_username
     */
    @Override
    public void saveRedis(String key, List<Cart> cartList_username) {
        // 先把cartList转换成json
        String cartListStr = JSON.toJSONString(cartList_username);
//        存入到reids
        redisTemplate.boundValueOps(key).set(cartListStr,2, TimeUnit.DAYS);


    }

    /**
     * 删除redis缓存
     * @param sessionId
     */
    @Override
    public void delete(String sessionId) {
        redisTemplate.delete(sessionId);
    }


    /**
     * 合并数据
     * @param cartList_sessionid
     * @param cartList_username
     */
    @Override
    public List<Cart> merge(List<Cart> cartList_sessionid, List<Cart> cartList_username) {
//        遍历匿名用户购物车集合
        for (Cart cart : cartList_sessionid) {
//            获取订单项集合（商品集合）
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
//                遍历商品集合
            for (TbOrderItem tbOrderItem : orderItemList) {
//                添加商品
                addCart(cartList_username,tbOrderItem.getItemId(),tbOrderItem.getNum());
            }

        }
        return cartList_username;

    }

    /**
     * 添加购物车
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */

    @Override
    public List<Cart> addCart(List<Cart> cartList, Long itemId, int num) {
//        先查询要购买的商品商品
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
//        获取商家主键
        String sellerId = tbItem.getSellerId();
//        获取商家名字
        String sellName = tbItem.getSeller();
//        先判断购物车中是否包含该商家,包含返回cart对象，不包含返回null
        Cart cart = sellerInCartList(cartList,sellerId);
//        如果包含该商家
        if (cart != null){
//            获取商品集合
            List<TbOrderItem> itemList = cart.getOrderItemList();
//            判断购物车中是否包含该商品
            TbOrderItem tbOrderItem = itemInOrderItemList(itemList,itemId);
//            如果包含该商品
            if (tbOrderItem != null){
//                数量加num
                tbOrderItem.setNum(tbOrderItem.getNum()+num);
                // 判断商品的数量等于0，说明不购买商品，清除掉
                if(tbOrderItem.getNum() <= 0){
                    // 清除订单项
                    itemList.remove(tbOrderItem);

                    // 如果orderItemList的集合的长度为0
                    if(itemList.size() <= 0){
                        // 清除购物车
                        cartList.remove(cart);
                    }

                }
//                更改
                double value = tbOrderItem.getTotalFee().doubleValue();
//                更新总价
                tbOrderItem.setTotalFee(new BigDecimal(value+ tbItem.getPrice().doubleValue()*num));
            }else {
//                如果不包含该商品
                tbOrderItem = new TbOrderItem();
//                设置属性值
                tbOrderItem.setNum(num);
//                 总金额
                tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue() * num));
//                 spu数据
                tbOrderItem.setGoodsId(tbItem.getGoodsId());
//                商品id
                tbOrderItem.setItemId(itemId);
//                商品图片路径
                tbOrderItem.setPicPath(tbItem.getImage());
//                商品价格
                tbOrderItem.setPrice(tbItem.getPrice());
//                商品标题
                tbOrderItem.setTitle(tbItem.getTitle());
//                商品所属商家id
                tbOrderItem.setSellerId(sellerId);
                // 把新的商品添加到购物车中
                itemList.add(tbOrderItem);
            }
        }else {
//            如果不包含该商家，创建新的购物车对象，
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(sellName);
//            创建新的商品集合sku
            List<TbOrderItem> list = new ArrayList<>();
//            创建新的商品
            TbOrderItem tbOrderItem = new TbOrderItem();
            //                设置属性值
            tbOrderItem.setNum(num);
//                 总金额
            tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue() * num));
//                 spu数据
            tbOrderItem.setGoodsId(tbItem.getGoodsId());
//                商品id
            tbOrderItem.setItemId(itemId);
//                商品图片路径
            tbOrderItem.setPicPath(tbItem.getImage());
//                商品价格
            tbOrderItem.setPrice(tbItem.getPrice());
//                商品标题
            tbOrderItem.setTitle(tbItem.getTitle());
//                商品所属商家id
            tbOrderItem.setSellerId(sellerId);
//            将商品存入商品集合中
            list.add(tbOrderItem);
//            将集合设置进购物车对象
            cart.setOrderItemList(list);
//            将购物车对象添加到购物车集合中
            cartList.add(cart);

        }


        return cartList;
    }

    /**
     * 判断商品集合中是否包含该商品
     * @param itemList
     * @param itemId
     * @return
     */
    private TbOrderItem itemInOrderItemList(List<TbOrderItem> itemList, Long itemId) {
//        遍历商品集合
        for (TbOrderItem tbOrderItem : itemList) {
//            如果相同
            if (tbOrderItem.getItemId().longValue() == itemId.longValue()){
//              返回商品
                return tbOrderItem;
            }
        }
        return null;
    }

    /**
     * 判断该购物车集合是否包含该商家购物车
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart sellerInCartList(List<Cart> cartList, String sellerId) {
//        遍历集合
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }



}
