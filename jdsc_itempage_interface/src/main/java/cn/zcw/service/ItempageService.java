package cn.zcw.service;

import cn.zcw.groupentity.Goods;

import java.util.List;

public interface ItempageService {


    Goods findByGoodsId(Long goodsId);

    List<Goods> findByGoodsIdAll();
}
