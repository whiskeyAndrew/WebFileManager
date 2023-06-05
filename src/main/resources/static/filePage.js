function sendDeleteRequest(url) {
    $.ajax({
        url: url,
        type: "DELETE",
        success: function (response) {
            console.log("DELETE запрос выполнен успешно");
            console.log(response);
            location.reload();
        },
        error: function (xhr) {
            console.log("Произошла ошибка при выполнении DELETE запроса");
            window.alert("Ошибка удаления файла");
            var err = xhr.status;
            console.log(err);
            location.reload();
        }

    });
}

initPage = function () {
    let page = $("body");
    page.on("click", ".js-delete-file-button", function () {
        let id = this.getAttribute('id');
        sendDeleteRequest('/delete/file/' + id);

    })

    page.on("click", ".js-delete-dir-button", function () {
        let id = this.getAttribute('id');
        sendDeleteRequest('delete/dir/'+id);
    })
}

