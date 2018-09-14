

// This handles ajax requests
let dataHandler = {

    decreaseProductQuantity: function (productId, callback) {
        $.ajax({
            type: "POST",
            url: "/handle-shopping-cart",
            data: {"quantity": "decrease",
                   "id": productId},
            success: function (newValues) {
                callback(newValues);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    },

    increaseProductQuantity: function (productId, callback) {
        $.ajax({
            type: "POST",
            url: "/handle-shopping-cart",
            data: {"quantity": "increase",
                   "id": productId},
            success: function (newValues) {
                callback(newValues);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    },

    getShoppingCartInfo: function (callback) {
        $.ajax({
            type: "GET",
            url: "/handle-shopping-cart",
            success: function (shoppingCartInfo) {
                callback(shoppingCartInfo);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    },

    registerUser: function (userEmail, userPassword, userPasswordConfirm, callback) {
        $.ajax({
            type: "POST",
            url: "/handle-user",
            data: {"event": "register",
                "userEmail": userEmail,
                "userPassword": userPassword,
                "userPasswordConfirm": userPasswordConfirm},
            success: function (alertInfo) {
                callback(alertInfo);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    },

    loginUser: function (userEmail, userPassword, callback) {
        $.ajax({
            type: "POST",
            url: "/handle-user",
            data: {"event": "login",
                "userEmail": userEmail,
                "userPassword": userPassword},
            success: function (alertInfo) {
                callback(alertInfo);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    },

    logoutUser: function (callback) {
        $.ajax({
            type: "GET",
            url: "/handle-user",
            success: function (alertInfo) {
                callback(alertInfo);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    },

    checkOutCartSameAddress: function (shoppingCartId, firstName, lastName, address, country,
                                       city, zipCode, isShippingSame, callback) {
        $.ajax({
            type: "POST",
            url: "/handle-checkout",
            data: {"event": "checkout",
                "shoppingCartId": shoppingCartId,
                "firstName": firstName,
                "lastName": lastName,
                "address": address,
                "country": country,
                "city": city,
                "zipCode": zipCode,
                "isShippingSame": isShippingSame},
            success: function (alertInfo) {
                callback(alertInfo);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    },

    checkoutCartDifferentAddress: function (shoppingCartId, firstName, lastName, address, country,
                                            city, zipCode, isShippingSame, addressShipping,
                                            countryShipping, cityShipping, zipCodeShipping, callback) {
        $.ajax({
            type: "POST",
            url: "/handle-checkout",
            data: {"event": "checkout",
                "shoppingCartId": shoppingCartId,
                "firstName": firstName,
                "lastName": lastName,
                "address": address,
                "country": country,
                "city": city,
                "zipCode": zipCode,
                "isShippingSame": isShippingSame,
                "addressShipping": addressShipping,
                "countryShipping": countryShipping,
                "cityShipping": cityShipping,
                "zipCodeShipping": zipCodeShipping},
            success: function (alertInfo) {
                callback(alertInfo);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    },

    payment: function (shoppingCartId, callback) {
        $.ajax({
            type: "POST",
            url: "/handle-checkout",
            data: {"event": "payment",
                "shoppingCartId": shoppingCartId},
            success: function (alertInfo) {
                callback(alertInfo);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    },

    confirmation: function (shoppingCartId, callback) {
        $.ajax({
            type: "POST",
            url: "/handle-checkout",
            data: {"event": "confirmation",
                "shoppingCartId": shoppingCartId},
            success: function (alertInfo) {
                callback(alertInfo);
            },
            error: function (errorInfo) {
                dom.handleError(errorInfo);
            }
        })
    }

};