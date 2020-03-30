package cn.zcw.service;

import cn.zcw.groupentity.Cart;

import java.util.List;

public interface CatService {
    List<Cart> findCartList(String sessionId);

    void saveRedis(String username, List<Cart> cartList_username);

    void delete(String sessionId);

    List<Cart> merge(List<Cart> cartList_sessionid, List<Cart> cartList_username);

    List<Cart> addCart(List<Cart> cartList, Long itemId, int num);
}
