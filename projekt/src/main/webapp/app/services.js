app
    .service('DiscoveryNameService',function(){
        var vm=this;
        vm.setName = function(name) {
            vm.name=name;
        };
        vm.getName=function() {
            return vm.name;
        }
    })
    .service('IdUser', function() {
        var vm = this;
        vm.setId = function (id) {
            vm.id = id;
        };
        vm.getId = function () {
            return vm.id;
        }
    })
    .factory('DiscoveryEndPoint', function($resource) {
        return $resource('api/discovery/:namedisc');
    })
    .factory('UserEndPoint', function($resource) {
        return $resource('api/user/:parameter');
    })
    .factory('UpdateUserEndPoint', function($resource) {
        return $resource('api/user/:parameter',null,{
            'update': { method:'PUT' }
        });
    })
    .factory('DiscoverySortEndPoint', function($resource) {
        return $resource('api/discovery?orderBy=:sort');
    })
    .factory('UpdateDiscoveryEndPoint', function($resource) {
        return $resource('api/discovery',null,{
            'update': { method:'PUT' }
        });
    })
    .factory('RegisterEndPoint', function($resource) {
        return $resource('api/register');
    })
    .factory('CheckEndPoint', function($resource) {
        return $resource('api/home/check');
    });