/**
 * Константа, определяющая период
 * автоматического обновления страницы приложения.
 * @type {number}
 */
const REFRESH_DELAY = 60000;

$(document).ready(showAds);
$(document).ready(autoRefresh);
$(document).ready(function () {
    setFilteringView("все объявления");
});

/**
 * Функция с рекурсивным setTimeout
 * для обновления списка объявлений.
 */
function refresh() {
    showAds();
    setTimeout(refresh, REFRESH_DELAY);
}

/**
 * Запуск автоматического обновления списка объявлений.
 */
function autoRefresh() {
    setTimeout(refresh, REFRESH_DELAY);
}

/**
 * Функция, загружающая на страницу список объявлений.
 */
function showAds() {
    $("#adPlace").empty();
    let mode = $("#search-mode").text();
    let searchMode = mode === "all" ? "" : (mode === "lastday" ? "lastday" : (mode === "onlyphoto" ? "onlyphoto" : "bybrand"));
    let searchParameter = searchMode === "" ? "" : searchMode === "lastday" ? "true" : (searchMode === "onlyphoto" ? "true" : $("#search-value").text());
    let data = {};
    data[searchMode] = searchParameter;
    $.ajax({
        type: 'GET',
        url: 'search.do',
        data: data,
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8'
    }).done(function (adData) {
        let adResponse = JSON.parse(JSON.stringify(adData));
        let userId = $("#currentUserId").text();
        if (userId !== "") {
            $.ajax({
                type: 'GET',
                url: 'search.do',
                data: {
                    userId: userId
                },
                dataType: 'json'
            }).done(function (userData) {
                let currentUserAdverts = JSON.parse(JSON.stringify(userData));
                let currentUserAdvertIds = [];
                if (currentUserAdverts.length > 0 && currentUserAdverts[0] !== null) {
                    for (let userAdvert of currentUserAdverts) {
                        currentUserAdvertIds.push(userAdvert.id)
                    }
                }
                for (let advert of adResponse) {
                    addAdvert(advert);
                    if (currentUserAdvertIds.includes(advert.id)) {
                        let editForm = prepareEditForm(advert);
                        $("#edit-form-advert" + advert.id).append(editForm);
                    }
                }
            }).fail(function (error) {
                console.log("Что-то пошло не так! Запрос не выполнился!")
            });
        } else {
            for (let advert of adResponse) {
                addAdvert(advert);
            }
        }
    }).fail(function (error) {
        console.log("Что-то пошло не так! Запрос не выполнился!")
    })
}

/**
 * Функция, добавляющая объявление в список на странице.
 * @param advert объект объявления.
 */
function addAdvert(advert) {
    if (advert.photos.length === 0) {
        $("#adPlace").append(addAdvertWithoutPhoto(advert));
        let fragment = `
                    <li>
                        <img src="https://www.seat.com/content/dam/public/seat-website/carworlds/compare/default-image/ghost.png" alt="" uk-cover>
                    </li>
                `;
        $("#modal-" + advert.id).append(fragment);
    } else {
        $("#adPlace").append(addAdvertWithPhoto(advert));
        for (photo of advert.photos) {
            $("#" + advert.id).append(addPhotoFragment(photo));
            $("#modal-" + advert.id).append(addPhotoFragment(photo));
        }
    }
}

/**
 * Подготовка фрагмента HTML с объявлением без фотографий.
 * @param advert объект объявления без фотографий.
 * @returns {string} фрагмент HTML для добавления на страницу.
 */
function addAdvertWithoutPhoto(advert) {
    let description = prepareDescription(advert);
    let fragment = `
    <div class="uk-card uk-card-default uk-grid-collapse uk-margin-small-top uk-card-small uk-padding-small" uk-grid>
        <div class="uk-card-badge uk-label uk-padding-small uk-padding-remove-vertical uk-margin-right">
                ${formatDate(advert.createdOn)}
        </div>
        <div class="uk-width-1-5 uk-card-media-left uk-cover-container">
            <img src="https://www.seat.com/content/dam/public/seat-website/carworlds/compare/default-image/ghost.png" alt="" uk-cover="height:180; width: 270">
        </div>
        ${description}
    </div>
    `;
    return fragment;
}

/**
 * Подготовка фрагмента HTML с объявлением с фотографиями.
 * @param advert объект объявления с фотографиями.
 * @returns {string} фрагмент HTML для добавления на страницу.
 */
function addAdvertWithPhoto(advert) {
    let description = prepareDescription(advert);
    let fragment = `
    <div class="uk-card uk-card-default uk-grid-collapse uk-margin-small-top uk-card-small uk-padding-small" uk-grid>
        <div class="uk-card-badge uk-label uk-padding-small uk-padding-remove-vertical uk-margin-right">
                ${formatDate(advert.createdOn)}
        </div>
        <div uk-slideshow="animation: push; min-height: 180; max-height: 180; autoplay: true; autoplay-interval: 5000" class="uk-width-1-5">
            <div class="uk-position-relative uk-visible-toggle uk-light">
                <ul id="${advert.id}" class="uk-slideshow-items">
                </ul>
                <a class="uk-position-center-left uk-position-small" href="#" uk-slidenav-previous uk-slideshow-item="previous"></a>
                <a class="uk-position-center-right uk-position-small" href="#" uk-slidenav-next uk-slideshow-item="next"></a>
            </div>
        </div>
        ${description}
    </div>
    `;
    return fragment;
}

/**
 * Подготовка фрагмента HTML с текстом объявления.
 * @param advert объект объявления.
 * @returns {string} фрагмент HTML, содержащий текст объявления.
 */
function prepareDescription(advert) {
    let cardBody = `
    <div class="uk-width-expand">
        <div class="uk-card-body uk-margin-small-top uk-padding-remove-right uk-padding-remove-bottom">
            <div uk-grid>
                <div class="uk-width-1-1">
                    <h3 class="uk-card-title">${advert.summary}</h3>
                </div>
                <div class="uk-width-1-4 uk-margin-large-top">
                     <h4>Цена: ${advert.price}</h4>
                </div>
                <div class="uk-width-expand uk-margin-large-top">
                </div>
                <div id="edit-form-${"advert" + advert.id}" class="uk-width-1-4 uk-margin-large-top">
                </div>
                <div class="uk-width-1-4 uk-margin-large-top">
                    <a class="uk-button uk-button-default" href="#modal-sections-${"advert-" + advert.id}" uk-toggle>Детали объявления</a>
                    <div id="modal-sections-${"advert-" + advert.id}" uk-modal>
                        <div class="uk-modal-dialog uk-margin-auto-vertical" uk-overflow-auto>
                            <button class="uk-modal-close-default" type="button" uk-close></button>
                            <div class="uk-modal-header">
                                <h2 class="uk-modal-title">
                                    <span class="uk-label">${formatDate(advert.createdOn)}</span>
                                    ${advert.summary}
                                </h2>
                            </div>
                            <div class="uk-modal-body">
                                <div uk-grid>
                                    <div class="uk-width-1-2">
                                        <h3>Цена авто: ${advert.price}</h3>
                                    </div>
                                    <div class="uk-width-1-2">
                                    </div>
                                    <div class="uk-width-1-2 uk-margin-remove-bottom">
                                        <p>Марка: ${advert.car.brand}</p>
                                    </div>
                                    <div class="uk-width-1-2 uk-margin-remove-bottom">
                                        <p>Кузов: ${advert.car.body}</p>
                                    </div>
                                    <div class="uk-width-1-2 uk-margin-remove-vertical">
                                        <p>Модель: ${advert.car.model}</p>
                                    </div>
                                    <div class="uk-width-1-2 uk-margin-remove-vertical">
                                        <p>Авто выпущено: ${formatDate(advert.car.produced)}</p>
                                    </div>
                                </div>
                                <hr class="uk-divider-icon">
                                <div uk-slideshow="animation: push; min-height: 360; max-height: 360; autoplay: true; autoplay-interval: 5000" class="uk-width-1-1">
                                    <div class="uk-position-relative uk-visible-toggle uk-light">
                                        <ul id="${"modal-" + advert.id}" class="uk-slideshow-items">
                                        </ul>
                                        <a class="uk-position-center-left uk-position-small" href="#" uk-slidenav-previous uk-slideshow-item="previous"></a>
                                        <a class="uk-position-center-right uk-position-small" href="#" uk-slidenav-next uk-slideshow-item="next"></a>
                                    </div>
                                </div>
                                <hr class="uk-divider-icon">
                                <p>${advert.description}</p>
                            </div>
                            <div class="uk-modal-footer uk-text-right">
                                <button class="uk-button uk-button-default uk-modal-close" type="button">Закрыть</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `;
    return cardBody;
}

/**
 * ПОдготовка фрагмента HTML с одной из фотографий для объявления в списке.
 * @param photo объект фото.
 * @returns {string} фрагмент HTML для добавления на страницу.
 */
function addPhotoFragment(photo) {
    let fragment = `
    <li id="${"photo" + photo.id}">
        <img src="data:image/jpeg;base64,${photo.data}" alt="" uk-cover>
    </li>
    `;
    return fragment;
}

/**
 * Подготовка фрагмента HTML с кнопкой редактирования объявления.
 * @param advert объект объявления.
 * @returns {string} фрагмент HTML для добавления в объявление в списке.
 */
function prepareEditForm(advert) {
    let fragment = `
    <form action="adcard.do" method="GET">
        <input type="hidden" name="adId" value="${advert.id}">
        <button type="submit" class="uk-button uk-button-default uk-align-right">Изменить</button>
     </form>
    `;
    return fragment;
}

/**
 * Установка на странице фильтра объявлений "за последний день".
 */
function setModeLastDay() {
    $("#search-mode").text("lastday");
    $("#search-value").text("");
    setFilteringView("за последние сутки");
    showAds();
}

/**
 * Установка на странице фильтра объявлений "только с фото".
 */
function setModeOnlyPhoto() {
    $("#search-mode").text("onlyphoto");
    $("#search-value").text("");
    setFilteringView("все с фото");
    showAds();
}

/**
 * Установка на странице фильтра объявлений "все объявления".
 */
function setModeAll() {
    $("#search-mode").text("all");
    $("#search-value").text("");
    setFilteringView("все объявления");
    showAds();
}

/**
 * Установка на странице значения для фильтра объявлений по марке
 * и немедленное обновление страницы при изменении поля.
 * Страница будет сразу обновляться по мере ввода данных.
 */
$(document).ready(function () {
        $("#search-brand").keyup(
            function (event) {
                $("#search-mode").text("bybrand");
                $("#search-value").text($("#search-brand").val());
                setFilteringView("по марке " + $("#search-value").text());
                showAds();
            }
        );
    }
);

/**
 * Отобразить на странице используемый в данный момент фильтр.
 * @param filterModeText режим фильтра.
 */
function setFilteringView(filterModeText) {
    $("#filter-mode").empty();
    let modeView = `
        <div>Отфильтровано: ${filterModeText}</div>
    `;
    $("#filter-mode").append(modeView);
}

/**
 * Просто вспомогательная функция для форматирования времени.
 * @param date время в UTC
 * @returns {string} человекочитаемое время.
 */
function formatDate(date) {
    let dateObject = new Date(date);
    let year = dateObject.getFullYear();
    let month = (dateObject.getMonth() + 1) >= 10 ? dateObject.getMonth() + 1 : "0" + (dateObject.getMonth() + 1);
    let day = dateObject.getDate() >= 10 ? dateObject.getDate() : "0" + dateObject.getDate();
    let hour = dateObject.getHours() >= 10 ? dateObject.getHours() : "0" + dateObject.getHours();
    let minute = dateObject.getMinutes() >= 10 ? dateObject.getMinutes() : "0" + dateObject.getMinutes();
    let second = dateObject.getSeconds() >= 10 ? dateObject.getSeconds() : "0" + dateObject.getSeconds();
    let timeString = hour + ":" + minute + ":" + second;
    let dateString = day + "." + month + "." + year;
    return dateString;
}

