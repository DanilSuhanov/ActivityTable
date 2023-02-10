function registrationPost() {
    document.querySelector('#registrationButton').onclick = async function () {

        const form = document.querySelector('#formRegistration');

        let username = form.querySelector('#usernameRegistration').value;
        let password = form.querySelector('#passwordRegistration').value;

        let data = {
            username: username,
            password: password
        };

        await fetch(`api/users/registration`, {method: 'PUT', headers: userFetch.head, body: JSON.stringify(data)})
    }
}

registrationPost();