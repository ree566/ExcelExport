<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
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
        <link rel="stylesheet" href="<c:url value="/libs/bootstrap/css/css/bootstrap.min.css" />" />
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-dt/css/jquery.dataTables.css" />" />
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-fixedheader-dt/css/fixedHeader.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-select-dt/css/select.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/bootstrap-datepicker/css/bootstrap-datepicker3.css" />"/>

        <script src="<c:url value="/libs/jQuery/jquery.js" />"></script>
        <script src="<c:url value="/libs/bootstrap/js/js/bootstrap.min.js" />"></script>
        <script src="<c:url value="/libs/datatables.net/js/jquery.dataTables.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-fixedheader/js/dataTables.fixedHeader.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-select/js/dataTables.select.js" />"></script>
        <script src="<c:url value="/libs/bootstrap-datepicker/js/bootstrap-datepicker.js" />"></script>
        <script src="<c:url value="extraJs/jquery.spring-friendly.js" />"></script>
        <script src="<c:url value="/libs/moment/js/moment.js" />"></script>
        <script src="<c:url value="/libs/jsog/js/JSOG.js" />"></script>
        <script>
            $(function () {

                var dataTable_config = {
                    "processing": true,
                    "serverSide": true,
                    "fixedHeader": true,
                    "orderCellsTop": true,
                    "ajax": {
                        "url": "ScrappedDetailController/findAll",
                        "type": "POST",
                        data: function (d) {
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
                        {data: "modelName"},
                        {data: "materialNumber"},
                        {data: "amount"},
                        {data: "reason"},
                        {data: "kind"},
                        {data: "price"},
                        {data: "negligenceUser"},
                        {data: "remark"},
                        {data: "createDate"},
                        {data: "floor.name"}
                    ],
                    "columnDefs": [
                        {
                            "targets": [0],
                            "visible": false,
                            "searchable": false
                        },
                        {
                            "targets": 10,
                            "searchable": false,
                            'render': function (data, type, full, meta) {
                                return data == null ? "n/a" : 　formatDate(data);
                            }
                        },
                        {
                            "targets": 11,
                            'render': function (data, type, full, meta) {
                                return data == null ? "n/a" : data;
                            }
                        }
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
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

                $("#_header_fitler_日期").addClass("search_disabled").attr("disabled", true);

                var table = $('#favourable').DataTable(dataTable_config);

                $("#datepicker_from, #datepicker_to").datepicker({
                    format: "yyyy-mm-dd",
                    showOn: "button",
                    buttonImage: "images/calendar.gif",
                    buttonImageOnly: false
                }).on('change', function () {
                    $('.datepicker').hide();
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


                function formatDate(ds) {
                    return moment(ds).format('YYYY/MM/DD'); // October 22nd 2018, 10:37:08 am
                }

            });

        </script>
    </head>
    <body>
        <div class="container box">
            <div class="table-responsive">
                <h1 align="center">Scrapped details search</h1>
                <div class="row">
                    <div id="date_filter" class="input-daterange">
                        <div class="col-md-4">
                            <span id="date-label-from" class="date-label">From: </span><input class="date_range_filter date form-control" type="text" id="datepicker_from" />
                        </div>
                        <div class="col-md-4">
                            <span id="date-label-to" class="date-label">To:<input class="date_range_filter date form-control" type="text" id="datepicker_to" />
                        </div>
                        <div class="col-md-4">
                            <input type="button" id="search" class="form-control" value="Search">
                            <input type="button" id="clear" class="form-control" value="Clear search filter">
                        </div>
                    </div>
                </div>
                <table class="table table-bordered table-hover" id="favourable">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>工單</th>
                            <th>機種</th>
                            <th>料號</th>
                            <th>數量</th>
                            <th>退料原因</th>
                            <th>類別</th>
                            <th>單價</th>
                            <th>疏失人員</th>
                            <th>備註</th>
                            <th>日期</th>
                            <th>樓層</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </body>
</html>
