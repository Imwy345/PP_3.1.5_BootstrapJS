var button;
document.addEventListener("DOMContentLoaded", function () {
    console.log('Script is running!');
    $('#updateModal').on('show.bs.modal', function (event) {
        button = $(event.relatedTarget);

        var buttonId = button.data('user-id');
        fetch('api/findUserById/' + buttonId)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('Data from server:', data);

                // Обработка ошибок
                if (!data || typeof data !== 'object') {
                    throw new Error('Invalid data format');
                }
                document.getElementById('IdUpdateForm').value = data.id;
                document.getElementById('usernameUpdateForm').value = data.username;
                document.getElementById('surnameUpdateForm').value = data.surname;
                document.getElementById('ageUpdateForm').value = data.age;
                document.getElementById('emailUpdateForm').value = data.email;
                document.getElementById('passwordUpdateForm').value = data.password
                var userRoles = ["USER", "ADMIN"]; // Полученные роли из данных
                var checkbox;
                document.querySelectorAll('#updateCheckboxs input[type="checkbox"]').forEach(checkbox => {
                    checkbox.checked = false;
                });
                data.roles.forEach((role, index) => {
                    if (userRoles.includes(role.nameWithoutRole)) {
                        checkbox = document.getElementById("update_" + role.nameWithoutRole.toLowerCase() + "_checkbox");
                        if (checkbox) {
                            checkbox.checked = true;
                        }
                    }
                });
            })
            .catch(error => {
                console.error('Error processing data:', error);
            });
        $('#userUpdateConfrim').on('click', function (event) {
            var idValue = $('#IdUpdateForm').val();
            var usernameValue = $('#usernameUpdateForm').val();
            var surnameValue = $('#surnameUpdateForm').val();
            var ageValue = $('#ageUpdateForm').val();
            var passwordValue = $('#passwordUpdateForm').val();
            var emailValue = $('#emailUpdateForm').val();
            var selectedRoles = [];
            $('.updateUserCheckbox:checked').each(function () {
                selectedRoles.push($(this).val());
            });
            // Создаем объект с данными
            var data = {
                id: idValue,
                username: usernameValue,
                surname: surnameValue,
                age: ageValue,
                password: passwordValue,
                email: emailValue,
                roles: selectedRoles,
            };
            fetch('/api/updateUser/', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            }).then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
                .then(message => {
                    console.log('User successfully deleted:', message);
                    fetchData();
                    header();
                    navButton();
                    // $('#deleteModal').removeClass('fade');
                    $('.modal-backdrop').remove();
                    $('#updateModal').modal('hide');


                })
                .catch(error => {
                    console.error('Error deleting user:', error);
                });
        });
    });
});