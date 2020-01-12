app.controller("itemCatController",function($scope,$controller,$http){

    // controller的继承，本质就是共用一个$scope
    $controller('baseController',{$scope:$scope});
    // 定义reloadList方法
    $scope.reloadList = function(){
        // 发送请求，分页查询数据
        // 请求参数传当前页和每页显示的条数
        // 返回值返回json，格式如下：data = {total:xx,rows:[{},{}]}
        $http.get("../itemCat/findPage/"+$scope.paginationConf.currentPage+"/"+$scope.paginationConf.itemsPerPage).success(function(resp){
            if(resp.success){
                // 分页数据
                $scope.list = resp.data;
                // 总记录数
                $scope.paginationConf.totalItems = resp.total;
            }else{
                // 表示失败
                alert(resp.message);
            }
        });
    };

    // 定义entity空对象，用来显示二级和三级分类
    $scope.entity1 = null;
    $scope.entity12 = null;

    // 定义级别属性
    $scope.grade = 1;

    // 父id
    $scope.parentId=0;

    // 提供一个设置级别的方法
    $scope.setGrade = function(val,en){
        $scope.grade = val;
        $scope.parentId=en.id;
        //列表显示的是一级分类
        if($scope.grade==1){
            $scope.entity1=null; //面包屑上显示的一级分类
            $scope.entity2=null; //面包屑上显示的二级分类
        }
        //列表显示的是二级分类    entity2 置成null
        if($scope.grade==2){
            $scope.entity1=en;
            $scope.entity2=null;
            $scope.parentId=en.id;
        }
        //列表显示的是三级分类
        if($scope.grade==3){
            $scope.entity2=en;
            $scope.parentId=en.id;
        }
    };

    // 查询分类数据
    $scope.findByParentId = function(parentId){
        // 清空二级和三级分类的名称
        if(parentId == 0){
            $scope.entity1=null;
            $scope.entity2=null;
            $scope.grade = 1;
            $scope.parentId=0;
        }

        $http.get("../itemCat/findByParentId/"+parentId).success(function(resp){
            if(resp.success){
                // 数据
                $scope.list = resp.data;
            }else{
                // 表示失败
                alert(resp.message);
            }
        });
    };

    // 通过id查询一个对象方法
    $scope.findOne = function(id){
        // 通过主键查询
        $http.get("../itemCat/findOne/"+id).success(function(resp){
            if(resp.success){
                // 赋值给entity变量
                $scope.entity = resp.data;
            }else{
                alert(resp.message);
            }
        });
    };

    // 查询所有的模板
    $scope.findTemplateAll = function(){
        $http.get("../typeTemplate/findAll").success(function(resp){
            if(resp.success){
                // 数据
                $scope.typeTemplateList = resp.data;
            }else{
                // 表示失败
                alert(resp.message);
            }
        });
    };

    // 保存和修改的方法
    $scope.save = function(){

        $scope.entity.parentId=$scope.parentId;

        // 定义变量名称
        var method = "add";
        // 判断当前是否再进行修改功能
        if($scope.entity.id != null){
            // 说明id有值，进行的是修改功能
            method = "update";
        }
        // 发送post请求，保存数据
        $http.post("../itemCat/"+method,$scope.entity).success(function(resp){
            if(resp.success){
                // 重新刷新数据
                $scope.findByParentId($scope.parentId);
            }else{
                alert(resp.message);
            }
        });
    };

    // 删除
    $scope.dele = function(){
        // 判断是否选择了元素
        if($scope.selectIds.length == 0){
            alert("至少选择一项!");
            return;
        }
        // 确认框
        if(window.confirm("确定删除吗")){
            // 发送请求，删除数据
            $http.get("../itemCat/delete/"+$scope.selectIds).success(function(resp){
                if(resp.success){
                    // 重新刷新数据
                    $scope.findByParentId($scope.parentId);
                    // 清空数据
                    $scope.selectIds = [];
                }else{
                    alert(resp.message);
                }
            });
        }
    };




});