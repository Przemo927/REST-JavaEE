angular.module('app', ['ngRoute','ngResource'])
.config(function ($routeProvider) {
    $routeProvider
    .when('/home', {
        templateUrl: 'home.html',
        controller: 'HomeController',
        controllerAs: 'homCtrl'
    })
    .when('/add', {
        templateUrl: 'new.html',
        controller: 'AddController',
        controllerAs: 'addCtrl'
    })
    .when('/login', {
        templateUrl: 'login.html',
        controller: 'LoginController',
        controllerAs: 'logCtrl'
    })
    .when('/register', {
        templateUrl: 'register.html',
        controller: 'RegisterController',
        controllerAs: 'regCtrl'
    })
    //.when('/*', {
       // controller: 'IndexController',
       // controllerAs: 'IndCtrl'
    //})
    .otherwise({
        redirectTo: '/home'
    });
})
.controller('HomeController', function($resource) {
	var vm=this;

	var Discoveries=$resource('api/home');
	function ReadAllDiscovery() {
	vm.discoveries=Discoveries.query(
	function success(data, headers) {
        console.log('Pobrano dane: ' + data);
        console.log(headers('Content-Type'));
       
    },
    function error(response) {
        console.log(response.status); //np. 404
    });
	}
	//var check=$resource('api/home/check');
	//function Check(){
		//vm.c=check.get();
		
//	}
	ReadAllDiscovery();
	//Check();
})
.controller('AddController', function($resource) {
	var vm = this;
	   var Discovery = $resource('api/add');
	    vm.discovery = new Discovery();
	    vm.addDiscovery = function(discovery) {
	        vm.discovery.$save(function(data) {
	           vm.discovery = new Discovery();
	        });
	    }
	    var Discoveries=$resource('api/home');
		function ReadAllDiscovery() {
		vm.discoveries=Discoveries.query();
		}
		ReadAllDiscovery();
	})
.controller('RegisterController', function($resource) {
	var vm = this;
   var User = $resource('api/register');
    vm.user = new User();
    vm.addUser = function(user) {
        vm.user.$save(function(data) {
           vm.user = new User();
        },
        function success(data, headers) {
            console.log('Pobrano dane: ' + data);
            console.log(headers('Content-Type'));
           
        },
        function error(response) {
            console.log(response.status); //np. 404
            alert("Lipa");
        }
        );
    }
});


//.controller('IndexController',function($http){
	//var vm = this;
	//var check = $resource('api/home/check');
	//vm.check1= new check();
	 //function Check(check1){
	//check1.get(function(data) {
        //vm.c=data;
    //});
//	}
//Check();
	 //function refreshData() {
	        //$http({
	          //  method : 'GET',
	         //   url : 'api/home/check'
	        //}).then(function success(response) {
	          //  vm.c = response.data;
	       // }, function error(response) {
	        	//console.log('API error ' + response.status);
	        //});
	    //}
	// refreshData();
//});