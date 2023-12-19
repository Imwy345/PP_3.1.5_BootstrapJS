document.addEventListener("DOMContentLoaded", function () {
    console.log("DOMContentLoaded event fired");
    fetchData();
    header();
    navButton();
});
    function fetchData() {
        fetch('/api/users')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => {
                mainTable(data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }
    function mainTable(users){
        // Получаем таблицу
        var table = document.getElementById("users-table");

        // Очищаем содержимое таблицы
        table.innerHTML = "";
        console.log("Table cleared");

        // Перебираем пользователей и добавляем строки в таблицу
        users.forEach(user => {
            var row = table.insertRow(-1);

            // Добавляем ячейки с данными пользователя
            row.insertCell(0).textContent = user.id;
            row.insertCell(1).textContent = user.username;
            row.insertCell(2).textContent = user.surname;
            row.insertCell(3).textContent = user.age;
            row.insertCell(4).textContent = user.email;

            // Создаем ячейку для ролей и добавляем их
            var rolesCell = row.insertCell(5);
            if (user.roles && Array.isArray(user.roles)) {
                user.roles.forEach((role, index) => {
                    rolesCell.textContent += role.nameWithoutRole;
                    if (index < user.roles.length - 1) {
                        rolesCell.textContent += ' ';
                    }
                });
            }

            // Добавляем кнопки Update и Delete
            var updateCell = row.insertCell(6);
            var deleteCell = row.insertCell(7);

            updateCell.innerHTML = `<button type="button" class="btn btn-success" data-toggle="modal"
                             data-target="#updateModal" data-user-id="${user.id}">
                             Update
                           </button>`;

            deleteCell.innerHTML = `<button type="button" class="btn btn-danger" data-toggle="modal"
                             data-target="#deleteModal" data-user-id="${user.id}">
                             Delete
                           </button>`;

        });


    }
