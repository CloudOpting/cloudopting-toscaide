angular
		.module('hello', [ 'ngRoute', 'schemaForm' ])
		.config(
				function($routeProvider, $httpProvider) {

					$routeProvider.when('/', {
						templateUrl : 'home.html',
						controller : 'home'
					}).when('/login', {
						templateUrl : 'login.html',
						controller : 'navigation'
					}).otherwise('/');

					$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

				})
		.controller(
				'navigation',

				function($rootScope, $scope, $http, $location, $route) {

					$scope.tab = function(route) {
						return $route.current
								&& route === $route.current.controller;
					};

					var authenticate = function(credentials, callback) {

						var headers = credentials ? {
							authorization : "Basic "
									+ btoa(credentials.username + ":"
											+ credentials.password)
						} : {};

						$http.get('user', {
							headers : headers
						}).success(function(data) {
							if (data.name) {
								$rootScope.authenticated = true;
							} else {
								$rootScope.authenticated = false;
							}
							callback && callback();
						}).error(function() {
							$rootScope.authenticated = false;
							callback && callback();
						});

					}

					authenticate();

					$scope.credentials = {};
					$scope.login = function() {
						authenticate($scope.credentials, function() {
							if ($rootScope.authenticated) {
								console.log("Login succeeded")
								$location.path("/");
								$scope.error = false;
								$rootScope.authenticated = true;
							} else {
								console.log("Login failed")
								$location.path("/login");
								$scope.error = true;
								$rootScope.authenticated = false;
							}
						})
					};

					$scope.logout = function() {
						$http.post('logout', {}).success(function() {
							$rootScope.authenticated = false;
							$location.path("/");
						}).error(function(data) {
							console.log("Logout failed")
							$rootScope.authenticated = false;
						});
					}

				})
		.controller('home', function($scope, $http) {
			$http.get('token').success(function(token) {
				$http({
					url : 'http://localhost:9000',
					method : 'GET',
					headers : {
						'X-Auth-Token' : token.token
					}
				}).success(function(data) {
					$scope.greeting = data;
				});
			})
		})
		.controller(
				'CytoscapeCtrl',
				function($scope, $rootScope, $http) {
					// container objects
					$scope.mapData = [];
					$scope.edgeData = [];
					// data types/groups object - used Cytoscape's shapes just
					// to make
					// it more clear
					// $scope.objTypes =
					// ['ellipse','triangle','rectangle','roundrectangle','pentagon','octagon','hexagon','heptagon','star'];
	//				$scope.objTypes = [ 'container', 'application', 'host',	'application-container' ];

					$http.get('/api/nodes').then(function data(response) {
						console.debug('called api');
						console.debug(response);
						$scope.objTypes = response.data;
						console.debug($scope.objTypes);
						$rootScope.$broadcast('appChanged');

					});

					$http.get('/api/nodeTypes').then(function data(response) {
						console.debug('called nodeTypes');
						console.debug(response);
						$scope.templateData = response.data;
						console.debug($scope.templateData);
						$rootScope.$broadcast('appChanged');

					});
					// add object from the form then broadcast event which
					// triggers the
					// directive redrawing of the chart
					// you can pass values and add them without redrawing the
					// entire
					// chart, but this is the simplest way
					$scope.addObj = function() {
						// collecting data from the form
						var newObj = $scope.form.obj.name;
						var newObjType = $scope.form.obj.objTypes;
						// building the new Node object
						// using the array length to generate an id for the
						// sample (you
						// can do it any other way)
						var newNode = {
							id : 'n' + ($scope.mapData.length),
							name : newObj,
							type : newObjType
						};
						// adding the new Node to the nodes array
						$scope.mapData.push(newNode);
						// broadcasting the event
						$rootScope.$broadcast('appChanged');
						// resetting the form
						$scope.form.obj = '';
						$scope.$broadcast('schemaFormRedraw');
					};

					// add Edges to the edges object, then broadcast the change
					// event
					$scope.addEdge = function() {
						// collecting the data from the form
						var edge1 = $scope.formEdges.fromName.id;
						var edge2 = $scope.formEdges.toName.id;
						// building the new Edge object from the data
						// using the array length to generate an id for the
						// sample (you
						// can do it any other way)
						var newEdge = {
							id : 'e' + ($scope.edgeData.length),
							source : edge1,
							target : edge2
						};
						// adding the new edge object to the adges array
						$scope.edgeData.push(newEdge);
						// broadcasting the event
						$rootScope.$broadcast('appChanged');
						// resetting the form
						$scope.formEdges = '';
					};

					$scope.typeschema = {
				            type: "object",
				            properties: {
				                name: { type: "string", minLength: 2, title: "Name", description: "Name or alias" },
				                title: {
				                    type: "string",
				                    enum: ['dr','jr','sir','mrs','mr','NaN','dj']
				                }
				            }
				        };
					
					$scope.typeschema = { 
							type: "object", 
							title: "DockerContainerProperties properties", 
							properties: {
								entrypoint:{title:"Entry1point in the Dockerfile",type:"string"},
								from:{title:"Base image1",type:"string"},
								cmd:{title:"Command in th1e Dockerfile",type:"string"}
							}
 						};


					
					// sample function to be called when clicking on an object
					// in the
					// chart
					$scope.doClick = function(value) {
						// sample just passes the object's ID then output it to
						// the
						// console and to an alert
						console.debug(value);
//						alert(value);
						console.debug('before'+$scope.typeschema);
						console.debug($scope.typeschema);
						$scope.typeschema = JSON.parse(JSON.stringify(value));
						console.debug('after'+$scope.typeschema);
						console.debug($scope.typeschema);
//						$scope.typeform.pop();
						$scope.$broadcast('schemaFormRedraw');
					};

					$scope.typeform = [ "*", {
						type : "submit",
						title : "Save"
					} ];

					$scope.typemodel = {};

					$scope.onSubmit = function(form) {
						// First we broadcast an event so all fields validate
						// themselves
						$scope.$broadcast('schemaFormValidate');

						// Then we check if the form is valid
						if (form.$valid) {
							// ... do whatever you need to do with your data.
							console.debug("The form is valid, let's send it: "
									+ form.node_id.$modelValue + " "
									+ form.memory.$modelValue);
							var callback = function(data) {
								console
										.debug("sendCustomForm succeeded with data: "
												+ data);
							};
						}
					}

					// reset the sample nodes
					$scope.reset = function() {
						$scope.mapData = [];
						$scope.edgeData = [];
						$rootScope.$broadcast('appChanged');
					}
				});
