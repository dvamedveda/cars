/**
 * Обработчик формы загрузки файлов на сервер.
 */
UIkit.upload('.js-upload', {

    url: 'photo.do',
    multiple: true,
    params: {adId: $("#adId")[0].value},

    beforeSend: function (environment) {
        console.log('beforeSend', arguments);
    },
    beforeAll: function () {
        console.log('beforeAll', arguments);
    },
    load: function () {
        console.log('load', arguments);
    },
    error: function () {
        console.log('error', arguments);
    },
    complete: function () {
        console.log('complete', arguments);
    },
    loadStart: function (e) {
        console.log('loadStart', arguments);
    },
    progress: function (e) {
        console.log('progress', arguments);
    },
    loadEnd: function (e) {
        console.log('loadEnd', arguments);
    },
    completeAll: function () {
        console.log('completeAll', arguments);
        loadPhoto()
    }
});