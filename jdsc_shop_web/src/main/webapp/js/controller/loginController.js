//定义控制器
app.controller("loginController", function($scope,$http) {

    $scope.loadLoginName = function(){
        // 发送get请求
        $http.get("../index/loadLoginName").success(function(resp){
            if(resp.success){
                // 数据
                $scope.loginname = resp.data;
            }else{
                // 表示失败
                alert(resp.message);
            }
        });
    };
	
});