package cn.zcw.controller;

import cn.zcw.domain.TbItem;
import cn.zcw.entity.Result;
import cn.zcw.groupentity.Goods;
import cn.zcw.service.ItempageService;
import com.alibaba.dubbo.config.annotation.Reference;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/itempage")

public class ItemPageController {
    @Reference
    private ItempageService itempageService;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;
    @Autowired
    private HttpServletResponse Response;

    @RequestMapping("/getHtmlByGoodsId/{tbgoodsId}")
    public Result getHtmlByGoodsId (@PathVariable("tbgoodsId") Long tbgoodsId){

        try {
            //调用业务层
            Goods goods = itempageService.findByGoodsId(tbgoodsId);
            // 生成详情页
            // 获取配置对象
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            List<TbItem> itemList = goods.getItemList();
            for (TbItem tbItem : itemList) {
                Map map = new HashMap();
                map.put("goods",goods);
                map.put("tbItem",tbItem);
                FileWriter fileWriter = new FileWriter(new File("D:\\pages\\"+tbItem.getId()+".html" ) );
                template.process(map,fileWriter);
                fileWriter.close();
            }
            return new Result(true,"生成成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"生成失败");
        }
    }


    @RequestMapping("/getHtmls")
    public Result getHtmls (){

        try {
            //调用业务层
            List<Goods>  goodsList = itempageService.findByGoodsIdAll();
            // 生成详情页
            // 获取配置对象
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            for (Goods goods : goodsList) {
                List<TbItem> itemList = goods.getItemList();
                for (TbItem tbItem : itemList) {
                    Map map = new HashMap();
                    map.put("goods",goods);
                    map.put("tbItem",tbItem);
                    FileWriter fileWriter = new FileWriter(new File("D:\\pages\\"+tbItem.getId()+".html"));
                    template.process(map,fileWriter);
                    fileWriter.close();
                }
            }

            return new Result(true,"生成成功1");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"生成失败2");
        }
    }



}
