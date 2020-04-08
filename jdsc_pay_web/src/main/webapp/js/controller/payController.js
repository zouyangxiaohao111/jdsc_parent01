app.controller("payController", function ($scope, $location, $http) {

    $scope.payOutInfo = "打开微信，扫码支付！";

    // 调用统一下单接口
    $scope.createNative = function () {
        // 调用后台统一下单接口，在微信端产生预支付订单，通过返回值生成二维码
        $http.get("./pay/createNative").success(function(resp){
            // 返回的结果
            $scope.resultMap = resp.data;

            // 生成二维码
            new QRious({
                element: document.getElementById('qrious'),
                size: 250,
                value: resp.data.code_url
            });

            // 马上查询是否支付
            $scope.queryPayStatus(resp.data.out_trade_no);
        });
    }

    // 查询是否支付
    $scope.queryPayStatus = function (out_trade_no) {

        // 查询是否支付
        $http.get("./pay/queryPayStatus/"+out_trade_no).success(function(resp){
            if(resp.success){
                location.href = "paysuccess.html";
            }else{
                // 返回的结果是支付超时
                if(resp.message=="支付超时"){
                    // 美工应该提供一个 刷新二维码的功能
                    $scope.payOutInfo="二维码已超时，请刷新页面";
                    new QRious({
                        element: document.getElementById('qrious'),
                        size: 250,
                        value: ""
                    });
                }else{
                    location.href="payfail.html";
                }
            }
        });
    }
    $scope.findIndex = function () {
        $http.get("../payIndex/findLogin").success(function (resp) {
            $scope.loginN = resp.data;
        })

    }

    $scope.showMoney = function () {
        return $location.search()['totalFee'];
    }

})