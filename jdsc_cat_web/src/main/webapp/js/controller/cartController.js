app.controller("cartController",function($scope,$http){

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