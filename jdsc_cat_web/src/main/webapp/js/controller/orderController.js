app.controller("orderController", function ($scope,$http) {

    //选择收件人
    $scope.selecedAddress = function (pojo) {
        $scope.defaultAddress = pojo;
    }

    //判断当前地址是否是选择的地址
    $scope.isSelectedAddress = function (pojo) {
        return pojo == $scope.defaultAddress;
    }

    // 默认地址 从addressList查询
    $scope.defaultAddress = null;

    // 查询当前登录人的所有收货地址
    $scope.findAddressList = function () {
        $http.get("/address/findAddressList").success(function (resp) {
            // 查询结果赋值
            $scope.addressList = resp.data;

            // 判断哪个州收货地址是默认值
            for (var i = 0; i < $scope.addressList.length; i++) {
                if ($scope.addressList[i].isDefault == '1') {
                    $scope.defaultAddress = $scope.addressList[i];
                    break;
                }
            }

            // 如果没有收获地址，默认使用第一个地址
            if ($scope.defaultAddress == null) {
                $scope.defaultAddress = $scope.addressList[0];
            }
        })
    }

    // 查询
    $scope.findCartList = function(){
        // 查询
        $http.post("./cart/findCartList").success(function(resp){
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


    // 支付方式默认是在线支付
    // 订单来源是PC端
    $scope.entity = {paymentType: '1', sourceType: '2'};

    // 保存订单
    $scope.saveOrder = function () {
        // 默认地址
        $scope.entity["receiverAreaName"] = $scope.defaultAddress.address;
        // 移动电话
        $scope.entity["receiverMobile"] = $scope.defaultAddress.mobile;
        // 联系人
        $scope.entity["receiver"] = $scope.defaultAddress.contact;

        // 发送请求
        $http.post("/order/saveOrder",$scope.entity).success(function (resp) {
            if (resp.success) {
                // 跳转到支付页面
                location.href = "http://localhost:8091/pay.html";
            } else {
                // 提示信息
                alert(resp.message);
            }
        })
    }


})