app
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
    .factory('CommentEndPoint', function($resource) {
        return $resource('api/comment/:name');
    })
    .factory('CheckEndPoint', function($resource) {
        return $resource('api/home/check');
    });