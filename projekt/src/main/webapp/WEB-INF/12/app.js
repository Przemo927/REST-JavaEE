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
		//return c;	
	//}
    //vm.c=Check();
	ReadAllDiscovery();
	
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
	    ReadAllDiscovery();  
	})
.controller('RegisterController', function($resource) {
	var vm = this;
   var User = $resource('api/register');
    vm.user = new User();
    vm.addUser = function(user) {
        vm.user.$save(function(data) {
           vm.user = new User();
        });
    }
})


.controller('LoginController',function(){
	
});