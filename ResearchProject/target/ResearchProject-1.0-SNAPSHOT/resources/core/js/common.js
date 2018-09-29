$(document).ready(function () {
    $(window).keydown(function (event) {
        if ((event.target.nodeName !== "TEXTAREA") && event.keyCode === 13) {
            event.preventDefault();
            return false;
        }
    });

    $(function () {
        var currenturl = window.location.href;
        var currentappurl = getAppURL(currenturl);
        $('nav a').each(function () {
            var pageurl = getAppURL(this.href);
            if (currentappurl === pageurl) {
                $(this).closest("li").addClass("active");
                $(this).closest("li").parents('li').addClass("open");
                $(this).parents('ul').attr('style', 'display: block');
            }
        });
    });
});

function getAppURL(url) {
    var urlarray = url.split('/');
    var appurl = "";
    for (var i = 3; i < urlarray.length; i++) {
        if (i === 3) {
            appurl += "/" + urlarray[i] + "/";
        } else if ((i + 1) === urlarray.length) {
            appurl += urlarray[i].split('?')[0];
        } else {
            appurl += urlarray[i] + "/";
        }
    }
    return appurl;
}

function padf(str, max) {
    if (str) {
        str = str.toString();
        return str.length < max ? padf("0" + str, max) : str;
    } else {
        return '';
    }
}
