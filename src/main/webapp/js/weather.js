
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
        $.get("ajaxcontroller", $("#rates").serialize(), function(result) {
            handleData(result)
            noErrors()
        })
    })

    $("#error").ajaxError(ajaxErrors);
}

function noErrors() {
    $("#error").css("display","none");
    $(".field-error").toggleClass("field-error", false)
}

function ajaxErrors(e, xhr, settings) {
    var err = $.parseJSON(xhr.statusText)
    if (err.errortype && err.errortype=="validation") {
        $("#"+err.field).toggleClass("field-error", true)
        $("#errortext").text("Invalid value")
        $("#error").css("display","block")
    }
}

function addRowSelectHandler() {
    $("#locations tbody").on("click", "tr", function(e) {
        var row = $(this)
        row.siblings().toggleClass("select", false)
        row.toggleClass("select",true);
        row.data("row")
    })
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
    $.get("ajaxcontroller", {action:'init'}, handleData)
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
                alert('post data')
                $(this).dialog("close")
            },
            "Cancel" : function() {
                $(this).dialog("close")
            }
        }
    })
})
