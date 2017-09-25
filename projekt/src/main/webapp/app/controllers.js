app
.config(function ($routeProvider,$locationProvider) {
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
            controller: 'AddDiscoveryController',
            controllerAs: 'addCtrl'
        })
        .when('/login', {
            templateUrl: 'partials/login.html',
            controller: 'LoginController',
            controllerAs: 'logCtrl'
        })
        .when('/editdiscovery/:discoveryId', {
            templateUrl: 'partials/editdiscovery.html',
            controller: 'EditDiscoveryController',
            controllerAs: 'editdiscCtrl'
        })
        .when('/discovery/:discoveryId', {
            templateUrl: 'partials/discovery.html',
            controller: 'DiscoveryController',
            controllerAs: 'discCtrl'
        })
        .when('/users', {
            templateUrl: 'partials/userslist.html',
            controller: 'UsersController',
            controllerAs: 'usersCtrl'
        })
        .when('/edituser/:id', {
            templateUrl: 'partials/edituser.html',
            controller: 'UserController',
            controllerAs: 'userCtrl'
        })

        .otherwise({
            redirectTo: '/'
        });
})



    .controller('HomeController', function(DiscoveryEndPoint,DiscoverySortEndPoint,CheckEndPoint,$resource) {
        var vm=this;
        function ReadAllDiscovery() {
            vm.discoveries=DiscoveryEndPoint.query(
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

            vm.discoveries=DiscoveryEndPoint.query({namedisc:vm.name});
        };


        vm.SortByTime=function(){

            vm.discoveries=DiscoverySortEndPoint.query({sort:'time'});
        };
        vm.SortByPopular=function(){
            vm.discoveries=DiscoverySortEndPoint.query({sort:'popular'});
        };

        function CheckRole() {
            vm.display=false;
            vm.checkRole=CheckEndPoint.get(function success (data) {
                if(vm.checkRole['role']=="admin"){
                    vm.display=true;
                }}
            )
           }
        CheckRole();

        })

    .controller('RegisterController', function(RegisterEndPoint) {
        var vm = this;
        vm.user = new RegisterEndPoint();
        vm.addUser = function(user) {
            vm.user.$save(function(data) {
                    vm.user = new RegisterEndPoint();
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

    .controller('AddDiscoveryController', function(DiscoveryEndPoint,$resource) {
        var vm = this;
        vm.discovery = new DiscoveryEndPoint();
        vm.addDiscovery = function(discovery) {
            vm.discovery.$save(function(data) {
                vm.discovery = new DiscoveryEndPoint();
            })
        };
    })

    .controller('EditDiscoveryController', function(DiscoveryEndPoint,UpdateDiscoveryEndPoint,$routeParams) {
        var vm = this;
        vm.discoveryId=$routeParams.discoveryId;
        DiscoveryEndPoint.get({id:vm.discoveryId},function success(data){
            console.log(data);
           vm.discovery=data;
        });
        vm.RemoveDiscovery=function(id){
            DiscoveryEndPoint.remove({id:id});
        };
        vm.EditDiscovery=function(editeddiscovery){
            if(editeddiscovery.name!=null){
                vm.discovery.name=editeddiscovery.name;
            }
            if(editeddiscovery.url!=null){
                vm.discovery.url=editeddiscovery.url;
            }
            if(editeddiscovery.description!=null){
                vm.discovery.description=editeddiscovery.description;
            }
            UpdateDiscoveryEndPoint.update(vm.discovery);
        }
    })

    .controller('DiscoveryController', function(DiscoveryEndPoint,$routeParams,CommentEndPoint) {
        var vm = this;
        vm.discoveryId=$routeParams.discoveryId;
        DiscoveryEndPoint.get({id:vm.discoveryId},function success(data){
            console.log(data);
            vm.discovery=data;
        });
        vm.getComment=CommentEndPoint.query({name:vm.discoveryId});
        vm.comment=new CommentEndPoint();
        vm.addComment=function(comment){

            vm.comment.$save({name:vm.discoveryId},function(data) {
            });
        }
    })

    .controller('UsersController', function(UserEndPoint) {
        var vm = this;
        function GetAllUsers(){
        vm.users = UserEndPoint.query();
    }
    GetAllUsers();

    })
    .controller('UserController', function(UserEndPoint,UpdateUserEndPoint,$routeParams) {
        var vm = this;
        vm.id=$routeParams.id;
        function GetUserById(id){
            vm.getuser=UserEndPoint.get({parameter:id});
        }
        GetUserById(vm.id);
        vm.UpdateUser=function(user){
            if(user.username!=null){
            vm.getuser.username=user.username;
            }
            if(user.email!=null) {
                vm.getuser.email = user.email;
            }
                vm.getuser.active=user.active;
            console.log(user.active);
        UpdateUserEndPoint.update(vm.getuser);
            }
    })


    .controller('IndexController',function(CheckEndPoint){
        var vm = this;
        function Check(){
            vm.c=CheckEndPoint.get();
        }
        Check();
    });