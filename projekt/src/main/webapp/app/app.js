angular.module('app', ['ngRoute','ngResource'])
.config(function ($routeProvider,$locationProvider) {
	$routeProvider.caseInsensitiveMatch=true;
    $routeProvider
    .when('/home', {
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
        templateUrl: 'login.html',
        controller: 'LoginController',
        controllerAs: 'logCtrl'
    })
   
    //.when('/*', {
     //  controller: 'IndexController',
      //  controllerAs: 'indCtrl'
    //})
    .otherwise({
        redirectTo: '/home'
    });
})
.controller('HomeController', function($resource) {
	var vm=this;

	var Discoveries=$resource('api/home/:namedisc');
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
		var Sort=$resource('api/home?orderBy=:sort');
	vm.discoveries=Sort.query({sort:'time'});
	}
	vm.SortByPopular=function(){
		var Sort=$resource('api/home?orderBy=:sort');
	vm.discoveries=Sort.query({sort:'popular'});
	}
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
})
.controller('IndexController',function($resource,$location,$anchorScroll){
var vm = this;
var check=$resource('api/home/check');
function Check(){
vm.c=check.get();
}
Check();
//vm.scrollTo=function(location){
	//$location.hash(location);
	//var newhash=location;
	//if($location.hash()==location){
	
	//}
	
	//$anchorScroll();
	//$location.hash()=='';
//s}
});