package cn.zcw.groupentity;

import cn.zcw.domain.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 *
 *
 * 购物车模块
 */
public class Cart implements Serializable {

    // 商家id
    private String sellerId;
    // 商家名称（冗余设计思想）
    private String sellerName;
    // 多个订单项
    private List<TbOrderItem> orderItemList;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
