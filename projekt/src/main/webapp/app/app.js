angular.module('app', ['ngRoute','ngResource'])
.config(function ($routeProvider,$locationProvider,$locationProvider) {
	$routeProvider.caseInsensitiveMatch=true;
	$locationProvider.hashPrefix('');
    $routeProvider
    .when('/', {
        templateUrl: 'partials/home.html',
        controller: 'HomeController',
        controllerAs: 'homCtrl'
    })
    .when('/add', {
        templateUrl: 'partials/new.html',
        controller: 'AddController',
        controllerAs: 'addCtrl'
    })
    .when('/login', {
        templateUrl: 'partials/login.html',
        controller: 'LoginController',
        controllerAs: 'logCtrl'
    })
   
    .otherwise({
        redirectTo: '/'
    });
})

.service('DiscoveryEndPoint', function($resource) {
        var vm=this;
        vm.getEndPoint=function($resource){
            return Discovery=$resource('api/discovery/:namedisc');
        }
    })
.controller('HomeController', function($resource,DiscoveryEndPoint) {
	var vm=this;
	var Discoveries=DiscoveryEndPoint.getEndPoint();
	
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
	ReadAllDiscovery();
	
	
	vm.SearchDiscovery=function(){
		vm.name=vm.textInput;
		
	vm.discoveries=Discoveries.query({namedisc:vm.name});
	}
	
	vm.SortByTime=function(){
		var Sort=$resource('api/discovery?orderBy=:sort');
	vm.discoveries=Sort.query({sort:'time'});
	}
	vm.SortByPopular=function(){
		var Sort=$resource('api/discovery?orderBy=:sort');
	vm.discoveries=Sort.query({sort:'popular'});
	}
})


.controller('AddController', function($resource,DiscoveryEndPoint) {
	var vm = this;
	   var Discovery = DiscoveryEndPoint.getEndPoint();
	    vm.discovery = new Discovery();
	    
	    vm.addDiscovery = function(discovery) {
	    	vm.discovery.$save(function(data) {
		           vm.discovery = new Discovery();
	        })
	        };
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
        }
        );
    }
})


.controller('IndexController',function($resource,$location,$anchorScroll){
var vm = this;
var check=$resource('api/home/check');
function Check(){
vm.c=check.get();
}
Check();
});
