async function editProfileMenu() {
    colContent.innerHTML = "";

    let user = await (await fetch('api/user')).json();

    let form = createSimpleForm("Никнейм", "Изменить данные");
    let button = form.button;

    let passwordInput = document.createElement("input");
    passwordInput.setAttribute("type", "password");
    passwordInput.setAttribute("class", "form-control");
    passwordInput.setAttribute("placeholder", "Пароль");

    form.container.removeChild(button);

    form.container.appendChild(passwordInput);
    form.container.appendChild(button);

    form.input.value = user.username;
    passwordInput.value = user.password;

    form.button.onclick = async function() {
        let data = {
            username: form.input.value,
            password: passwordInput.value
        };
        await editProfile(data);

        form.input.value = "";
        passwordInput.value = "";
    };

    colContent.appendChild(form.main);
}

async function editProfile(user) {
    await fetch('api/user', {
        method: 'POST',
        headers: headerFetch,
        body: JSON.stringify(user)
    });
}