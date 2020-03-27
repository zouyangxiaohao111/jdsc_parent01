app.controller("formController",function($scope,$controller,$http){

    // controller的继承，本质就是共用一个$scope
    $controller('baseController',{$scope:$scope});




    // 用户注册功能
    $scope.initLogine=function(){
        // 发送get请求
        $http.get("../index/findLoginname").success(function(resp){

            if(resp.success){
                // 数据
                $scope.loginname = resp.data;

                location.href="http://localhost:8083/index.html#?loginname="+$scope.loginname;
            }else{
                // 表示失败
                alert(resp.message);
            }
        });


    }

});