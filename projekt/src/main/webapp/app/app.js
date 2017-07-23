angular.module('app', ['ngRoute','ngResource'])
.config(function ($routeProvider,$locationProvider) {
	$routeProvider.caseInsensitiveMatch=true;
	$locationProvider.hashPrefix('');
    $routeProvider
    .when('/', {
        templateUrl: 'home.html',
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
        vm.getEndPoint=function($resource) {
            return Discovery=$resource('api/discovery/:namedisc');
        }
    })
.controller('HomeController', function(DiscoveryEndPoint,$resource) {
	var vm=this;
	var Discoveries=DiscoveryEndPoint.getEndPoint($resource);
    var Sort=$resource('api/discovery?orderBy=:sort');

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
	};


	vm.SortByTime=function(){

	vm.discoveries=Sort.query({sort:'time'});
	};
	vm.SortByPopular=function(){
	vm.discoveries=Sort.query({sort:'popular'});
	}
})


.controller('AddController', function(DiscoveryEndPoint,$resource) {
	var vm = this;
	   var Discovery = DiscoveryEndPoint.getEndPoint($resource);
	    vm.discovery = new Discovery($resource);
	    
	    vm.addDiscovery = function(discovery) {
	    	vm.discovery.$save(function(data) {
		           vm.discovery = new Discovery($resource);
	        })
	        };
})
	
.controller('RegisterController', function($resource) {
	var vm = this;
	var username='username';
   var User = $resource('api/register');
    vm.user = new User();
    vm.addUser = function(user) {
    	 vm.user.$save(function(data) {
             vm.user = new User();
         },
             function success(data, headers) {
                 var InvalidFieldList=data['data']['InvalidFieldList'];
                 console.log('Pobrano dane1: ' +data['data']['InvalidFieldList'][vm.s]);
                 if(InvalidFieldList['username']){
                        vm.MessageUser=InvalidFieldList['username'];
                    }
                 if(InvalidFieldList['email']){
                     vm.MessageEmail=InvalidFieldList['username'];
                 }
                 console.log('Pobrano dane: ' + data['data']['InvalidFieldList']['username']);
                 console.log(headers('Content-Type'));

             },
             function error(response) {
                 console.log(response.status); //np. 404
             });
         }
  })


.controller('IndexController',function($resource){
var vm = this;
var check=$resource('api/home/check');
function Check(){
vm.c=check.get();
}
Check();
});
