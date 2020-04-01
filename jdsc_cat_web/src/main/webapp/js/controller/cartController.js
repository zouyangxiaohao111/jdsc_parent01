app.controller("cartController",function($scope,$http){

    $scope.tempt=true;
    // 点击搜索按钮
    $scope.search=function(){
        // 如果用户没有输入内容，自己指定搜索的关键字
        if($scope.keyword==null || $scope.keyword==""){
            // 设置默认值
            $scope.keyword="小米";
        }
        // 跳转页面，发送请求
        location.href="http://localhost:8084/search.html#?keyword="+$scope.keyword;
    }

    // 查询
    $scope.findCartList = function(){

        // 查询
        $http.post("../cart/findCartList").success(function(resp){

            // 返回结果
            $scope.cartList = resp.data;

            // 总价
            $scope.totalMomey=0.00;
            // 总数量
            $scope.totalNum=0;

            // 获取到集合
            var obj = resp.data;
            // 遍历购物车集合
            for (var i = 0; i < obj.length; i++) {
                // 获取到订单详情
                var orderItemList = obj[i].orderItemList;
                // 遍历订单详情
                for (var j = 0; j < orderItemList.length; j++) {
                    // 累加总价格
                    $scope.totalMomey+=orderItemList[j].totalFee;
                    // 累加总数量
                    $scope.totalNum+=orderItemList[j].num;
                }
            }
        })

    }

    // 点击 + - 号处理
    // itemId sku的id  num=购买的数量
    $scope.addCart = function(itemId,num){
        // 查询
        $http.post("../cart/addCart/"+itemId+"/"+num).success(function(resp){
            // 判断
            if(resp.success){
                // 刷新页面
                $scope.findCartList();
            }else{
                alert(resp.message);
            }
        })
    }

})