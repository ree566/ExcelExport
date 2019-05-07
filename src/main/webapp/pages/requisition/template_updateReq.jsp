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
            th { font-size: 12px; }
            td { font-size: 11px; }
            .job-close {
                color: grey;
                opacity: 0.8;
            }
            .job-new {
                color: red;
            }
            .tooltip {
                opacity: 1;
            }
            input[readonly]{
                background-color:transparent;
                border: 0;
                font-size: 1em;
            }
        </style>
        <link rel="stylesheet" href="<c:url value="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" />" />
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-dt/jquery.dataTables.css" />" />
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-fixedheader-dt/fixedHeader.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-select-dt/select.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-buttons-dt/buttons.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/bootstrap-datepicker/bootstrap-datepicker3.css" />"/>

        <script src="<c:url value="/libs/jQuery/jquery.js" />"></script>
        <script src="<c:url value="https://unpkg.com/popper.js@1.15.0/dist/umd/popper.min.js" />"></script>
        <script src="<c:url value="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" />"></script>
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
                var isEditor = ${isOper || isAdmin};

                initDropDownOptions();

                var modelTooltipObj = $(".tooltip-wiget");

                $(".tooltip-wiget").attr({
                    "data-toggle": "tooltip",
                    "container": "body"
                });

                modelTooltipObj.tooltip();

                $("#po").val("PC123456");
                $("#materialNumber").val("965811152");
                $("#amount").val(3);
                $("#requisitionReason\\.id").val(2);
                $("#requisitionState\\.id").val(1);
                $("#requisitionType\\.id").val(1);

                $("#materialType").val(2);
                $("#remark").val("test");
                $("#receiveDate").val("2019-02-01");
                $("#returnDate").val("2019-04-03");

                var dataTable_config = {
                    "sPaginationType": "full_numbers",
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
                        {data: "id", title: "id"},
                        {data: "po", title: "工單"},
                        {data: "materialNumber", title: "料號"},
                        {data: "amount", title: "數量"},
                        {data: "amount", title: "SAP數量餘"},
                        {data: "amount", title: "儲區位置"},
                        {data: "requisitionReason.name", "defaultContent": "n/a", title: "原因"},
                        {data: "user.username", "defaultContent": "n/a", title: "申請人"},
                        {data: "requisitionState.name", "defaultContent": "n/a", title: "申請狀態"},
                        {data: "createDate", title: "申請日期"},
                        {data: "receiveDate", title: "領料日期"},
                        {data: "returnDate", title: "退料日期"},
                        {data: "requisitionType.name", "defaultContent": "n/a", title: "料件狀態"},
                        {data: "materialType", "defaultContent": "n/a", title: "分類"},
                        {data: "remark", "defaultContent": "n/a", title: "備註"},
                        {data: "id", "defaultContent": "n/a", title: "紀錄", "tooltip": "顯示申請狀態異動紀錄"}
                    ],
                    "headerCallback": function (nHead, aData, iStart, iEnd, aiDisplay) {
                        table.columns().iterator('column', function (settings, column) {
                            if (settings.aoColumns[ column ].tooltip !== undefined) {
                                $(table.column(column).header()).attr('title', settings.aoColumns[ column ].tooltip);
                            }
                        });
                    },
                    "columnDefs": [
                        {
                            "targets": [0, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14],
                            "visible": false,
                            "searchable": false
                        },
                        {
                            "targets": [2],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                return "<a href='#'>詳細</a>";
                            }
                        },
                        {
                            "targets": [4, 5],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                return "---";
                            }
                        },
                        {
                            "targets": [13, 14],
                            "visible": isEditor,
                            "searchable": false
                        },
                        {
                            "targets": [9, 10, 11],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                return data == null ? "n/a" : formatDate(data);
                            }
                        },
                        {
                            "targets": [15],
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                return "<a href='event.jsp?requisition_id=" + data + "' target='_blank'>紀錄</a>";
                            }
                        }
                    ],
                    "createdRow": function (row, data, dataIndex) {
                        var state = data.requisitionState.id;
                        $(row).addClass('text text-' + (state == 7 || state == 2 ? 'muted' : (state == 3 ? 'danger' : 'primary')));
                    },
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    "initComplete": function (settings, json) {
                        $('thead th[title]').addClass("tooltip-wiget").attr({
                            "data-toggle": "tooltip",
                            "data-placement": "right",
                            "data-container": "body"
                        });
                    },
                    "bAutoWidth": false,
                    "displayLength": 10,
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
                            'pageLength',
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
                        ]
                    };
                    $.extend(dataTable_config, extraSetting);
                    $("#model-table").find("input, select").attr({
                        "disabled": true,
                        "readonly": "readonly"
                    });
                } else if (${isOper}) {
                    var successHandler = function (response) {
                        console.log("updated");
                        refreshTable();
                        $.notify('資料已更新', {placement: {
                                from: "bottom",
                                align: "right"
                            }
                        });
                    };
                    var errorHandler = function (xhr, ajaxOptions, thrownError) {
                        $("#dialog-msg").val(xhr.responseText);
                    };

                    var extraSetting2 = {
                        "dom": 'Bfrtip',
                        "buttons": [
                            'pageLength',
                            {
                                "text": '新增需求',
                                "attr": {
                                    "data-toggle": "modal",
                                    "data-target": "#myModal"
                                },
                                "action": function (e, dt, node, config) {
                                    $("#model-table input").val("");
                                    $("#model-table #id").val(0);
                                }
                            },
                            {
                                "text": '編輯',
                                "attr": {
                                    "data-toggle": "modal"
//                                    "data-target": "#myModal2"
                                },
                                "action": function (e, dt, node, config) {
                                    var cnt = table.rows('.selected').count();
                                    if (cnt != 1) {
                                        alert("Please select a row.");
                                        return false;
                                    }

                                    $('#myModal2').modal('show');
                                    var arr = table.rows('.selected').data();
                                    var data = arr[0];

                                }
                            },
                            {
                                "extend": 'excel',
                                "exportOptions": {
                                    "columns": 'th:not(:first-child):not(:last-child)',
                                    modifier: {
                                        // DataTables core
                                        search: 'applied',
                                        order: 'applied'
                                    }
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

                $(".hide_col").hide();

                $(".show-dialog-tooltip").click(function () {
                    modelTooltipObj.tooltip('toggle');
                });

                $(".hideAll-dialog-tooltip").click(function () {
                    modelTooltipObj.tooltip('hide');
                });

                $("#low-amount-alarm").click(function () {
                    alert("通知物管成功");
                });

                var materialDetail = $("#material-detail").find("tbody>tr").eq(1);
                $("#add-material").click(function () {
                    var clone = materialDetail.clone();
                    materialDetail.after(clone);
                });
                
                $("#save").click(function () {
                    alert("資料已儲存");
                    window.close();
                });

                $("#close").click(function () {
                    window.close();
                });

//                $("input, select").addClass("form-control input-sm");

                function formatDate(ds) {
//                    console.log(moment(ds));
                    return moment.utc(ds).format('YY/MM/DD HH:mm'); // October 22nd 2018, 10:37:08 am
                }

                function initDropDownOptions() {
                    $.ajax({
                        type: "GET",
                        url: "<c:url value="/RequisitionController/findRequisitionReasonOptions" />",
                        success: function (response) {
                            var sel = $("#model-table #requisitionReason\\.id");
                            var sel2 = $("#model-table2 #requisitionReason\\.id");
                            var d = response;
                            for (var i = 0; i < d.length; i++) {
                                var options = d[i];
                                sel.append("<option value='" + options.id + "'>" + options.name + "</option>");
                                sel2.append("<option value='" + options.id + "'>" + options.name + "</option>");
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    });
                    $.ajax({
                        type: "GET",
                        url: "<c:url value="/RequisitionController/findRequisitionStateOptions" />",
                        success: function (response) {
                            var sel = $("#model-table #requisitionState\\.id");
                            var sel2 = $("#model-table2 #requisitionState\\.id");
                            var d = response;
                            for (var i = 0; i < d.length; i++) {
                                var options = d[i];
                                sel.append("<option value='" + options.id + "'>" + options.name + "</option>");
                                sel2.append("<option value='" + options.id + "'>" + options.name + "</option>");
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    });
                    $.ajax({
                        type: "GET",
                        url: "<c:url value="/RequisitionController/findRequisitionTypeOptions" />",
                        success: function (response) {
                            var sel = $("#model-table #requisitionType\\.id");
                            var sel2 = $("#model-table2 #requisitionType\\.id");
                            var d = response;
                            for (var i = 0; i < d.length; i++) {
                                var options = d[i];
                                sel.append("<option value='" + options.id + "'>" + options.name + "</option>");
                                sel2.append("<option value='" + options.id + "'>" + options.name + "</option>");
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    });
                }

            });

        </script>
    </head>
    <body>

        <div class="container p-3">
            <table id="model-table" cellspacing="10" class="table table-bordered">
                <tr class="hide_col">
                    <td class="lab">id</td>
                    <td>
                        <input type="text" id="id" value="0" disabled="true" readonly>
                    </td>
                </tr>
                <tr>
                    <td class="lab">工單</td>
                    <td> 
                        <input type="text" id="po" placeholder="工單" />
                    </td>
                </tr>
                <tr>
                    <td class="lab">詳細</td>
                    <td id="materials">
                        <div class="material-detail">
                            <table id="material-detail" class="table table-bordered table-sm">
                                <thead>
                                    <tr>
                                        <th>料號</th>
                                        <th>數量</th>
                                        <th>庫存</th>
                                        <th>儲位</th>
                                        <th>原因</th>
                                        <th>申請狀態</th>
                                        <th>料號狀態</th>
                                        <th>分類</th>
                                        <th>備註</th>
                                        <th>領料日</th>
                                        <th>退料日</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <input type="text" id="materialNumber" class="tooltip-wiget" placeholder="料號" title="key in料號與SAP工單料號比對是否相符" data-placement="top"/>
                                        </td>
                                        <td>
                                            <input type="number" id="amount"  class="tooltip-wiget" placeholder="數量" title="需求數與庫存數不足時,將缺少數量自動連結掛缺料平台" data-placement="bottom">
                                        </td>
                                        <td>
                                            <h5 class="tooltip-wiget remain-msg" title="顯示SAP庫存數量" data-placement="bottom">N</h5>
                                        </td>

                                        <td>
                                            <input type="text" class="tooltip-wiget" title="key in 料號時自動帶出SAP中設定的儲位" readonly="true" placeholder="readonly" data-placement="top">
                                        </td>
                                        <td>
                                            <select id="requisitionReason.id"></select>
                                        </td>
                                        <td>
                                            <select id="requisitionState.id"></select>
                                        </td>
                                        <td>
                                            <select id="requisitionType.id"></select>
                                        </td>
                                        <td>
                                            <input type="text" id="materialType" class="tooltip-wiget" placeholder="料件分類" title="管理者維護料件分類(鐵件類...etc)">
                                        </td>

                                        <td>
                                            <input type="text" id="remark" placeholder="備註" />
                                        </td>
                                        <td>
                                            <input type="text" id="receiveDate" readonly>
                                        </td>
                                        <td>
                                            <input type="text" id="returnDate" readonly>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="text" id="materialNumber" placeholder="料號" />
                                        </td>
                                        <td>
                                            <input type="number" id="amount" placeholder="數量">
                                        </td>
                                        <td>
                                            <h5 class="tooltip-wiget remain-msg">N</h5>
                                        </td>

                                        <td>
                                            <input type="text" readonly="true"  placeholder="readonly">
                                        </td>
                                        <td>
                                            <select id="requisitionReason.id"></select>
                                        </td>
                                        <td>
                                            <select id="requisitionState.id"></select>
                                        </td>
                                        <td>
                                            <select id="requisitionType.id"></select>
                                        </td>
                                        <td>
                                            <input type="text" id="materialType" placeholder="料件分類"  />
                                        </td>
                                        <td>
                                            <input type="text" id="remark" placeholder="備註"  />
                                        </td>
                                        <td>
                                            <input type="text" id="receiveDate" readonly>
                                        </td>
                                        <td>
                                            <input type="text" id="returnDate" readonly>
                                        </td>

                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>


            </table>
            <div id="dialog-msg" class="alarm"></div>
        </div>
    </div>
    <div class="modal-footer">
        <a class="show-dialog-tooltip" href="#">open hint</a>
        <a class="hideAll-dialog-tooltip" href="#">hide all hint</a>
        <button type="button" id="save" class="btn btn-default tooltip-wiget" title="儲存後展開成excel格式呈現在Table中">Save</button>
        <button type="button" id="close" class="btn btn-default" data-dismiss="modal">Close</button>
    </div>

</body>
</html>
