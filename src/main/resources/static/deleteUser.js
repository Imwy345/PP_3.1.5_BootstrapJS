var button;
// обработчик событий модального окна Delete
document.addEventListener("DOMContentLoaded", function () {
    $('#deleteModal').on('show.bs.modal', function (event) {
        button = $(event.relatedTarget);

        var buttonId = button.data('user-id');
        fetch('api/findUserById/' + buttonId)
            .then(response => {
                return response.json();
            })
            .then(data => {
                document.getElementById('IdDeleteForm').value = data.id;
                document.getElementById('usernameDeleteForm').value = data.username;
                document.getElementById('surnameDeleteForm').value = data.surname;
                document.getElementById('ageDeleteForm').value = data.age;
                document.getElementById('emailDeleteForm').value = data.email;
                document.getElementById('passwordDeleteForm').value = data.password;
                var userRoles = ["USER", "ADMIN"]; // Полученные роли из данных
                var checkbox;
                document.querySelectorAll('#deleteCheckboxs input[type="checkbox"]').forEach(checkbox => {
                    checkbox.checked = false;
                });
                data.roles.forEach((role) => {
                    if (userRoles.includes(role.nameWithoutRole)) {
                        checkbox = document.getElementById("delete_" + role.nameWithoutRole.toLowerCase() + "_checkbox");
                        if (checkbox) {
                            checkbox.checked = true;
                        }
                    }
                });
            });
        $('#userDeleteConfrim').on('click', function (event) {
            event.preventDefault();
            fetch('/api/deleteUser/' + buttonId, {
                method: 'DELETE',
            }).then(response => {
                return response.text();
            })
                .then(message => {
                    fetchData();
                    $('.modal-backdrop').remove();
                    $('#deleteModal').modal('hide');
                })
        });

    });
});