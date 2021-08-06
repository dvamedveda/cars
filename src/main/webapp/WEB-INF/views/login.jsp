<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- UIkit CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/uikit@3.7.0/dist/css/uikit.min.css" />
    <!-- UIkit JS -->
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.7.0/dist/js/uikit.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.7.0/dist/js/uikit-icons.min.js"></script>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
    integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
    crossorigin="anonymous"></script>

    <title>Cars::Login</title>
</head>
<body>
    <div uk-grid>
        <div class="uk-width-1-3">
        </div>
        <div class="uk-width-1-3">
            <div uk-grid>
                <div class="uk-card uk-card-default uk-card-body uk-position-top-center uk-margin-medium-top">
                    <div class="uk-card-title">Авторизация</div>
                    <c:if test="${requestScope.error != null}">
                        <div class="uk-alert-danger" uk-alert>
                            <c:out value="${requestScope.error}"/>
                        </div>
                    </c:if>
                    <c:if test="${requestScope.info != null}">
                        <div class="uk-alert-success" uk-alert>
                            <c:out value="${requestScope.info}"/>
                        </div>
                    </c:if>
                    <form class="uk-form-stacked" action="${pageContext.request.contextPath}/auth.do" method="post">
                        <div>
                            <label class="uk-form-label">Почта</label>
                            <div class="uk-form-controls">
                                <input class="uk-form-width-medium uk-form-medium" type="email" name="email" required>
                            </div>
                        </div>
                        <div>
                            <label class="uk-form-label">Пароль</label>
                            <div class="uk-form-controls">
                                <input class="uk-form-width-medium uk-form-medium" type="password" name="password" required>
                            </div>
                        </div>
                        <div class="uk-margin">
                            <button type="submit" class="uk-button uk-button-small uk-button-primary">Войти</button>
                            <a href="${pageContext.request.contextPath}/register.do" class="uk-button uk-button-small uk-button-default">Регистрация</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="uk-width-1-3">
        </div>
    </div>
</body>
</html>