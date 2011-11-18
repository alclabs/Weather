
var addDialog;

function setupHandlers() {
    // Delete handler
    $("#locations").on('click', "button.del", function() {
        var num = $(this).parents("tr").data("row")
        $.post("ajaxcontroller", {action:"deleterow", rownum:num}, function(result) {
            if (!handleResponseErrors(result)) {
                handleData(result)
                noErrors()
            }
        })
    })

    // Show Data
    $("#locations").on('click', "button.data", function() {
        var num = $(this).parents("tr").data("row")
        $.get("ajaxcontroller", {action:"showdata", rownum:num}, function(result) {
            if (!handleResponseErrors(result)) {
                handleResultData(result)
                noErrors()
            }
        })
    })

    // add location button
    $("#addlocation").button({icons:{primary: 'ui-icon-circle-plus'}}).on('click', function() {
        addDialog.dialog("open");
    });

    // Apply Rates
    $("#submitrates").button().on('click', function() {
        $.post("ajaxcontroller", $("#rates").serialize(), function(result) {
            if (!handleResponseErrors(result)) {
                handleData(result)
                noErrors()
            }
        })
    })

    $("#error").ajaxError(ajaxErrors);
}

function noErrors() {
    $("#error").css("display","none");
    $(".field-error").toggleClass("field-error", false).attr("title","")
}

function ajaxErrors(e, xhr, settings) {
    $("#errortext").text("Error communicating with server")
    $("#error").css("display","block")
}

function addRowSelectHandler() {
    $("#locations tbody").on("click", "tr", function(e) {
        var row = $(this)
        row.siblings().toggleClass("select", false)
        row.toggleClass("select",true);
        row.data("row")
    })
}

function handleResponseErrors(data) {
    if (data.errors) { // if we have any errors
        var errors = data.errors
        var globalErrors = false;
        if (errors) {
            $("#errortext").empty()
            for (var i in errors) {
                var error = errors[i]
                if (error.errortype && error.errortype=="validation") {
                    $('input[name="'+error.field+'"]').toggleClass("field-error", true).attr("title", error.message)
                } else {
                    globalErrors = true;
                    $("#errortext").append("<div>"+error.message+"</div>")
                }
            }
            if (globalErrors) {
                $("#error").css("display","block")
            }
        }

        return true
    } else
        return false
}

function handleData(data) {
    if (data.data) {
        for (var key in data.data) {
            $('input[name="'+key+'"]').filter('*[type!="radio"]').val(data.data[key])
            $('input[name="'+key+'"]').filter('*[type="radio"]').removeAttr("checked");
            $('input[name="'+key+'"]').filter('*[type="radio"]').filter('*[value="'+data.data[key]+'"]').attr("checked","true");
        }
    }
    $("#locations tbody").empty()
    if (data.locations) {
        for (var i=0; i<data.locations.length; i++) {
            var next = data.locations[i]
            var row = $("<tr></tr>")
            row.append("<td><button class='del' type='button'></button></td>")
            $.each(next, function(index, value) {
                row.append("<td>"+value+"</td>")
            })
            row.append("<td><button class='data' type='button'>Show Data</button></td>");
            $("#locations tbody").append(row)
            row.data("row", i)
        }
    }
    if (data.currentservice) {
        $('select[name="service"] option[value="'+data.currentservice+'"]').attr("selected","selected")
    }
    $("#locations button.del").button({text:false, icons:{primary: 'ui-icon-circle-minus'}})
    $("#locations button.data").button()
    $("#resultdata").css("display","none");

}

/*
Handles results of a showdata request.  This has a block of html content under
data.resultdata.
 */
function handleResultData(data) {
    if (data.name) {
        $("#resultname").text(data.name)
    }
    if (data.station) {
        $.each(data.station, function(index, value) {
            $("#stationdata tbody").append("<tr><td>"+value.field+"</td><td>"+value.value+"</td></tr>")
        })
    }
    if (data.current) {
        $.each(data.current, function(index, value) {
            $("#currentdata tbody").append("<tr><td>"+value.field+"</td><td>"+value.value+"</td></tr>")
        })
    }
    if (data.forecastheaders) {
        $.each(data.forecastheaders, function(index, name) {
            $("#forecastdata thead tr").append("<th>"+name+"</th>")
        })
    }
    if (data.forecast) {
        $.each(data.forecast, function(index, rowData) {
            var row = $("<tr></tr>")
            $.each(rowData, function(index, name) {
                row.append("<td>"+name+"</td>")
            })
            $("#forecastdata tbody").append(row)
        })
    }
    if (data.icon) {
        $.each(data.icon, function(index, name) {
            $("#icondata tbody").append("<tr><td>"+name.field+"</td><td>"+name.value+"</td></tr>")
        })
    }

    $("#resultdata").css("display","block")
}

function handleUIResults(data) {
    $("#adddialog").html(data.adddialog)
    $("#serviceconfig").html(data.serviceconfig)
    $.each(data.entryheaders, function(index, value) {
        $("#locations thead tr").append("<th>"+value+"</th>");
    })
    $("#locations thead tr").append("<th>Last Update</th>");
    if (data.services) {
        $.each(data.services, function(index, value) {
            $('select[name="service"]').append('<option value="'+value.key+'">'+value.display+'</option>')
        })
    }
}

function initData() {
    $.get("ajaxcontroller", {action:'init'}, function(result) {
        if (!handleResponseErrors(result)) {
            handleUIResults(result)
            handleData(result)
        }
    })
}

$(document).ready(function() {
    setupHandlers()
    initData()
    addDialog = $("#adddialog").dialog({
        autoOpen:false,
        title:'Add New Location',
        minWidth:400,

        buttons: {
            "OK" : function() {
                $.post("ajaxcontroller", $("#adddialog form").serialize(), function(result) {
                    if (!handleResponseErrors(result)) {
                        handleData(result)
                        noErrors()
                    }
                })
                $('#adddialog form input[type!="hidden"]').val("")
                $(this).dialog("close")
            },
            "Cancel" : function() {
                $('#adddialog form input[type!="hidden"]').val("")
                $(this).dialog("close")
            }
        }
    })
})
