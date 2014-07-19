/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    var parameters = {};
    parameters['time'] = new Date().getMilliseconds();
    parameters['first'] = 1;
    $.ajax({url: "/Chat/chat",
        data: parameters,
        success: function(data) {
            if (data) {
                var textH = $("#history").text("");
                textH = $("#history").append("<p>" + data + "</p>");
                //$("#history").append(textH);


            }
        },
        type: "GET",
        complete: poll
    });
    function poll() {
        var parameters = {};
        parameters['time'] = new Date().getMilliseconds();
        //parameters['first'] = param;
        $.ajax({url: "/Chat/chat",
            data: parameters,
            success: function(data) {
                if (data) {
                    var textH = $("#history").append("<p>" + data + "</p>");
                    //$("#history").append(textH);
                }
            },
            type: "GET",
            complete: poll
        });
    }
    ;
//    poll(1);



    $('#send_btn').click(function() {
        $('#msgId').disabled = true;
        $('#send_btn').disabled = true;
        if (($('#msgId').val()).length > 0) {
            $.ajax({
                url: "/Chat/chat",
                data: {'msg': $('#msgId').val()},
                type: "POST",
                complete: function() {
                    $('#msgId').get(0).value = "";
                    $('#msgId').disabled = false;
                    $('#send_btn').disabled = false;

                }
            });
        }
    });

    $("#msgId").keypress(function(event) {
        if (event.which == 13 && ($('#msgId').val()).length > 0) {
            $('#msgId').disabled = true;
            $('#send_btn').disabled = true;

            $.ajax({
                url: "/Chat/chat",
                data: {'msg': $('#msgId').val()},
                type: "POST",
                complete: function() {
                    $('#msgId').get(0).value = "";
                    $('#msgId').disabled = false;
                    $('#send_btn').disabled = false;

                }
            });
        }

    });



});