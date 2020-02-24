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
        <title>${initParam.pageTitle} - FQC KanBan</title>
        <style>
            h1{
                color: red;
            }
            .box {
                width:1270px;
                padding:20px;
                background-color:#fff;
                border:1px solid #ccc;
                border-radius:5px;
                margin-top:25px;
            }
            th { font-size: 12px; }
            td { font-size: 11px; }
            .job-close {
                color: grey;
                opacity: 0.8;
            }
            .job-new {
                color: red;
            }
        </style>
        <link rel="stylesheet" href="<c:url value="/libs/bootstrap/bootstrap.css" />" />
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-dt/jquery.dataTables.css" />" />
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-fixedheader-dt/fixedHeader.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-select-dt/select.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-buttons-dt/buttons.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/bootstrap-datepicker/bootstrap-datepicker3.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/font-awesome/font-awesome.min.css" />" />

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
                
                var table;
                var isEditor = ${isOper || isAdmin};
                console.log(isEditor);
                $.fn.dataTable.ext.buttons.reload = {
                    text: 'Reload',
                    action: function (e, dt, node, config) {
                        dt.ajax.reload();
                    }
                };
                var dataTable_config = {
                    "sPaginationType": "full_numbers",
                    "processing": true,
                    "serverSide": true,
                    "fixedHeader": true,
                    "orderCellsTop": true,
                    "dom": 'Bfrtip',
                    buttons: [
                        'reload'
                    ],
                    "ajax": {
                        "url": "<c:url value="/FqcKanBanController/findAll" />",
                        "type": "POST",
                        "data": function (d) {
                        },
                        "dataSrc": function (json) {
                            return JSOG.decode(json.data);
                        }
                    },
                    "columns": [
                        {data: "id", title: "id"},
                        {data: "po", title: "工單"},
                        {data: "modelName", title: "機種"},
                        {data: "inhouseNo", title: "入庫單號"},
                        {data: "inhouseQty", title: "入庫數量"},
                        {data: "createDate", title: "送驗時間"},
                        {data: "updateDate", "defaultContent": "n/a", title: "判定時間"},
                        {data: "qaResult", title: "達成情況"},
                        {data: "memo", "defaultContent": "n/a", title: "memo"},
                        {data: "time", title: "time"},
                        {data: "state", "defaultContent": "n/a", title: "檢驗狀態"},
                        {data: "lastEditor.username", "defaultContent": "n/a", title: "最後修改"},
                        {data: "priority", title: "priority"}
                    ],
                    "columnDefs": [
                        {
                            "targets": [0],
                            "visible": false,
                            "searchable": false
                        },
                        {
                            "targets": [],
                            "visible": isEditor,
                            "searchable": false
                        },
                        {
                            "targets": [5, 6],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                return data == null ? "n/a" : formatDate(data);
                            }
                        },
                        {
                            "targets": [7],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                var startTime = moment(full.createDate).subtract(8, 'hours');
                                var end = moment();
                                var duration = moment.duration(end.diff(startTime));
                                var hours = duration.asHours();
                                return hours >= 4 ? '4hr alert' : "";
                            }
                        },
                        {
                            "targets": [10],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                var d = full.id;
                                return '<input type="radio" id="ck1' + d + '" name="rd_' + d + '" class="state" value=0 ' + (data === 0 ? 'checked="true"' : '') + ' /><label for="ck1' + d + '">檢驗中</label>' +
                                        '<input type="radio" id="ck2' + d + '" name="rd_' + d + '" class="state" value=1 ' + (data === 1 ? 'checked="true"' : '') + ' /><label for="ck2' + d + '">驗畢</label>' +
                                        '<input type="radio" id="ck3' + d + '" name="rd_' + d + '" class="state" value=2 ' + (data === 2 ? 'checked="true"' : '') + ' /><label for="ck3' + d + '">異常</label>';
                            }
                        },
                        {
                            "targets": [12],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                var d = full.id;
                                return '<input type="checkbox" id="ck1_1' + d + '" name="rd1_1_' + d + '" class="priority" value=0 ' + (data === 0 ? 'checked="true"' : '') + ' /><label for="ck1_1' + d + '">置頂</label>';
                            }
                        }
                    ],
                    "createdRow": function (row, data, dataIndex) {
                        var priority = data.priority;
                        var state = data.state;
                        if (priority == 0) {
                            $(row).addClass('text text-danger font-weight-bolder table-warning');
                        }
                        if(state == 2){
                            $(row).addClass('text text-danger font-weight-bolder table-danger');
                        }
                    },
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    "initComplete": function (settings, json) {
                        $("body").on("click", "#favourable .state", function () {
                            if (confirm("Save changes?")) {
                                var d = table.row($(this).parents('tr')).data();
                                d.state = $(this).val();
                                save(d);
                            }
                        });

                        $("body").on("click", "#favourable .priority", function () {
                            if (confirm("Save changes?")) {
                                var d = table.row($(this).parents('tr')).data();
                                d.priority = $(this).is(":checked") ? 0 : 1;
                                save(d);
                            }
                        });
                    },
                    "bAutoWidth": false,
                    "displayLength": -1,
                    "lengthChange": false,
                    "filter": true,
                    "info": true,
                    "paginate": true,
                    "searchDelay": 1000,
                    "order": [[0, "desc"]],
                    "orderFixed": {
                        "pre": [[12, 'asc'], [1, 'asc']]
                    }
                };
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
                table = $('#favourable').DataTable(dataTable_config);
                $(document).ajaxStart(function () {
                    $("input").not(".search_disabled, div.dataTables_filter input").attr("disabled", true);
                }).ajaxStop(function () {
                    $("input").not(".search_disabled").removeAttr("disabled");
                });

                $("body").on("keyup", "#model-table #po, #model-table #materialNumber", function () {
                    $(this).val($(this).val().trim().toLocaleUpperCase());
                });
                $(".hide_col").hide();
                $("#myModal2").find("input, select, textarea").addClass("form-control");

                function formatDate(ds) {
//                    console.log(moment(ds));
                    return moment.utc(ds).format('YYYY/MM/DD HH:mm'); // October 22nd 2018, 10:37:08 am
                }

                function save(data) {
                    $.ajax({
                        type: "POST",
                        url: "<c:url value="/FqcKanBanController/update" />",
                        data: data,
                        success: function (response) {
//                            alert(response);
                            refreshTable();
                            $.notify('資料已更新', {placement: {
                                    from: "bottom",
                                    align: "right"
                                }
                            });
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            console.log(xhr.responseText);
                            $.notify('發生錯誤, 資料未更新', {placement: {
                                    from: "bottom",
                                    align: "right"
                                },
                                type: 'danger'
                            });
                        }
                    });
                }

                function refreshTable() {
                    table.ajax.reload(null, false);
                }

            });

        </script>
    </head>
    <body>
        <div class="container-fluid box">
            <small>
                <div class="table-responsive">
                    <!--<h1 align="center">Requisition details search</h1>-->

                    <c:if test="${isLogin}">
                        <h5>
                            Hello, <c:out value="${user.username}" /> /
                            <a href="<c:url value="/logout" />">登出</a>
                        </h5>
                    </c:if>

                    <table class="table table-bordered table-hover" id="favourable">
                    </table>
                </div>
            </small>
        </div>
        <div>
            <a href="<c:url value="/template.jsp" />"></a>
        </div>
    </body>
</html>
