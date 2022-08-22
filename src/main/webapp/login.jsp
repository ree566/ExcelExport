<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value=""/>

<!DOCTYPE html>
<html>
    <head>
        <style>
            .error{
                color:red;
            }
            td{
                padding: 5px;
            }
        </style>
        <script src="<c:url value="/libs/jQuery/jquery.js" />"></script>
        <script>
            $(function () {
                $('input').on("keypress", function (e) {
                    /* ENTER PRESSED*/
                    if (e.keyCode == 13) {
                        /* FOCUS ELEMENT */
                        var inputs = $(this).parents("form").eq(0).find(":input");
                        var idx = inputs.index(this);

                        if (idx == inputs.length - 1) {
                            inputs[0].select();
                        } else {
                            inputs[idx + 1].focus(); //  handles submit buttons
                            inputs[idx + 1].select();
                        }
                        return false;
                    }
                });
            });
        </script>
    </head>
    <body>
        <h1>Login</h1>
        <c:if test="${param.error != null}">
            <h5 class="error">Your login attempt was not successful, try again.</h5>
            <h5 class="error">Reason: Bad credentials</h5>
        </c:if>
        <form name='f' action="login" method='POST'>
            <table>
                <tr>
                    <td>User:</td>
                    <td><input type='text' name='username' value=''></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type='password' name='password' /></td>
                </tr>
                <tr>
                    <td colspan="2"><input name="submit" type="submit" value="submit" /></td>
                </tr>
            </table>
        </form>
    </body>
</html>