document.addEventListener("DOMContentLoaded", function() {
    $('#addButton').on('click', function (event) {
        event.preventDefault();
        // Получаем значение из поля username
        var usernameValue = $('#username').val();
        var surnameValue = $('#surname').val();
        var ageValue = $('#age').val(); // Преобразование в строку
        var passwordValue = $('#password').val();
        var emailValue = $('#email').val();// Преобразование в строку
        var selectedRoles = [];
        $('.role-checkbox:checked').each(function () {
            selectedRoles.push($(this).val());
        });

        var data = {
            username: usernameValue,
            surname: surnameValue,
            age:ageValue,
            password:passwordValue,
            email:emailValue,
            roles:selectedRoles,
        };

        fetch('/api/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(savedUser => {
                console.log('Успех', savedUser);

                $('#username').val('');
                $('#surname').val('');
                $('#age').val('');
                $('#password').val('');
                $('#email').val('');
                $('.role-checkbox').prop('checked', false);

                fetchData();
                $('#usersTab').tab('show');
            })
    });
});