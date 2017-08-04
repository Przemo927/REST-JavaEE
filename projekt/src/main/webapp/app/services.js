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
    .factory('DiscoveryEndPoint', function($resource) {
        return $resource('api/discovery/:namedisc');
    })
    .factory('UserEndPoint', function($resource) {
        return $resource('api/user/:username');
    })
    .factory('DiscoverySortEndPoint', function($resource) {
        return $resource('api/discovery?orderBy=:sort');
    })
    .factory('RegisterEndPoint', function($resource) {
        return $resource('api/register');
    })
    .factory('CheckEndPoint', function($resource) {
        return $resource('api/home/check');
    });