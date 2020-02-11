package cn.zcw.controller;

import cn.zcw.domain.TbItemCat;
import cn.zcw.entity.Result;
import cn.zcw.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//RestController=@Controller + @ResponseBody
//接受json格式数据
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {
    @Reference
    private ItemCatService itemCatService;


    /**
     * 查询父id为pid的数据
     * @param pid
     * @return
     */
    @RequestMapping("/findByParentId/{pid}")
    public Result findByParentId(@PathVariable("pid") Long pid){

        try {
//            查询所有父id=pid的所有分类数据
            List<TbItemCat> list = itemCatService.findByParentId(pid);
//            返回前端
            return new Result(true,"查询成功1",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }


    /**
     * 分页查询
     * @param
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize){

        try {
//            查询所有父id=pid的所有分类数据
            PageInfo<TbItemCat> pageInfo = itemCatService.findPage(pageNum,pageSize);
//            返回前端
            return new Result(true,"分页查询成功1",pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"分页查询失败");
        }
    }

    /**
     * 数据回显查询
     * @param
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public Result findOne(@PathVariable("id") Long id){

        try {
//            通过主键id查询
            TbItemCat tbItemCat = itemCatService.findOne(id);
//            返回前端
            return new Result(true,"查询成功1",tbItemCat);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }

    /**
     * 保存
     * @param
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbItemCat tbItemCat){

        try {
//            添加
              itemCatService.add(tbItemCat);
//            返回前端
            return new Result(true,"添加成功1");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    /**
     * 更新
     * @param
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbItemCat tbItemCat){

        try {
//            添加
            itemCatService.update(tbItemCat);
//            返回前端
            return new Result(true,"更新成功1");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    /**
     * 查询所有
     * @param
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){

        try {
//            查询所有
            List<TbItemCat> list = itemCatService.findAll();
//            返回前端
            return new Result(true,"查询成功1",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }
    /**
     * 批量删除
     * @param
     * @return
     */
    @RequestMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") Long [] ids){

        try {
//            批量删除
              itemCatService.delete(ids);
//            返回前端
            return new Result(true,"批量删除成功1");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"批量删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param
     * @param
     * @param
     * @return
     */

    @RequestMapping("/search/{pageNum}/{pageSize}")
    public Result search(@RequestBody TbItemCat itemCat, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {
        try {
            // 调用业务层，查询
            PageInfo<TbItemCat> page = itemCatService.findPage(itemCat, pageNum, pageSize);
            return new Result(true, "查询成功1", page.getTotal(), page.getList());
        } catch (Exception e) {
            // 打印异常
            e.printStackTrace();
            // 返回错误提示信息
            return new Result(false, "查询失败");
        }
    }
}
