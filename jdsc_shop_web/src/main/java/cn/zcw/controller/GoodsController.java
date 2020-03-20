package cn.zcw.controller;

import cn.zcw.domain.TbGoods;
import cn.zcw.entity.Result;
import cn.zcw.groupentity.Goods;
import cn.zcw.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {
//    获取注册中心的接口
    @Reference
    private GoodsService goodService;
    @RequestMapping("/save")
    public Result add(@RequestBody Goods goods){
        try {

            goodService.add(goods);
            return new Result(true,"保存成功,正在跳转页面");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageCode
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageCode}")
    public Result findPage(@PathVariable("pageNum")int pageNum,@PathVariable("pageCode")int pageCode){
        try {

            PageInfo<TbGoods> pageInfo = goodService.findPage(pageNum, pageCode);
            return new Result(true,"操作成功",pageInfo.getTotal(),pageInfo.getList());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

    @RequestMapping("/updateGoodsMark/{val}/{ids}")
    public Result updateGoodsMark(@PathVariable("val")String val,@PathVariable("ids")Long[] ids){
        try {
            // 商品上架
            goodService.updateGoods(val,ids);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }



}
