
var addDialog;

function setupHandlers() {
    //addRowSelectHandler
    addDeleteHandler();
    addShowDataHandler();

    // add location button
    $("#addlocation").button({icons:{primary: 'ui-icon-circle-plus'}}).on('click', function() {
        addDialog.dialog("open");
    });

    $("#submitrates").button().on('click', function() {
        $.get("ajaxcontroller", $("#rates").serialize(), function(result) {
            alert("Posted:"+result)
        })
    })
/*
    $("#submitrates").button().on('click', function() {
        alert("submitrates")
    })
    */
}

function addDeleteHandler() {
    $("#locations").on('click', "button.del", function() {
        alert("Delete:"+$(this).parents("tr").data("row"))
    })
}

function addShowDataHandler() {
    $("#locations").on('click', "button.data", function() {
        alert("Show data for:"+$(this).parents("tr").data("row"))
    })
}

function addRowSelectHandler() {
    $("#locations tbody").on("click", "tr", function(e) {
        var row = $(this)
        row.siblings().toggleClass("select", false)
        row.toggleClass("select",true);
        row.data("row")
    })
}

function initData() {
    $.get("ajaxcontroller", {action:'init'}, function(data) {
        $("#condition_rate").val(data.conditionrefresh)
        $("#forecast_rate").val(data.forecastrefresh)
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
                alert('post data')
                $(this).dialog("close")
            },
            "Cancel" : function() {
                $(this).dialog("close")
            }
        }
    })
})
