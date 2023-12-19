    function navButton() {
        fetch('/api/authenticationUser')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.statusText}`);
                }
                return response.json();
            }).then(data => {
            var userRoles = data.roles.map(role => role.nameWithoutRole);
            var navButtonAdmin = document.getElementById('navButtonAdmin');
            var navButtonUser = document.getElementById('navButtonUser');

            // Скрываем обе кнопки
            navButtonAdmin.style.display = 'none';
            navButtonUser.style.display = 'none';

            var userRole = 'USER';
            var adminRole = 'ADMIN';

            data.roles.forEach((role) => {
                if (role.nameWithoutRole === userRole) {
                    navButtonUser.style.display = 'block';
                }

                if (role.nameWithoutRole === adminRole) {
                    navButtonAdmin.style.display = 'block';
                }
            });
        })
            .catch(error => {
                console.error('Error fetching user data:', error);
            });
    }