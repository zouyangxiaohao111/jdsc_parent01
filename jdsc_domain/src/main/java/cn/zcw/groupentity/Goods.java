package cn.zcw.groupentity;

import cn.zcw.domain.TbGoods;
import cn.zcw.domain.TbGoodsDesc;
import cn.zcw.domain.TbItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Goods implements Serializable {
//    sku
    private List<TbItem>  itemList;
    //    spu
    private TbGoods tbGoods;
    //    spu详细信息
    private TbGoodsDesc tbGoodsDesc;

    // 存储的是分类
    private Map<String,String> categoryMap;

    public Map<String, String> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map<String, String> categoryMap) {
        this.categoryMap = categoryMap;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }

    public TbGoods getTbGoods() {
        return tbGoods;
    }

    public void setTbGoods(TbGoods tbGoods) {
        this.tbGoods = tbGoods;
    }

    public TbGoodsDesc getTbGoodsDesc() {
        return tbGoodsDesc;
    }

    public void setTbGoodsDesc(TbGoodsDesc tbGoodsDesc) {
        this.tbGoodsDesc = tbGoodsDesc;
    }







}
