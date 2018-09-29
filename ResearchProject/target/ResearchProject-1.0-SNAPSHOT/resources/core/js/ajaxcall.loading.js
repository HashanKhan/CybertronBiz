/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$('form').submit(function (event) {
    if (event.target.id !== "caseReport" && event.target.id !== "callReport" && event.target.id !== "accountReport" && event.target.id !== "caseAttachment") {
        $.blockUI({css: {
                border: 'transparent',
                backgroundColor: 'transparent'
            },
            message: '<div id="circularG"><div id="circularG_1" class="circularG"></div><div id="circularG_2" class="circularG"></div><div id="circularG_3" class="circularG"></div><div id="circularG_4" class="circularG"></div><div id="circularG_5" class="circularG"></div><div id="circularG_6" class="circularG"></div><div id="circularG_7" class="circularG"></div><div id="circularG_8" class="circularG"></div></div>',
            baseZ: 2000
        });
    }
});

$('#download_pdf').click(function () {
    startDownload();
});

$('#download_excel').click(function () {
    startDownload();
});

$('#downloadattachment').click(function () {
    startDownload();
});

function startDownload() {
    var token = new Date().getTime(); //use the current timestamp as the token value
    $('#download_token_value_id').val(token);
    fileDownloadCheckTimer = window.setInterval(function () {
        var cookieValue = $.cookie('fileDownloadToken');
        if (parseInt(cookieValue) === parseInt(token))
            finishDownload();
    }, 1000);
}

function finishDownload() {
    window.clearInterval(fileDownloadCheckTimer);
    $.removeCookie('fileDownloadToken'); //clears this cookie value
    $.unblockUI();
}

$("form").bind("invalid-form.validate", function () {
    $.unblockUI();
});

var spinnerText;
var spinnerElem;

$(document).ajaxStart(function (e) {
    var elem = e.target.activeElement;

    if ($(elem).hasClass('btn-spinner')) {
        spinnerElem = elem;
        spinnerText = $(spinnerElem).html();
        $(spinnerElem).html("<i class='fa fa-spinner fa-spin '></i> Processing").attr("disabled", "disabled");
    }



    $.blockUI({css: {
            border: 'transparent',
            backgroundColor: 'transparent'
        },
        message: '<div id="circularG"><div id="circularG_1" class="circularG"></div><div id="circularG_2" class="circularG"></div><div id="circularG_3" class="circularG"></div><div id="circularG_4" class="circularG"></div><div id="circularG_5" class="circularG"></div><div id="circularG_6" class="circularG"></div><div id="circularG_7" class="circularG"></div><div id="circularG_8" class="circularG"></div></div>',
        baseZ: 2000
    });
});

$(document).ajaxStop(function () {
    $(spinnerElem).html(spinnerText).removeAttr("disabled");
    $.unblockUI();
});