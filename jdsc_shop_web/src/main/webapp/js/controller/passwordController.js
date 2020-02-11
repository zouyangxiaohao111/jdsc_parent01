//定义控制器
app.controller("passwordController", function($scope,$http) {


    $scope.findingLoginName = function(){
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


	//更新
	$scope.update = function(loginName){

		if ($scope.entity.password != $scope.newPassword){
			alert("两次密码不一致，重新输入!")
		}
		else {
            //调用控制层方法
            $http.post("../seller/update/" + loginName, $scope.entity).success(
                function (resp) {
                    if (resp.success) {
                        alert("密码修改成功,即将跳转到登录页面");
                        location.href = "/admin/home.html";
                    } else {
                        alert("账号或者密码错误，请重新输入")
                    }
                }
            );
        }

	};


	
});