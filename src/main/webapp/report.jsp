<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            h1{
                color: red;
            }
        </style>
        <link rel="stylesheet" href="bower_components/bootstrap/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="bower_components/datatables.net-dt/css/jquery.dataTables.min.css" />
        <script src="bower_components/jquery/dist/jquery.min.js"></script>
        <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
        <script>
            $(function () {
                var dataTable_config = {
                    "processing": true,
                    "serverSide": false,
                    "ajax": {
                        "url": "ScrappedDetailController/findAll",
                        "type": "GET",
                        data: {
                            id: 3967
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
                        {data: "createDate"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: false,
                    displayLength: -1,
                    lengthChange: false,
                    filter: false,
                    info: true,
                    paginate: false,
                    destroy: true,
                    "order": [[0, "asc"]]
                };

                $('#favourable').DataTable(dataTable_config);


            });
        </script>
    </head>
    <body>
        <table class="table table-bordered table-hover" id="favourable">
            <thead>
                <tr>
                    <th>
                        id
                    </th>
                    <th>
                        po
                    </th>
                    <th>
                        modelName
                    </th>
                    <th>
                        materialNumber
                    </th>
                    <th>
                        amount
                    </th>
                    <th>
                        reason
                    </th>
                    <th>
                        kind
                    </th>
                    <th>
                        price
                    </th>
                    <th>
                        negligenceUser
                    </th>
                    <th>
                        remark
                    </th>
                    <th>
                        createDate
                    </th>
                </tr>
            </thead>
        </table>
    </body>
</html>
