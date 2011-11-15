
var addDialog;

function setupHandlers() {
    // Delete handler
    $("#locations").on('click', "button.del", function() {
        alert("Delete:"+$(this).parents("tr").data("row"))
    })

    // Show Data
    $("#locations").on('click', "button.data", function() {
        alert("Show data for:"+$(this).parents("tr").data("row"))
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
                    $("#"+error.field).toggleClass("field-error", true).attr("title", error.message)
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
        $("#condition_rate").val(data.conditionrefresh)
        $("#forecast_rate").val(data.forecastrefresh)
        $("#locations tbody").empty()
        for (var i=0; i<data.locations.length; i++) {
            var next = data.locations[i]
            var row = $("#locations tbody").append("<tr><td><button class='del'></button></td><td>"+next.path+"</td>"+
            "<td>"+next.zip+"</td>"+
            "<td>"+next.update+"</td>"+
            "<td><button class='data'>Show Data</button></td></tr>").children().last()
            row.data("row", i)
        }
        $("#locations button.del").button({text:false, icons:{primary: 'ui-icon-circle-minus'}})
        $("#locations button.data").button()
    }

function initData() {
    $.get("ajaxcontroller", {action:'init'}, function(result) {
        if (!handleResponseErrors(result)) {
            handleData(result)
        }
    })
}

function initAddDialog() {
    $.get("ajaxcontroller", {action:'adddialog'}, function(content){
        $("#adddialog").html(content)
    })

}

$(document).ready(function() {
    setupHandlers()
    initData()
    initAddDialog()
    addDialog = $("#adddialog").dialog({
        autoOpen:false,
        title:'Add New Location',
        minWidth:400,

        buttons: {
            "OK" : function() {
                alert('post data')
                $(this).dialog("close")
            },
            "Cancel" : function() {
                $(this).dialog("close")
            }
        }
    })
})
