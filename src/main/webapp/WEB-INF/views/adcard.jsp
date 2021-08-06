<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <script type="text/javascript" src='<c:url value="/scripts/adcard.js"/>' defer></script>
    <script type="text/javascript" src='<c:url value="/scripts/upload.js"/>' defer></script>

    <title>Cars::Ad</title>
</head>
<body>
<nav class="uk-navbar-container" uk-navbar="dropbar: true; dropbar-mode: push">
    <div class="uk-navbar-left">
        <div class="uk-navbar-item">
            <h4>РЕДАКТИРОВАНИЕ ОБЪЯВЛЕНИЯ</h4>
        </div>
    </div>
    <div class="uk-navbar-right">
        <div id="currentUser" hidden><c:out value="${sessionScope['user'].name}"/></div>
        <c:if test="${requestScope['edit'] == 'true'}" var="isEdit"/>
        <ul id="loginPlace" class="uk-navbar-nav">
        </ul>
    </div>
</nav>

<div uk-grid>
    <div class="uk-width-expand">
    </div>
    <div class="uk-width-2-5">
        <div class="uk-container-expand">
            <div class="uk-card uk-card-default uk-margin-small-top">
                <div id="photoPlace">
                </div>
                <div class="uk-card-body">
                    <div class="uk-float-left">
                        <h3 class="uk-card-title">Фотографии для объявления</h3>
                        <p>Загруженные для автомобиля фотографии</p>
                    </div>
                    <div class="js-upload uk-float-right" uk-form-custom>
                        <input type="file" multiple>
                        <button class="uk-button uk-button-default" type="button" tabindex="-1">Добавить еще фото</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="uk-width-2-5">
        <div class="uk-container-expand">
            <div class="uk-card uk-card-default uk-margin-small-top">
                <div class="uk-card-body">
                    <form class="form uk-margin uk-grid-small" action="" method="POST" uk-grid>
                        <legend class="uk-legend uk-text-center">Заполните объявление: </legend>
                        <input id="adId" type="hidden" name="adId" value="<c:out value="${requestScope['advert'].id}"/>">
                        <div class="uk-margin uk-width-1-1">
                            <input class="uk-input" name="summary" type="text" placeholder="Заголовок объявления" minlength="15" maxlength="80"
                            <c:if test="${isEdit}">value="${requestScope['advert'].summary}"</c:if> required>
                        </div>
                        <div class="uk-margin uk-width-1-1">
                            <textarea class="uk-textarea" name="description" placeholder="Текст объявления" rows="3"
                                    required><c:if test="${isEdit}">${requestScope['advert'].description}</c:if></textarea>
                        </div>
                        <div class="uk-margin uk-width-1-2">
                            <input class="uk-input" name="brand" type="text" placeholder="Марка" minlength="2" maxlength="16"
                                   <c:if test="${isEdit}">value="${requestScope['advert'].car.brand}"</c:if> required>
                        </div>
                        <div class="uk-margin uk-width-1-2">
                            <input class="uk-input" name="model" type="text" placeholder="Модель"
                                   <c:if test="${isEdit}">value="${requestScope['advert'].car.model}"</c:if> required>
                        </div>
                        <div class="uk-margin uk-width-1-2">
                            <input class="uk-input" name="body" type="text" placeholder="Кузов"
                                   <c:if test="${isEdit}">value="${requestScope['advert'].car.body}"</c:if> required>
                        </div>
                        <div class="uk-margin uk-width-1-2">
                            <input class="uk-input" name="price" type="number" placeholder="Цена"
                                   <c:if test="${isEdit}">value="${requestScope['advert'].price}"</c:if>required>
                        </div>
                        <div class="uk-margin uk-width-1-1">
                            <label class="uk-form-label">Год выпуска</label>
                            <input class="uk-input" name="produced" type="date" placeholder="Год выпуска"
                                   <c:if test="${isEdit}">
                                        <c:set var="producedTimestamp" value="${requestScope['advert'].car.produced}" />
                                        <fmt:formatDate value="${producedTimestamp}" var="parsed" pattern="yyyy-MM-dd"/>
                                        value="${parsed}"
                                   </c:if>required>
                        </div>
                        <div class="uk-margin">
                            <input class="uk-checkbox" type="checkbox" name="published"
                                   <c:if test="${isEdit}">checked</c:if>> Опубликовать изменения
                        </div>
                        <div class="uk-margin">
                            <input class="uk-checkbox" type="checkbox" name="closed"> Закрыть объявление
                        </div>
                        <div class="uk-margin uk-width-1-1 uk-text-center">
                            <button id="submit_advert" type="submit" class="uk-button uk-button-default">Отправить</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="uk-width-expand">
    </div>
</div>
</body>
</html>