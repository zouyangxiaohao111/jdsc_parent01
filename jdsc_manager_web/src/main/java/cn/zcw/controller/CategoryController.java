package cn.zcw.controller;

import cn.zcw.domain.TbContentCategory;
import cn.zcw.entity.Result;
import cn.zcw.service.CategoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/category")
@RestController
public class CategoryController {
    @Reference
    private CategoryService categoryService;

    /**
     * 更新
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize){

        try {
            PageInfo<TbContentCategory> pageInfo = categoryService.findPage(pageNum,pageSize);
            return new Result(true,"分页成功",pageInfo.getTotal(),pageInfo.getList());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"分页失败");
        }

    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbContentCategory tbContentCategory){

        try {
            categoryService.update(tbContentCategory);
            return  new Result(true,"成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"失败的更新");
        }

    }
    @RequestMapping("/save")
    public Result save(@RequestBody TbContentCategory tbContentCategory){

        try {
            categoryService.save(tbContentCategory);
            return  new Result(true,"成d功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"失败d的更新");
        }

    }
    @RequestMapping("/findOne/{id}")
    public Result findOne(@PathVariable("id") Long id){

        try {
            TbContentCategory tbContentCategory = categoryService.findOne(id);
            return  new Result(true,"成功",tbContentCategory);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"失败的查询");
        }

    }

    @RequestMapping("/delete/{ids}")
    public Result findOne(@PathVariable("ids") Long [] ids){

        try {
             categoryService.delete(ids);
            return  new Result(true,"成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"失败");
        }

    }


}
