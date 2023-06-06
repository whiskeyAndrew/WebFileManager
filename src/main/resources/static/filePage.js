function sendDeleteRequest(url, isDeepDelete) {
    $.ajax({
        url: url + "?isDeepDelete=" + isDeepDelete,
        type: "DELETE",
        success: function (response) {
            console.log("DELETE запрос выполнен успешно");
            console.log(response);
            location.reload();
        },
        error: function (xhr) {
            console.log("Произошла ошибка при выполнении DELETE запроса");
            const err = xhr.status;

            switch (err) {
                case 412:
                    window.alert("Папка не пустая");
                    break;
                case 409:
                    window.alert("Произошла ошибка при удалении папки");
                    break;
                case 400:
                    window.alert("Что-то пошло не так");
                    break;
                case 404:
                    window.alert("Папка не найдена");
                    break;
            }

            location.reload();
        }

    });
}

initPage = function () {
    let page = $("body");
    page.on("click", ".js-delete-file-button", function () {
        if (confirm("Удалить файл?")) {
            let id = this.getAttribute('id');
            sendDeleteRequest('/delete/file/' + id, false);
        }
    })

    page.on("click", ".js-delete-dir-button", function () {
        if (confirm("Удалить папку?")) {
            let id = this.getAttribute('id');
            sendDeleteRequest('/delete/dir/' + id, false);
        }
    })

    page.on("click", ".js-deep-delete-dir-button", function () {
        if (confirm("Удалить папку и ВСЁ её содержимое?")) {
            let id = this.getAttribute('id');
            sendDeleteRequest('/delete/dir/' + id, true);
        }
    })
}

