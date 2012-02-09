/*
  ~ Copyright (c) 2011 Automated Logic Corporation
  ~
  ~ Permission is hereby granted, free of charge, to any person obtaining a copy
  ~ of this software and associated documentation files (the "Software"), to deal
  ~ in the Software without restriction, including without limitation the rights
  ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  ~ copies of the Software, and to permit persons to whom the Software is
  ~ furnished to do so, subject to the following conditions:
  ~
  ~ The above copyright notice and this permission notice shall be included in
  ~ all copies or substantial portions of the Software.
  ~
  ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  ~ THE SOFTWARE.
  */

var addDialog;

function setupServiceConfigHandlers() {
    $('select[name="service"]').on('change', function() {
        $.post("ajaxcontroller", {action:"changeservice", service:$(this).val()}, function(result) {
            if (!handleResponseErrors(result)) {
                handleUIResults(result)
                handleData(result)
                noErrors()
                $("#addlocation").button('disable')
            }
        })
    })
}

function setupHandlers() {
    setupServiceConfigHandlers()

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
                handleData(result)
                handleResultData(result)
                noErrors()
            }
        })
    })

    // add location button
    $("#addlocation").button({icons:{primary: 'ui-icon-circle-plus'}}).on('click', function() {
        addDialog.dialog("open");
    });

    // Submit configuration changes
    $("#submitconfig").button().on('click', function() {
        $.post("ajaxcontroller", $("#rates").serialize(), function(result) {
            if (!handleResponseErrors(result)) {
                handleData(result)
                noErrors()
            }
            $("#addlocation").button('enable')
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

function removeAllRows() {
    $("#locations tbody").empty()
}

/*
Associate data returned as JSON with form fields
 */
function handleData(data) {
    if (data.data) {
        for (var key in data.data) {
            $('input[name="'+key+'"]').filter('*[type!="radio"]').val(data.data[key])
            $('input[name="'+key+'"]').filter('*[type="radio"]').removeAttr("checked");
            $('input[name="'+key+'"]').filter('*[type="radio"]').filter('*[value="'+data.data[key]+'"]').attr("checked","true");
        }
    }
    removeAllRows()
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
    // clear out any previous result data that might be currently displayed
    $("#resultname").empty()
    $("#stationdata tbody").empty()
    $("#currentdata tbody").empty()
    $("#forecastdata thead tr").empty()
    $("#forecastdata tbody").empty()
    $("#icondata tbody").empty()

    // put the new data onto the page
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
            $("#currentdata tbody").append("<tr><td>"+value.field+"</td><td>"+value.value+" <i>"+value.units+"</i></td></tr>")
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
                row.append("<td>"+name.value+" <i>"+name.units+"</i></td>")
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

/*
Adds the parts of the UI contributed by the WeatherServiceUI
 */
function handleUIResults(data) {
    if (data.error) {   // currently just used for licensing error
        $("#errortext").text(data.error);
        $("#error").css("display","block")
        return;
    }
    $("#adddialog").html(data.adddialog)

    $("#serviceconfig").html(data.serviceconfig)
    setupServiceConfigHandlers()

    $("#locations thead tr").empty()
    $.each(data.entryheaders, function(index, value) {
        $("#locations thead tr").append("<th>"+value+"</th>");
    })

    if (data.services) {
        $.each(data.services, function(index, value) {
            $('select[name="service"]').append('<option value="'+value.key+'">'+value.display+'</option>')
        })
    }
}

/*
One time initialization on page load to load all dynamic UIs and get data
 */
function initialize() {
    $.get("ajaxcontroller", {action:'init'}, function(result) {
        if (!handleResponseErrors(result)) {
            handleUIResults(result)
            handleData(result)
        }
        setupHandlers()
    })
}

function cleanUpDialog() {
    if (cleanAddDialog != undefined) {
        cleanAddDialog()
    } else {
        $('#adddialog form input[type!="hidden"]').val("")
    }
}

$(document).ready(function() {
    initialize()
    addDialog = $("#adddialog").dialog({
        autoOpen:false,
        title:'Add New Location',
        minWidth:700,   // this seems wide, but avoids a bug in IE with combo boxes that are too wide
                        // test with wbug at zip code 60541
        modal:true,

        buttons: {
            "OK" : function() {
                $.post("ajaxcontroller", $("#adddialog form").serialize(), function(result) {
                    if (!handleResponseErrors(result)) {
                        handleData(result)
                        noErrors()
                    }
                })
                cleanUpDialog()
                $(this).dialog("close")
            },
            "Cancel" : function() {
                cleanUpDialog()
                $(this).dialog("close")
            }
        }
    })
})
