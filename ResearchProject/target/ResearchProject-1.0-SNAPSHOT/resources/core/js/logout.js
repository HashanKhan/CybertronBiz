/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function logout(context) {
    var username = null;
    var getlogoutresong = "";
//    console.log(context+"/login/activuser");
    $.ajax({
        async: false,
        type: "GET",
        url: context + "/login/activuser",
        cache: false,
        success: function (response) {
            username = response;
        },
        error: function () {
            console.log('Error while request..');
        }
    });

    $.ajax({
        async: false,
        type: "GET",
        url: context + "/login/getlogoutresons",
        cache: false,
        success: function (response) {
            for (var i = 0; i < response.length; i++) {
                getlogoutresong += response[i].toString();
            }
//            console.log(response);
        },
        error: function () {
            console.log('Error while request..');
        }
    });


    $.SmartMessageBox({
        title: "<font color='#a57225'><i class='fa fa-sign-out'></i></font> Logout <b><font color='#a57225'>" + username + "</font></b> ?",
        content: " Please select logout reason.",
        buttons: '[No][Yes]',
        input: "select",
        options: "" + getlogoutresong + ""
    }, function (ButtonPress, Value) {
        var logoutreson = null;
        if (ButtonPress === "Yes") {
            $.ajax({
                async: false,
                type: "POST",
                url: context + "/logout/logoutreson",
                cache: false,
                data: 'logoutreson=' + Value,
                success: function (response) {
                    logoutreson = response;
                },
                error: function () {
                    console.log('Error while request..');
                }
            });
            if (logoutreson) {
                window.location = context + "/?auth=logout";
            }
        }
        if (ButtonPress === "No") {
        }

    });
}



