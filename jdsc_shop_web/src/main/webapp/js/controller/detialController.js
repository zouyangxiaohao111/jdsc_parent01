//定义控制器
app.controller("detialController", function($scope,$http) {


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
    // $scope.findOne = function(loginname){
    //
    //     $http.get("../seller/findOne/"+loginname).success(function(resp){
    //         if(resp.success){
    //             // 数据
    //             $scope.entity = resp.data;
    //         }else{
    //             // 表示失败
    //             alert(resp.message);
    //         }
    //     });
    //
    // }


	//更新
	$scope.save = function(){



            //调用控制层方法
            $http.post("../seller/save",$scope.entity).success(
                function (resp) {
                    if (resp.success) {
                        alert("资料修改成功,即将跳转到登录页面");
                        location.href = "/admin/home.html";
                    } else {
                        alert("修改失败");
                    }
                }
            );


	};


	
});