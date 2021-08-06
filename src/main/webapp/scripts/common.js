$(document).ready(showUser);

/**
 * Показ на странице данных о пользователе.
 */
function showUser() {
    $("#loginPlace").empty();
    let currentUser = $("#currentUser").text().toUpperCase();
    if (currentUser === '') {
        let notLogged = `
            <li>
                <a href="auth.do">Войти</a>
            </li>
        `;
        $("#loginPlace").append(notLogged);
    } else {
        let logged = `
            <li>
                <div class="uk-navbar-item">
                    <div>ВЫ ВОШЛИ КАК: ${currentUser}</div>
                </div>
            </li>
            <li>
                <a href="logout.do">Выйти</a>
            </li>
        `;
        $("#loginPlace").append(logged);
    }
}