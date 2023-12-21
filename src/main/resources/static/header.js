function header() {
    fetch('/api/users/authentication')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.statusText}`);
            }
            return response.json();
        }).then(data => {
        var username = data.username;
        var surname = data.surname;
        var userInfoElement = document.getElementById('user-info');
        var userRolesElement = document.getElementById('user-roles');
        var roles = '';
        if(userInfoElement) {
            userInfoElement.textContent = `${username} ${surname}`;
        }
        data.roles.forEach((role, index) => {
            roles+=role.nameWithoutRole;
            if (index< data.roles.length - 1) {
                roles += ', ';
            }
        })
        userRolesElement.textContent = roles;

    }).catch(error => {
        console.error('Error fetching user data:', error);
    });
}