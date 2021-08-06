$(document).ready(loadPhoto);

/**
 * Загрузка фото на странице редактирования объявления.
 */
function loadPhoto() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/cars/photo.do',
        data: {
            adId: $("#adId")[0].value
        },
        dataType: 'json'
    }).done(function (data) {
        let response = JSON.parse(JSON.stringify(data));
        $("#photoPlace").empty();
        if (response.length === 0) {
            $("#photoPlace").append(defaultPhoto);
        } else {
            $("#photoPlace").append(photoCard);
            for (let photo of response) {
                let nextPhoto = `
                <li>
                    <img src="data:image/jpeg;base64,${photo.data}" alt="" uk-cover>
                </li>
                `;
                $("#slideshowElement").append(nextPhoto);
            }
        }
    }).fail(function (error) {
        console.log("Что-то пошло не так! Запрос не выполнился!")
    })
}

/**
 * Фрагмент добавляющий дефолтное фото.
 * @type {string}
 */
let defaultPhoto = `
    <div class="uk-card-media-top">
        <img src="https://www.seat.com/content/dam/public/seat-website/carworlds/compare/default-image/ghost.png" alt="">
    </div>
`;

/**
 * Фрагмент, добавлящий на страницу карусель из фото объявления.
 * @type {string}
 */
let photoCard = `
    <div uk-slideshow="animation: push; min-height: 400; max-height: 400; autoplay: true; autoplay-interval: 1000">
                        <div class="uk-position-relative uk-visible-toggle uk-light">
                            <ul id="slideshowElement" class="uk-slideshow-items">
                            </ul>
                            <a class="uk-position-center-left uk-position-small" href="#" uk-slidenav-previous uk-slideshow-item="previous"></a>
                            <a class="uk-position-center-right uk-position-small" href="#" uk-slidenav-next uk-slideshow-item="next"></a>
                        </div>
                    </div>
`;