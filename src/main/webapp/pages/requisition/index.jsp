<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authentication var="user" property="principal" />
<sec:authorize access="isAuthenticated()"  var="isLogin" />
<sec:authorize access="hasRole('USER')"  var="isUser" />
<sec:authorize access="hasRole('OPER')"  var="isOper" />
<sec:authorize access="hasRole('ADMIN')"  var="isAdmin" />

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle} - Requisition</title>
        <style>
            h1{
                color: red;
            }
            thead input {
                width: 100%;
            }
            .box {
                width:1270px;
                padding:20px;
                background-color:#fff;
                border:1px solid #ccc;
                border-radius:5px;
                margin-top:25px;
            }
        </style>
        <link rel="stylesheet" href="<c:url value="/libs/bootstrap/bootstrap.css" />" />
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-dt/jquery.dataTables.css" />" />
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-fixedheader-dt/fixedHeader.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-select-dt/select.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-buttons-dt/buttons.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/bootstrap-datepicker/bootstrap-datepicker3.css" />"/>

        <script src="<c:url value="/libs/jQuery/jquery.js" />"></script>
        <script src="<c:url value="/libs/bootstrap/bootstrap.js" />"></script>
        <script src="<c:url value="/libs/datatables.net/jquery.dataTables.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-fixedheader/dataTables.fixedHeader.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-select/dataTables.select.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-buttons/dataTables.buttons.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-buttons/buttons.flash.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-buttons/buttons.colVis.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-buttons/buttons.html5.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-buttons/buttons.print.js" />"></script>
        <script src="<c:url value="/libs/jszip/jszip.js" />"></script>
        <script src="<c:url value="/libs/bootstrap-datepicker/bootstrap-datepicker.js" />"></script>
        <script src="<c:url value="/extraJs/jquery.spring-friendly.js" />"></script>
        <script src="<c:url value="/libs/moment/moment.js" />"></script>
        <script src="<c:url value="/libs/jsog/JSOG.js" />"></script>
        <script src="<c:url value="/libs/remarkable-bootstrap-notify/bootstrap-notify.js" />"></script>
        <script>
            $(function () {
                var dataTable_config = {
                    "processing": true,
                    "serverSide": true,
                    "fixedHeader": true,
                    "orderCellsTop": true,
                    "ajax": {
                        "url": "<c:url value="/RequisitionController/findAll" />",
                        "type": "POST",
                        "data": function (d) {
//                            https://medium.com/code-kings/datatables-js-how-to-update-your-data-object-for-ajax-json-data-retrieval-c1ac832d7aa5
                            d.startDate = $("#datepicker_from").val();
                            d.endDate = $("#datepicker_to").val();
                        },
                        "dataSrc": function (json) {
                            return JSOG.decode(json.data);
                        }
                    },
                    "columns": [
                        {data: "id"},
                        {data: "po"},
                        {data: "materialNumber"},
                        {data: "amount"},
                        {data: "requisitionState.name"},
                        {data: "user.username"},
                        {data: "createDate"},
                        {data: "lastUpdateDate"},
                        {data: "requisitionState.id"},
                        {data: "id"}
                    ],
                    "columnDefs": [
                        {
                            "targets": [0],
                            "visible": false,
                            "searchable": false
                        },
                        {
                            "targets": [6, 7],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                return data == null ? "n/a" : formatDate(data);
                            }
                        },
                        {
                            "targets": [8],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                return data == 3 ? (${isOper} ? ("<input type='button' class='accept btn btn-default' value='核可' />" +
                                        "<input type='button' class='reject btn btn-default' value='拒絕' />") : "待處理") : "已處理";
                            }
                        },
                        {
                            "targets": [9],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                return "<a href='event.jsp?requisition_id=" + data + "' target='_blank'>紀錄</a>";
                            }
                        }
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    "initComplete": function (settings, json) {
                        connectToServer();
                    },
                    "bAutoWidth": false,
                    "displayLength": 50,
                    "lengthChange": true,
                    "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                    "filter": true,
                    "info": true,
                    "paginate": true,
                    "select": true,
                    "searchDelay": 1000,
                    "order": [[0, "desc"]]
                };

                if (${isUser && !isOper}) {
                    var extraSetting = {
                        "dom": 'Bfrtip',
                        "buttons": [
                            {
                                "text": '需求申請',
                                "attr": {
                                    "data-toggle": "modal",
                                    "data-target": "#myModal"
                                },
                                "action": function (e, dt, node, config) {
                                    $("#model-table input").val("");
                                    $("#model-table #id").val(0);
                                }
                            }
//                            {
//                                "text": 'Edit',
//                                "attr": {
//
//                                },
//                                "action": function (e, dt, node, config) {
//                                    var cnt = table.rows('.selected').count();
//                                    if (cnt != 1) {
//                                        alert("Please select a row.");
//                                        return false;
//                                    }
//                                    $('#myModal').modal('show');
//                                    var arr = table.rows('.selected').data();
//                                    var data = arr[0];
//
//                                    $("#model-table #id").val(data.id);
//                                    $("#model-table #po").val(data.po);
//                                    $("#model-table #materialNumber").val(data.materialNumber);
//                                    $("#model-table #amount").val(data.amount);
//                                }
//                            }
                        ]
                    };
                    $.extend(dataTable_config, extraSetting);
                } else if (${isOper}) {
                    var extraSetting2 = {
                        "dom": 'Bfrtip',
                        "buttons": [
                            "csv",
                            {
                                "extend": 'excel',
                                "exportOptions": {
                                    "columns": 'th:not(:first-child):not(:last-child)'
                                }
                            }
                        ]
                    };
                    $.extend(dataTable_config, extraSetting2);
                }

                $('#favourable thead tr').clone(true).appendTo('#favourable thead');
                $('#favourable thead tr:eq(1) th').each(function (i) {
                    var title = $(this).text();
                    $(this).html('<input type="text" id=_header_fitler_' + title + ' placeholder="Search ' + title + '" />');
                    $('input', this).on('keyup change', function () {
                        if (table.column(i).search() !== this.value) {
                            table
                                    .column(i)
                                    .search(this.value)
                                    .draw();
                        }
                    });
                });

//                $("#_header_fitler_日期").addClass("search_disabled").attr("disabled", true);

                var table = $('#favourable').DataTable(dataTable_config);

                $("#datepicker_from, #datepicker_to").datepicker({
                    format: "yyyy-mm-dd",
                    showOn: "button",
                    buttonImage: "images/calendar.gif",
                    buttonImageOnly: false
                });

                $("#search").click(function () {
                    table.ajax.reload();
                });

                $("#clear").click(function () {
                    $(":text, input[type='search']").val("");
                    table.search('').columns().search('').draw();
                });

                $(document).ajaxStart(function () {
                    $("input").not(".search_disabled").attr("disabled", true);
                }).ajaxStop(function () {
                    $("input").not(".search_disabled").removeAttr("disabled");
                });

                $("#save").click(function () {
                    if (confirm("Confirm save?")) {
                        var amount = $("#model-table #amount").val();
                        var po = $("#model-table #po").val(), m = $("#model-table #materialNumber").val();
                        if (isNaN(amount) || amount == "") {
                            alert("Amount please insert a number.");
                            return false;
                        }
                        if (po == "" || m == "") {
                            alert("Po or MaterialNumber can't be empty.");
                            return false;
                        }
                        var data = {
                            id: $("#model-table #id").val(),
                            po: po,
                            materialNumber: m,
                            amount: amount,
                            "returnReason.id": $("#model-table #returnReason\\.id").val(),
                            "user.id": $("#model-table #user\\.id").val(),
                            remark: $("#model-table #remark").val()
                        };
                        save(data);
                    }
                });

                $("body").on("click", ".accept", function () {
                    if (confirm("Confirm action \"Approved\"?")) {
                        var data = table.row($(this).parents('tr')).data();
                        changeStatus(data, 1);
                    }
                });

                $("body").on("click", ".reject", function () {
                    if (confirm("Confirm action \"Reject\"?")) {
                        var data = table.row($(this).parents('tr')).data();
                        changeStatus(data, 2);
                    }
                });

                $("body").on("keyup", "#model-table #po, #model-table #materialNumber", function () {
                    $(this).val($(this).val().trim().toLocaleUpperCase());
                });
                function formatDate(ds) {
//                    console.log(moment(ds));
                    return moment.utc(ds).format('YYYY/MM/DD HH:mm:ss'); // October 22nd 2018, 10:37:08 am
                }

                function save(data) {
                    $.ajax({
                        type: "POST",
                        url: "<c:url value="/RequisitionController/save" />",
                        data: data,
                        success: function (response) {
                            alert(response);
                            $('#myModal').modal('toggle');
                            refreshTable();
                            ws.send("ADD");
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            $("#dialog-msg").val(xhr.responseText);
                        }
                    });
                }

                function changeStatus(data, statusNumber) {
                    $.ajax({
                        type: "POST",
                        url: "<c:url value="/RequisitionController/updateState" />",
                        data: {
                            requisition_id: data.id,
                            "state_id": statusNumber
                        },
                        success: function (response) {
                            alert(response);
                            refreshTable();
                            ws.send("ADD");
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    });
                }

                function refreshTable() {
                    table.ajax.reload();
                }

                //Websocket connect part
                var ws;
                var wsFailMsg = $("#ws-connect-fail-message");
                function connectToServer() {

                    try {
                        ws = new WebSocket("ws://localhost:8080/ExcelReport/myHandler");

                        ws.onopen = function () {
                            wsFailMsg.remove();
                            console.log("Connected");
                        };
                        ws.onmessage = function (event) {
                            var d = event.data;
                            d = d.replace(/\"/g, "");
                            console.log(d);
                            if ("ADD" == d || "REMOVE" == d) {
                                refreshTable();
                                $.notify('資料已更新', {placement: {
                                        from: "bottom",
                                        align: "right"
                                    }
                                });
                            }
                        };
                        ws.onclose = function () {
                            console.log("Disconnected");
                        };
                    } catch (e) {
                        console.log(e);
                    }


                }
                function disconnectToServer() {
                    ws.close();
                }

                if (ws != null) {
                }

            });

        </script>
    </head>
    <body>
        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 id="titleMessage" class="modal-title"></h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <table id="model-table" cellspacing="10" class="table table-bordered">
                                <tr>
                                    <td class="lab">id</td>
                                    <td>
                                        <input type="text" id="id" value="0" disabled="true" readonly>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">工單</td>
                                    <td> 
                                        <input type="text" id="po">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">料號</td>
                                    <td>
                                        <input type="text" id="materialNumber">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">數量</td>
                                    <td>
                                        <input type="number" id="amount">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">備註</td>
                                    <td>
                                        <textarea id="remark"></textarea>
                                    </td>
                                </tr>
                            </table>
                            <div id="dialog-msg" class="alarm"></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" id="save" class="btn btn-default">Save</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>

        <div class="container box">
            <div class="table-responsive">
                <!--<h1 align="center">Requisition details search</h1>-->

                <c:if test="${isLogin}">
                    <h5>
                        Hello, <c:out value="${user.username}" /> /
                        <a href="<c:url value="/logout" />">Logout</a>
                    </h5>
                </c:if>

                <h5 class="text-danger" id="ws-connect-fail-message">※因網頁不支援某些功能無法自動重整, 請手動按右方的Search button重新整理表格</h5>
                <div class="row">
                    <div id="date_filter" class="input-daterange">
                        <div class="col-md-4">
                            <span id="date-label-from" class="date-label">From: </span><input class="date_range_filter date form-control" type="text" id="datepicker_from" />
                        </div>
                        <div class="col-md-4">
                            <span id="date-label-to" class="date-label">To:<input class="date_range_filter date form-control" type="text" id="datepicker_to" />
                        </div>
                        <div class="col-md-4">
                            <input type="button" id="search" class="form-control" value="搜尋">
                            <input type="button" id="clear" class="form-control" value="清除搜尋">
                        </div>
                    </div>
                </div>
                <table class="table table-bordered table-hover" id="favourable">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>工單</th>
                            <th>料號</th>
                            <th>數量</th>
                            <th>狀態</th>
                            <th>申請人</th>
                            <th>起始日期</th>
                            <th>結束日期</th>
                            <th>動作</th>
                            <th>紀錄</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </body>
</html>
