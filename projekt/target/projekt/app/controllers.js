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
        .when('/addevent', {
            templateUrl: 'partials/event.html',
            controller: 'EventController',
            controllerAs: 'eventCtrl'
        })
        .when('/findevent', {
            templateUrl: 'partials/eventlist.html',
            controller: 'FindEventController',
            controllerAs: 'findCtrl'
        })
        .when('/eventdetails/:eventId', {
            templateUrl: 'partials/eventdetails.html',
            controller: 'EventDetailsController',
            controllerAs: 'eventdetailsCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });
})



    .controller('HomeController', function(DiscoveryEndPoint,DiscoverySortEndPoint,CheckEndPoint,$resource) {
        var vm=this;
        function readAllDiscovery() {
            vm.discoveries=DiscoveryEndPoint.query(
                function success(data, headers) {
                    console.log('Pobrano dane: ' + data);
                    console.log(headers('Content-Type'));
                },
                function error(response) {
                    console.log(response.status); //np. 404
                });
        }
        readAllDiscovery();


        vm.searchDiscovery=function(){
            vm.name=vm.textInput;

            vm.discoveries=DiscoveryEndPoint.query({id:vm.name});
        };


        vm.sortByTime=function(){

            vm.discoveries=DiscoverySortEndPoint.query({sort:'time'});
        };
        vm.sortByPopular=function(){
            vm.discoveries=DiscoverySortEndPoint.query({sort:'popular'});
        };

        function checkRole() {
            vm.display=false;
            vm.checkRole=CheckEndPoint.get(function success (data) {
                if(vm.checkRole['role']=="admin"){
                    vm.display=true;
                }}
            )
           }
        checkRole();

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
                    console.log('Pobrano dane1: ' +data['data'][0]);
                    console.log('Pobrano dane: ' + data['data']['InvalidFieldList']);
                },
                function error(response) {
                    console.log(response.status); //np. 404
                });
        }
    })

    .controller('EditDiscoveryController', function(DiscoveryEndPoint,UpdateDiscoveryEndPoint,$routeParams) {
        var vm = this;
        vm.discoveryId=$routeParams.discoveryId;
        DiscoveryEndPoint.get({id:vm.discoveryId},function success(data){
            console.log(data);
           vm.discovery=data;
        });
        vm.removeDiscovery=function(id){
            DiscoveryEndPoint.remove({id:id});
        };
        vm.editDiscovery=function(editeddiscovery){
            if(editeddiscovery.name!==null){
                vm.discovery.name=editeddiscovery.name;
            }
            if(editeddiscovery.url!==null){
                vm.discovery.url=editeddiscovery.url;
            }
            if(editeddiscovery.description!==null){
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
        function getAllUsers(){
        vm.users = UserEndPoint.query();
    }
    getAllUsers();

    })
    .controller('UserController', function(UserEndPoint,UpdateUserEndPoint,$routeParams) {
        var vm = this;
        vm.id=$routeParams.id;
        function getUserById(id){
            vm.getuser=UserEndPoint.get({parameter:id});
        }
        getUserById(vm.id);
        vm.updateUser=function(user){
            if(user.username!=null){
            vm.getuser.username=user.username;

            if(user.email!=null) {
                vm.getuser.email = user.email;
            }
                vm.getuser.active=user.active;
            console.log(user.active);
        updateUserEndPoint.update(vm.getuser);
            }
    }})


    .controller('IndexController',function(CheckEndPoint){
        var vm = this;
        function check(){
            vm.c=CheckEndPoint.get();
        }
        check();
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
    .controller("EventController", function($scope,EventEndPoint,EventPositionEndPoint) {
        var vm=this;

        vm.showMap=function(){
            vm.show=true;
        };

        vm.event=new EventEndPoint();
        vm.addEvent = function(event) {
            vm.event.timestamp=new Date(vm.date+" "+vm.time);
            vm.event.eventPosition=vm.listOfEventPosition;
            vm.event.$save(function (data) {
                vm.event = new EventEndPoint();
                console.log(vm.date+""+vm.time);
                vm.date='';
                vm.time='';
            })
        };

        vm.markers=new Array();
        vm.listOfEventPosition=new Array();
        $scope.$on('leafletDirectiveMap.click', function(event, args) {
            vm.eventPosition=new EventPositionEndPoint();
            vm.eventPosition.xCoordinate=args.leafletEvent.latlng.lat;
            vm.eventPosition.yCoordinate=args.leafletEvent.latlng.lng;
            vm.listOfEventPosition.push(vm.eventPosition);
            vm.markers.push({
                lat: args.leafletEvent.latlng.lat,
                lng: args.leafletEvent.latlng.lng,
                message: "My Added Marker",
                number:args.leafletEvent.latlng.lat,
                draggable: true
            });
        });

        $scope.$on('leafletDirectiveMarker.contextmenu', function(event, args) {
            vm.lat=args.leafletEvent.latlng.lat;
            console.log(args.leafletEvent.latlng.lat);
            for(i=0;i<vm.markers.length;i++){
                if(vm.lat===vm.markers[i].lat){
                    vm.markers.splice(i,1);
                }
            }
            });

        $scope.$on('leafletDirectiveMarker.dragend', function(event, args){
            console.log(args.model.lat+" "+args.model.lng);
        });

        angular.extend(this, {
            center: {
                lat: 50,
                lng: 20,
                zoom: 6
            },
            defaults: {
                scrollWheelZoom: false
            },
            position: {
                lat: 51,
                lng: 0
            },
                events: {
                    map: {
                        enable: ['click'],
                        logic: 'emit'
                    }
                }
        })
    })
        .controller('FindEventController',function(EventEndPoint,EventFindByPositionEndPoint,$scope){
            var vm = this;

            function getListOfEvents(){
                vm.eventList=EventEndPoint.query();
            }
            getListOfEvents();

            vm.getListOfEventsByCity=function(city) {
                vm.eventList = EventEndPoint.query({city: city});
            };
                vm.markers=new Array();
            $scope.$on('leafletDirectiveMap.click', function(event, args) {
                vm.lat=args.leafletEvent.latlng.lat;
                vm.long=args.leafletEvent.latlng.lng;
                    vm.markers[0]=({
                        lat: args.leafletEvent.latlng.lat,
                        lng: args.leafletEvent.latlng.lng,
                        message: "My Added Marker",
                        draggable: true
                    });
                });
            vm.getListOfEventsByPosition=function(distance) {
                vm.eventList = EventFindByPositionEndPoint.query({x: vm.lat,y: vm.long, distance: distance});
            };
            angular.extend(this, {
                center: {
                    lat: 50,
                    lng: 20,
                    zoom: 6
                },
                defaults: {
                    scrollWheelZoom: false
                },
                position: {
                    lat: 51,
                    lng: 0
                },
                events: {
                    map: {
                        enable: ['click'],
                        logic: 'emit'
                    }
                }
            })
        })
        .controller('EventDetailsController',function($routeParams,EventEndPoint){
            var vm = this;
            vm.id=$routeParams.eventId;
            function getEventById(id) {
                vm.event=EventEndPoint.get({id:id},function success(data) {
                    vm.positionList = data.eventPosition;
                    vm.markers=new Array();
                    for(i=0;i<vm.positionList.length;i++) {
                        vm.markers.push({
                            lat: vm.positionList[i].xCoordinate,
                            lng: vm.positionList[i].yCoordinate,
                            draggable: false
                        });
                    }
                });

            }
            getEventById(vm.id);


        });

