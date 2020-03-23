app.controller("registerController",function($scope,$controller,$http){

    // controller的继承，本质就是共用一个$scope
    $controller('baseController',{$scope:$scope});

    $scope.entity={phone:''};

    // 发送短信验证码
    $scope.sendCode=function(){
        // 判断手机号是否为空
        if($scope.entity.phone=='' || $scope.entity.phone==null){
            alert("手机号没填！");
            return;
        }
        // 正则表达式
        var reg =/^1[3|4|5|6|7|8|9|][0-9]{9}$/;
        if(!reg.test($scope.entity.phone)){
            alert("格式不对！");
            return;
        }
        // 发送请求
        $http.get("user/sendCode/"+$scope.entity.phone);
    };

    // 用户注册功能
    $scope.register=function(){
        if($scope.entity.password!=$scope.password2){
            alert("两次密码输入不一致！");
            return;
        }
        // 用户注册
        $http.post("user/regist/"+$scope.code,$scope.entity).success(function(resp){
            if(resp.success){
                location.href = "login.html";
            }else{
                alert(resp.message);
            }
        });
    }

});