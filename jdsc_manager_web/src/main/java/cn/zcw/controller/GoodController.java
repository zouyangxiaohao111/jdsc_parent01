//package cn.zcw.controller;
//
//import cn.zcw.domain.TbGoods;
//import cn.zcw.entity.Result;
//import cn.zcw.service.GoodService;
//import com.alibaba.dubbo.config.annotation.Reference;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
////RestController=@Controller + @ResponseBody
////接受json格式数据
//@RequestMapping("/goods")
//@RestController
//public class GoodController {
////    通过注册中心获取暴露的服务接口
//    @Reference
//    private GoodService goodService;
//    /**
//     * 查询所有
//     * @param
//     * @return
//     */
//    @RequestMapping("/findAll")
//    public Result findAll(){
//        try {
//            List<TbGoods> list = goodService.findAll();
//            return new Result(true,"查询所有成功",list);
//        }catch (Exception e){
//            e.printStackTrace();
//            return new Result(true,"查询所有失败");
//        }
//
//    }
////    /**
////     * 分页查询所有
////     * @param
////     * @return
////     */
////    @RequestMapping("/findPage1/{pageNum}/{pageSize}")
////    public Result findPage1(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize){
////        try {
////            PageInfo<TbGoods> pageInfo = goodService.findPage(pageNum,pageSize);
////            return new Result(true,"查询分页成功",pageInfo.getTotal(),pageInfo.getList());
////        }catch (Exception e){
////            e.printStackTrace();
////            return new Result(false,"查询分页失败");
////        }
////    }
//    @RequestMapping("/delete/{ids}")
//    public Result delete(@PathVariable("ids") Long [] ids){
//        try {
//            goodService.delete(ids);
//            return new Result(true,"删除成功");
//        }catch (Exception e){
//            e.printStackTrace();
//            return new Result(false,"失败");
//        }
//
//    }
//    @RequestMapping("/updateAuditStatus/{status}/{ids}")
//    public Result updateAuditStatus(@PathVariable("status") String status,@PathVariable("ids") Long [] ids){
//        try {
//            goodService.updateAuditStatus(status,ids);
//            return new Result(true,"操作成功");
//        }catch (Exception e){
//            e.printStackTrace();
//            return new Result(false,"失败");
//        }
//
//    }
//}
