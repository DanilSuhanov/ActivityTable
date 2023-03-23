async function editProfileMenu() {
    colContent.innerHTML = "";

    let password = await (await fetch('api/user/getPassword')).text();
    let form = createSimpleForm("Пароль", "Изменить данные");

    form.input.value = password;

    form.button.onclick = async function() {
        await editProfile(form.input.value);

        form.input.value = "";

        let notice = getNotice("success", "Пароль изменён!");
        addNotice(notice, colContent);
    };

    colContent.appendChild(form.main);
}

async function editProfile(password) {
    await fetch('api/user/editPassword', {
        method: 'POST',
        headers: headerFetch,
        body: password
    });
}