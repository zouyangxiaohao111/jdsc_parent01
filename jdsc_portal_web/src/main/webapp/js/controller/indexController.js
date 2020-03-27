app.controller("indexController",function($scope,$location,$http){

    $scope.paramMap = {"loginname":'',"brand":''};
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


    // 页面加载，初始化的方法
    $scope.initLogine2=function(){
        // 获取url上的keyword
        $scope.paramMap.loginname =$location.search()["loginname"];


    }
	
})