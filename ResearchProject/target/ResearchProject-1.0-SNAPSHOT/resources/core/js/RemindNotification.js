/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    ajaxd();
    setInterval(ajaxd, 300000);
});
var objnotification;
var notification = [];
var notificationarray;
var maxpage = 0;
var currentpage = 0;
var content;




function ajaxd() {
    console.log("shedule start");
    var currenturl = window.location.href;
    var currentappurl = getAppURL(currenturl);
    var contextpath = currentappurl.split("/");
    console.log(contextpath);
    console.log("/" + contextpath[1] + "/");
    console.log("tetsing");
    var context = ("/" + contextpath[1] + "/");
    var dataObject = new Object();
    dataObject.iDisplayStart = currentpage;
    dataObject.iDisplayLength = currentpage;
    content = JSON.stringify(dataObject);
    $.ajax({
        type: "post",
        url: context + "getCount",
        cache: false,
        dataType: 'json',
        async: false,
        global: false,
        success: function (response) {
//            console.log(response);
            var notificount = response.count;
//            console.log("notificarion " + notificount);
            $('#badge').text(notificount).addClass('badge bg-color-red bounceIn animated');
            $('#labelclass').removeClass(' active');
            if (maxpage < 0) {
                $('#plus').attr("disabled", "disabled");
                $('#minus').attr("disabled", "disabled");
            } else {
                $('#plus').removeClass(' disabled');
                $('#minus').removeClass(' disabled');
            }
//            console.log(maxpage);
        },
        error: function () {
        }
    });

    $.ajax({
        type: "POST",
        url: context + "loadReminder2",
        cache: false,
        async: false,
        global: false,
        data: {content: content},
        success: function (response) {
//            console.log("sucess");
            response = JSON.parse(response);
//            console.log(response);
            objnotification = response;
//            console.log(objnotification);
//            console.log(response.length);
//            console.log(response);
            var content_body = '';
            notification.length = 0;
            if (response.length > 0) {
                maxpage = response[0].maxPages;
            }
//            console.log(maxpage);
            if (maxpage <= 0) {
                $('#plus').attr("disabled", "disabled");
                $('#plus').hide();
                $('#plus').attr("disabled", "disabled");
                $('#minus').hide();
                $('#minus').attr("disabled", "disabled");
            } else {
                $('#plus').removeClass('disabled');
                $('#plus').show();
                $('#minus').show();
                $('#minus').removeClass('disabled');
            }
            currentpage = 0;
            for (var i = 0; i < response.length; i++) {

                notification.push(response[i].id);
                notificationarray = $.parseJSON(JSON.stringify(notification));
//                console.log(notificationarray);
                console.log(response[i].viewState);
                if (response[i].viewState === "35") {
                    if (response[i].section == "86") {
                        content_body += '<li>'
                                + '<span class = "padding-10">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-blueLight pull-left margin-right-5">'
                                + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Call Back Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    } else {
                        content_body += '<li>'
                                + '<span class = "padding-10">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-blueLight   pull-left margin-right-5">'
                                + '<i class="fa fa-ticket fa-fw fa-2x" aria-hidden="true"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Created Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    }
                } else {

                    if (response[i].section == "86") {
                        content_body += '<li>'
                                + '<span class = "padding-10 unread">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-blue pull-left margin-right-5">'
                                + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Call Back Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    } else {
                        content_body += '<li>'
                                + '<span class = "padding-10 unread">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-green   pull-left margin-right-5">'
                                + '<i class="fa fa-ticket fa-fw fa-2x" aria-hidden="true"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Created Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    }

                }

            }
            $("#notification-body").html(content_body);
        },
        error: function () {
        }

    });

}


$('#notification-body').on('click', 'a', function (e) {
    var currenturl = window.location.href;
    var currentappurl = getAppURL(currenturl);
    var contextpath = currentappurl.split("/");
    e.preventDefault();
    var url = $(this).attr('href');
    var style = $(this).attr('style');
//    console.log(style);
//    console.log(url);
    if (style === "ntfv") {
        $(this).closest('li > span').attr('class', 'padding-10');
        $.ajax({
            type: "POST",
            url: "/" + contextpath[1] + "/updateNotifiState",
            cache: false,
            global: false,
            data: {content: content},
            success: function (response) {
                console.log(response);
            },
            error: function () {
                console.log('Error while request..');
            }
        });
        window.open(url, '_blank');
    } else {
        $.SmartMessageBox({
            title: "Smart Alert!",
            content: "Are you sure you want to delete",
            buttons: '[No][Yes]'
        }, function (ButtonPressed) {
            if (ButtonPressed === "Yes") {
                var parameter = url.split("=");
//        console.log(parameter);
                var dataObject = new Object();
                dataObject.id = parameter[1];
                dataObject.section = parameter[2];
                dataObject.iDisplayStart = currentpage;
                dataObject.iDisplayLength = currentpage;
                var content = JSON.stringify(dataObject);
//        console.log("Conetc " + content);
                $.ajax({
                    type: "GET",
                    url: "/" + contextpath[1] + "/Deletenotification;",
                    cache: false,
                    global: false,
                    data: {content: content},
                    success: function (response) {
//                console.log("sucess");
                        response = JSON.parse(response);
//                console.log(response);
                        objnotification = response;
//                console.log(objnotification);
//                console.log(response.length);
//                console.log(response);
                        var content_body = '';
                        notification.length = 0;
                        if (response.length > 0) {
                            maxpage = response[0].maxPages;
                        }
                        currentpage = 0;
                        for (var i = 0; i < response.length; i++) {

                            notification.push(response[i].id);
                            notificationarray = $.parseJSON(JSON.stringify(notification));
//                    console.log(notificationarray);
//                    console.log(response[i].viewState);
                            if (response[i].viewState === "35") {
                                if (response[i].section == "86") {
                                    content_body += '<li>'
                                            + '<span class = "padding-10">'
                                            + '<em class = "badge padding-5 no-border-radius bg-color-blueLight pull-left margin-right-5">'
                                            + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                            + '</em>'
                                            + '<span>' + response[i].description + '</span>'
                                            + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                            + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                            + '<br><br>'
                                            + '<span class = "pull-right font-xs text-muted"><i> Call Back Time ' + response[i].remindTime + '</i></span>'
                                            + '</span>'
                                            + '</span>'
                                            + '</li>';
                                } else {
                                    content_body += '<li>'
                                            + '<span class = "padding-10">'
                                            + '<em class = "badge padding-5 no-border-radius bg-color-blueLight   pull-left margin-right-5">'
                                            + '<i class="fa fa-ticket fa-fw fa-2x" aria-hidden="true"></i>'
                                            + '</em>'
                                            + '<span>' + response[i].description + '</span>'
                                            + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                            + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                            + '<br><br>'
                                            + '<span class = "pull-right font-xs text-muted"><i> Created Time ' + response[i].remindTime + '</i></span>'
                                            + '</span>'
                                            + '</span>'
                                            + '</li>';
                                }
                            } else {

                                if (response[i].section == "86") {
                                    content_body += '<li>'
                                            + '<span class = "padding-10 unread">'
                                            + '<em class = "badge padding-5 no-border-radius bg-color-blue pull-left margin-right-5">'
                                            + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                            + '</em>'
                                            + '<span>' + response[i].description + '</span>'
                                            + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                            + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                            + '<br><br>'
                                            + '<span class = "pull-right font-xs text-muted"><i> Call Back Time ' + response[i].remindTime + '</i></span>'
                                            + '</span>'
                                            + '</span>'
                                            + '</li>';
                                } else {
                                    content_body += '<li>'
                                            + '<span class = "padding-10 unread">'
                                            + '<em class = "badge padding-5 no-border-radius bg-color-green pull-left margin-right-5">'
                                            + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                            + '</em>'
                                            + '<span>' + response[i].description + '</span>'
                                            + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                            + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                            + '<br><br>'
                                            + '<span class = "pull-right font-xs text-muted"><i> Created Time ' + response[i].remindTime + '</i></span>'
                                            + '</span>'
                                            + '</span>'
                                            + '</li>';
                                }

                            }

                        }
                        $("#notification-body").html(content_body);
                    },
                    error: function () {
                        console.log('Error while request..');
                    }
                });
            }
            if (ButtonPressed === "No") {

            }

        });




    }
//   
});





$('#plus').click(function () {
    var currenturl = window.location.href;
    var currentappurl = getAppURL(currenturl);
    var contextpath = currentappurl.split("/");
//    console.log("maxpage" + maxpage);
    if (currentpage < maxpage) {
//        console.log("tryueee");
        currentpage += 1;
    }
//    console.log("currentpage" + currentpage);
    var dataObject = new Object();
    dataObject.iDisplayStart = currentpage;
    dataObject.iDisplayLength = currentpage;
    var content = JSON.stringify(dataObject);
    $.ajax({
        type: "POST",
        url: "/" + contextpath[1] + "/loadReminder2",
        cache: false,
        async: true,
        global: false,
        data: {content: content},
        success: function (response) {
//            console.log("sucess");
            response = JSON.parse(response);
//            console.log(response);
            objnotification = response;
//            console.log(objnotification);
//            console.log(response.length);
            var content_body = '';
            notification.length = 0;
            if (response.length > 0) {
                maxpage = response[0].maxPages;
            }
            currentpage = 0;
            for (var i = 0; i < response.length; i++) {
                notification.push(response[i].id);
                notificationarray = $.parseJSON(JSON.stringify(notification));
                console.log("notificationarray" + notificationarray);
                if (response[i].viewState === "35") {
                    if (response[i].section == "86") {
                        content_body += '<li>'
                                + '<span class = "padding-10">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-blueLight pull-left margin-right-5">'
                                + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Call Back Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    } else {
                        content_body += '<li>'
                                + '<span class = "padding-10">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-blueLight   pull-left margin-right-5">'
                                + '<i class="fa fa-ticket fa-fw fa-2x" aria-hidden="true"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Created Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    }
                } else {

                    if (response[i].section == "86") {
                        content_body += '<li>'
                                + '<span class = "padding-10 unread">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-blue pull-left margin-right-5">'
                                + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Call Back Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    } else {
                        content_body += '<li>'
                                + '<span class = "padding-10 unread">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-green pull-left margin-right-5">'
                                + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Created Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    }

                }

            }
            $("#notification-body").html(content_body);
//            console.log(notification);
//            $("#activity").trigger("click");
        },
        error: function () {
        }

    });
});


$('#minus').click(function () {
    var currenturl = window.location.href;
    var currentappurl = getAppURL(currenturl);
    var contextpath = currentappurl.split("/");
//    console.log("maxpage" + maxpage);
    if (currentpage > 0) {
//        console.log("tryueee");
        currentpage -= 1;
    }

//    console.log("currentpage" + currentpage);
    var dataObject = new Object();
    dataObject.iDisplayStart = currentpage;
    dataObject.iDisplayLength = currentpage;
    var content = JSON.stringify(dataObject);
    $.ajax({
        type: "POST",
        url: "/" + contextpath[1] + "/loadReminder2",
        cache: false,
        async: true,
        global: false,
        data: {content: content},
        success: function (response) {
//            console.log("sucess");
            response = JSON.parse(response);
//            console.log(response);
            objnotification = response;
//            console.log(objnotification);
//            console.log(response.length);
            var content_body = '';
            notification.length = 0;
            maxpage = response[0].maxPages;
            currentpage = 0;
            for (var i = 0; i < response.length; i++) {
                notification.push(response[i].id);
                notificationarray = $.parseJSON(JSON.stringify(notification));
                console.log("notificationarray" + notificationarray);
                if (response[i].viewState === "35") {
                    if (response[i].section == "86") {
                        content_body += '<li>'
                                + '<span class = "padding-10">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-blueLight pull-left margin-right-5">'
                                + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Call Back Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    } else {
                        content_body += '<li>'
                                + '<span class = "padding-10">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-blueLight   pull-left margin-right-5">'
                                + '<i class="fa fa-ticket fa-fw fa-2x" aria-hidden="true"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Created Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    }
                } else {

                    if (response[i].section == "86") {
                        content_body += '<li>'
                                + '<span class = "padding-10 unread">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-blue pull-left margin-right-5">'
                                + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Call Back Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    } else {
                        content_body += '<li>'
                                + '<span class = "padding-10 unread">'
                                + '<em class = "badge padding-5 no-border-radius bg-color-bg-color-green  pull-left margin-right-5">'
                                + '<i class = "fa fa-phone fa-fw fa-2x"></i>'
                                + '</em>'
                                + '<span>' + response[i].description + '</span>'
                                + '<span class = "pull-right" style="margin-left:10px"><a style="ntfd" href="' + response[i].contextpath + '/Deletenotification?Id=' + response[i].sourcetabelId + '=' + response[i].section + '"> Delete </a></span>&nbsp;'
                                + '<span class = "pull-right"><a style="ntfv" href="' + response[i].contextpath + '/Notification?Id=' + response[i].sourcetabelId + '&section=' + response[i].section + '"> View </a></span> '
                                + '<br><br>'
                                + '<span class = "pull-right font-xs text-muted"><i> Created Time ' + response[i].remindTime + '</i></span>'
                                + '</span>'
                                + '</span>'
                                + '</li>';
                    }

                }


            }
            $("#notification-body").html(content_body);
//            console.log(notification);
//            $("#activity").trigger("click");
        },
        error: function () {
        }

    });
});


$('#activity').click(function () {
    var currenturl = window.location.href;
    var currentappurl = getAppURL(currenturl);
    var contextpath = currentappurl.split("/");
    $('#badge').text("0");
    var dataObject = new Object();
    dataObject.array = notificationarray;
    var content = JSON.stringify(dataObject);
//    console.log("coneeeeeeeeeee" + notification.length);
    if (notification.length !== 0) {
        console.log("Conetc " + content);
        $.ajax({
            type: "POST",
            url: "/" + contextpath[1] + "/updateNotifiState",
            cache: false,
            global: false,
            data: {content: content},
            success: function (response) {
                console.log(response);
            },
            error: function () {
                console.log('Error while request..');
            }
        });
    }
});
