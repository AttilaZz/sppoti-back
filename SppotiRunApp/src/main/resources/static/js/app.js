/**
 * Created by: Wail DJENANE On Nov 16, 2016
 */


'use strict';

var App = angular.module('sppoti', ['ngRoute', 'ngMessages', 'ngCookies'])
    .constant('Routes', {
        route1: '/',
        route2: '/admin',
        route3: '/404'
    })
    .config(['$routeProvider', 'Routes', function ($routeProvider, Routes) {
        $routeProvider
            .when(Routes.route1, {
                templateUrl: '/views/login.html',
                controller: 'AuthController',
                title: 'Login',
                css: '/css/login.css',
                resolve: {
                    "check": function ($location) {
                        if (localStorage.getItem("session") !== null) {
                            $location.path(Routes.route1);
                        }
                    }
                }
            })

            .when(Routes.route2, {
                templateUrl: '/views/admin.html',
                controller: 'AdminController',
                title: 'Admin',
                resolve: {
                    "check": function ($location) {
                        if (localStorage.getItem("session") !== null) {
                            //Do something
                        } else {
                            $location.path(Routes.route1);    //redirect user to home.
                            alert("You don't have access here");
                        }
                    }
                }
            })

            .otherwise({
                redirectTo: Routes.route1
            });

    }])
    .directive('head', ['$rootScope', '$compile',
        function ($rootScope, $compile) {
            return {
                restrict: 'E',
                link: function (scope, elem) {
                    var html = '<link rel="stylesheet" ng-repeat="(routeCtrl, cssUrl) in routeStyles" ng-href="{{cssUrl}}" >';
                    elem.append($compile(html)(scope));

                    scope.routeStyles = {};

                    $rootScope.$on('$routeChangeStart', function (e, next, current) {

                        if (current && current.$$route && current.$$route.css) {
                            if (!Array.isArray(current.$$route.css)) {
                                current.$$route.css = [current.$$route.css];
                            }
                            angular.forEach(current.$$route.css, function (sheet) {
                                scope.routeStyles[sheet] = undefined;
                            });
                        }

                        if (next && next.$$route && next.$$route.css) {
                            if (!Array.isArray(next.$$route.css)) {
                                next.$$route.css = [next.$$route.css];
                            }
                            angular.forEach(next.$$route.css, function (sheet) {
                                scope.routeStyles[sheet] = sheet;
                            });
                        }

                    });

                }
            };
        }
    ])
    .run(function ($rootScope, $route, $cookies, $log) { // inject title in each view when routing
        $rootScope.page_title = 'Login';
        $log.info("X-XSRF-TOKEN:" +
            "" + $cookies.get('X-XSRF-TOKEN'));

        $rootScope.$on('$routeChangeSuccess', function () {
            $rootScope.page_title = $route.current.title;
        });
    });


