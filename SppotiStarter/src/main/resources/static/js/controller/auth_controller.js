/**
 * Created by: Wail DJENANE On Nov 16, 2016
 */

'use strict';

App.controller('AuthController', ['$location', '$scope', '$log', 'AuthService', function ($location, $scope, $log, AuthService) {
    var self = this;
    self.user = {username: '', password: ''};

    //self.connected = AuthService.isLoggedIn();


    self.regexp = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;

    /* --- */

    /*
     This function is called from login controller, fields are checked and validated
     using regex and length properties.
     If OK, login service is called !
     */
    self.submit = function () {
        if (self.user.password.length > 0 && self.user.username.length
            && $scope.loginForm.username.$valid) {

            AuthService.login(self.user, function success(response) {
                    console.log("Login JSON response: " + JSON.stringify(response.data));
                    localStorage.setItem("session", {});

                    if (localStorage.getItem("user") !== null)
                        localStorage.removeItem("user");

                    localStorage.setItem("user", JSON.stringify(response.data));
                    switch (response.status) {
                        case 200 :
                            $location.path('/admin');
                            /*switch (response.data.role) {
                             case 'CONSULTANT':
                             $location.path('/consultant');
                             break;
                             case 'ADMINISTRATOR' :
                             $location.path('/admin');
                             break;
                             }*/
                            break;
                        case 401:
                            self.loginFailure = "mot de passe ou nom d'utilisateur invalide"
                            break;
                    }
                },
                function failure(response) {
                    console.error("We couldn't find you !!");
                });

        } else {
            //form validation failed
            /*
             Process error here
             */
            console.log("erreur saisi");
        }
    };

    //logout controller
    self.logout = function () {
        AuthService.logout(self.user, function success(response) {
                //redirect to login page
                $location.path('/');
            },
            function failure(response) {
                console.error("Error found on logout !");
            });

    };

    /*
     If session exist then check if the session item was not deleted by user,
     if so, logout !
     */
    self.isLoggedIn = function () {
        return AuthService.isLoggedIn();

    };

    /* ------------ form validation functions  -------------------*/
    //check email form
    self.checkMail = function (o, regexp) {
        if (!( regexp.test(o) )) {
            return false;
        }
        return true;
    }

    //check input Length
    self.checkLength = function (o) {
        if (o == "") {
            return false;
        }
        return true;
    };

}]);
