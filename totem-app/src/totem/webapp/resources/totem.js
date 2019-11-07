function dispatchNumPadKey(value) {
    console.log(value)
    v = $('.numPadTarget').val();
    console.log(v)
    if (value >= 0) {
        $('.numPadTarget').val(v + value)
    } else if (v.length > 0) {
        $('.numPadTarget').val(v.substring(0, v.length - 1))
    }
    console.log($('.numPadTarget').val());
}

function initProgressBar(value, percent) {
    $('#progressbar').progress('set active');
    $('#progressbar').progress({total: value});
    incrementPorcentProgressBar(percent);
}

function completeProgressBar() {
    $('#progressbar').progress('complete');
}

function isErrorProgressBar() {
    $('#progressbar').progress('set error');
}

function incrementPorcentProgressBar(value) {
    $('#progressbar').progress({percent: value});
}

function incrementProgressBar(max) {
    for (var i = 0; i > max; i++) {
        $('#progressbar').progress('increment');
    }
}


function stopLoading() {
    $('#loading').hide();
    $('#guiaFrame').show();
};

function initGuiaTramites() {
    window.addEventListener('message', function (event) {
        console.log(event.data);
        setTimeout(500, new function () {
            if (event.data.length > 3 && event.data.substring(0, 3) == 'hgt') {
                var value = event.data.substring(3, event.data.length);
                if (value == "100px") {
                    value = "1350px";
                }
                console.log(value);
                frame = document.getElementById("guiaFrame");
                frame.style.visibility = 'hidden';
                frame.style.height = value;
                frame.style.visibility = 'visible';

            }
            if (event.data.length >= 3 && event.data.substring(0, 3) == 'rst') {
                window.scrollTo(0, 0);
                $(".contenidoTotemScroll").scrollTop(0);
            }
            updateScrollingAdvice();
        });
    });
}

$(function () {
    $(document).bind("contextmenu", function (e) {
        return false;
    });
});

function setupConfig(callbackUrl) {
    Wicket.Ajax.post({u: callbackUrl + "&idSede=025"});
    /**
     $.ajax({
            url: "http://localhost:5987/config",
            method: "GET",
            headers: {"Access-Control-Allow-Origin": "*"},
            timeout: 10000
        }
     ).done(function (data) {
        Wicket.Ajax.post({u: callbackUrl + "&idSede=" + data});
    }).fail(function error (data) {
        console.log(data)
        Wicket.Ajax.post({u: callbackUrl + "&error=" + data});
    });
     **/
}


function showDimmer() {
    $('.dimmer').addClass('active');
}

function hideDimmer() {
    $('.dimmer').removeClass('active');
}

function adviceDown() {
    console.log("adviceDown")
    $('.scrolldown')
        .transition('set looping')
        .transition('pulse', '1000ms')
    ;
}

function unadviceDown() {
    console.log("unadviceDown")
    $('.scrolldown')
        .transition('remove looping')
        .transition('stop all')
    ;
}

function adviceUp() {
    console.log("adviceUp")
    $('.scrollup')
        .transition('set looping')
        .transition('pulse', '1000ms')
    ;
}

function unadviceUp() {
    console.log("unadviceUp")
    $('.scrollup')
        .transition('remove looping')
        .transition('stop all')
    ;
}


function down() {
    var current = $(".contenidoTotemScroll").scrollTop();
    console.log(current)
    var h = $('.contenidoTotem').prop('scrollHeight');
    var total = $('.contenidoTotemScroll').prop('scrollHeight');
    var delta = total - current;
    console.log(delta);
    if (delta >= h) {
        pos = current + h
        $(".contenidoTotemScroll").scrollTop(pos);
    }
    current = $(".contenidoTotemScroll").scrollTop();
    console.log(current)
    updateScrollingAdvice();
}

function up() {
    var current = $(".contenidoTotemScroll").scrollTop();
    var h = $('.contenidoTotem').prop('scrollHeight');
    var total = $('.contenidoTotemScroll').prop('scrollHeight');
    console.log(current);
    var pos = current - h;
    if (current + h >= total) {
        pos = Math.floor(current / h) * h;
    }
    console.log(pos);
    $(".contenidoTotemScroll").scrollTop(pos);
    updateScrollingAdvice();
}

function updateScrollingAdvice() {
    console.log("update scrolling")
    window.setTimeout(100, new function () {

        var top = $(".contenidoTotemScroll").scrollTop();
        var total = $('.contenidoTotemScroll').prop('scrollHeight');
        var h = $('.contenidoTotem').prop('scrollHeight');
        console.log(top + ", " + h + ", " + total)
        if (top + h < total - 3) {
            adviceDown();
        } else {
            unadviceDown();
        }
        if (top > 0) {
            adviceUp();
        } else {
            unadviceUp();
        }
    });
}

function printTicket(ticketData) {
    $.post( "http://localhost:5987/ticket", ticketData)
        .done(function( data )
        {
            obj = JSON.parse(data);
        }).fail( function(data)
        {
            alert(data);
        });

    setTimeout(function()
    {
        var sede = window.location.href.split("&")[1];
        var uri = window.location.href.split("?")[0];
        document.location.href=uri+"?"+sede;
    }, 5000);
}



function printTicketOnline(ticketData) {
    setTimeout(function()
    {
        var sede = window.location.href.split("&")[1];
        var uri = window.location.href.split("?")[0];
        document.location.href=uri+"?"+sede;
    }, 5000);
}

