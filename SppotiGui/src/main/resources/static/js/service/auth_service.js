/**
 Edited by Wail DJENANE on 2/11/2016
 */

'use strict';

App.service('AuthService', ['$http', 'Paths', '$cookies', function ($http, Paths, $cookies) {

    var self = this;

    self.rootAddress = Paths.applicationRootAddress;
    self.loginLink = Paths.loginAddress;
    self.logoutLink = Paths.logoutAddress;

    //this function require only a good link sent via the controller and a valid spring configuration

    self.login = function (config, success, failure) {
        var csrf = $cookies.get('X-XSRF-TOKEN');
        var contentTypeHeader = {'Content-Type': 'application/x-www-form-urlencoded'};
        var header = {headers: contentTypeHeader};

        var url = "" + self.rootAddress + self.loginLink;
        $http.post(url, "username=" + config.username +
            "&password=" + config.password + "&_csrf=" + csrf, header).then(
            function (response) {
                console.log("success method from login service");
                success(response);
            }, function (response) {
                console.log("error server from login service");
                failure(response);
            }
        );
    };


    //logout angular, the trick is to delete the session item from local storage
    self.logout = function (success, failure) {
        var url = "" + self.rootAddress + self.logoutLink;
        return $http.get(url)
            .then(
                function (response) {
                    localStorage.removeItem("session");
                    console.log('logged Out');
                    success(response);
                },
                function (errResponse) {
                    console.error("Error while logging Out");
                    failure(errResponse);
                }
            );
    };

    // check if the session exist in the server
    self.isLoggedIn = function () {
        return localStorage.getItem("session") !== null;
    };

}]);

