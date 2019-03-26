<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authentication var="user" property="principal" />
<sec:authorize access="isAuthenticated()"  var="isLogin" />
<c:if test="${!isLogin || param.requisition_id == null}">
    <c:redirect url="/" />
</c:if>

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
        <link rel="stylesheet" href="<c:url value="/libs/datatables.net-buttons-dt/css/buttons.dataTables.css" />"/>
        <link rel="stylesheet" href="<c:url value="/libs/bootstrap-datepicker/css/bootstrap-datepicker3.css" />"/>

        <script src="<c:url value="/libs/jquery/js/jquery.js" />"></script>
        <script src="<c:url value="/libs/bootstrap/js/js/bootstrap.min.js" />"></script>
        <script src="<c:url value="/libs/datatables.net/js/jquery.dataTables.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-fixedheader/js/dataTables.fixedHeader.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-select/js/dataTables.select.js" />"></script>
        <script src="<c:url value="/libs/datatables.net-buttons/js/dataTables.buttons.js" />"></script>
        <script src="<c:url value="/libs/bootstrap-datepicker/js/bootstrap-datepicker.js" />"></script>
        <script src="<c:url value="/extraJs/jquery.spring-friendly.js" />"></script>
        <script src="<c:url value="/libs/moment/js/moment.js" />"></script>
        <script src="<c:url value="/libs/jsog/js/JSOG.js" />"></script>
        <script>
            $(function () {
                var requisition_id = '${param.requisition_id}';

                var dataTable_config = {
                    "processing": true,
                    "serverSide": true,
                    "fixedHeader": true,
                    "orderCellsTop": true,
                    "ajax": {
                        "url": "<c:url value="/RequisitionController/findEvent" />",
                        "type": "POST",
                        "data": {
                            requisition_id: requisition_id
                        },
                        "dataSrc": function (json) {
                            return JSOG.decode(json.data);
                        }
                    },
                    "columns": [
                        {data: "id"},
                        {data: "user.username"},
                        {data: "requisitionState.name"},
                        {data: "modifiedDate"},
                        {data: "remark"}
                    ],
                    "columnDefs": [
                        {
                            "targets": [0],
                            "visible": true
                        },
                        {
                            "targets": [3],
                            'render': function (data, type, full, meta) {
                                return data == null ? "n/a" : formatDate(data);
                            }
                        },
                        {
                            "targets": [4],
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
                    "displayLength": 50,
                    "lengthChange": false,
                    "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                    "filter": false,
                    "info": false,
                    "paginate": false,
                    "order": [[0, "asc"]]
                };

                var table = $('#favourable').DataTable(dataTable_config);

                $(document).ajaxStart(function () {
                    $("input").not(".search_disabled").attr("disabled", true);
                }).ajaxStop(function () {
                    $("input").not(".search_disabled").removeAttr("disabled");
                });

                function formatDate(ds) {
                    console.log(moment(ds));
                    return moment(ds).format('YYYY/MM/DD'); // October 22nd 2018, 10:37:08 am
                }

            });

        </script>
    </head>
    <body>
        <div class="container box">
            <div class="table-responsive">
                <table class="table table-bordered table-hover" id="favourable">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>人員</th>
                            <th>申請狀態</th>
                            <th>異動日期</th>
                            <th>備註</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </body>
</html>
