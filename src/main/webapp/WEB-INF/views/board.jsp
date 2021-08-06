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
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src='<c:url value="/scripts/common.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/scripts/board.js"/>'></script>

    <title>Cars::Board</title>
</head>
<body>
    <nav class="uk-navbar-container" uk-navbar>
        <div class="uk-navbar-left">
            <div class="uk-navbar-item"> <!-- Отдельный контейнер -->
                <h4>СПИСОК ОБЪЯВЛЕНИЙ</h4>
            </div>
            <ul class="uk-navbar-nav">
                <li>
                    <a href="#">Фильтр объявлений</a>
                    <div class="uk-navbar-dropdown">
                        <ul class="uk-nav uk-navbar-dropdown-nav">
                            <li class="uk-nav-header">Среди всех:</li>
                            <li>
                                <a id="search-lastday" href="#" onclick="setModeLastDay()">Только за последние сутки</a>
                            </li>
                            <li class="uk-nav-divider"></li>
                            <li>
                                <a id="search-onlyphoto" href="#" onclick="setModeOnlyPhoto()">Только объявления с фото</a>
                            </li>
                            <li class="uk-nav-divider"></li>
                            <li>
                                <form>
                                    <label class="uk-form-label" for="search-brand">Только с заданной маркой</label>
                                    <div class="uk-form-controls">
                                        <input id="search-brand" class="uk-input uk-form-blank uk-form-width-medium" type="text" placeholder="Введите марку">
                                    </div>
                                </form>
                            </li>
                            <li class="uk-nav-divider"></li>
                            <li>
                                <a id="search-all" href="#" onclick="setModeAll()">Сбросить настройки</a>
                            </li>
                        </ul>
                        <div id="search-mode" hidden>all</div>
                        <div id="search-value" hidden></div>
                    </div>
                </li>
                <div id="filter-mode" class="uk-navbar-item">
                </div>
                <li>
                    <a href="adcard.do">Создать объявление</a>
                </li>
            </ul>
        </div>
        <div class="uk-navbar-right">
            <div id="currentUser" hidden><c:out value="${sessionScope['user'].name}"/></div>
            <div id="currentUserId" hidden><c:out value="${sessionScope['user'].id}"/></div>
            <ul id="loginPlace" class="uk-navbar-nav">
            </ul>
        </div>
    </nav>

    <div uk-grid>
        <div class="uk-width-expand">
        </div>
        <div class="uk-width-4-5">
            <div id="adPlace" class="uk-container-expand">
            </div>
        </div>
        <div class="uk-width-expand">
        </div>
    </div>
</body>
</html>