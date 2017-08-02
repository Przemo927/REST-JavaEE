app
    .factory('DiscoveryEndPoint', function($resource) {
        return $resource('api/discovery/:namedisc');
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