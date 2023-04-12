async function createTelegramUserOnClickButton(registrationForm) {
    registrationForm.button.onclick = async function () {
        await fetch('api/telegram/telegramUser/new', {
            method: 'POST',
            headers: headerFetch,
            body: registrationForm.input.value
        });

        addNotice(getNotice("success", "Запрос на привязку телеграм отправлена!"), colContent)
    }
}

async function getWarningTelegramText() {
    let info = document.createElement("p");
    info.textContent = "После создания запроса на привязку в телеграм, необходимо написать боту /registration\nБот - @ActivityTable_bot";

    return info;
}

async function loadTelegramMenu() {
    let user = await (await fetch(`api/user`)).json();

    if (user.telegramUser == null) {
        colContent.innerHTML = "";

        let info = await getWarningTelegramText();
        colContent.appendChild(info);

        let registrationForm = createSimpleForm("Username в телеграм", "Привязать");
        colContent.appendChild(registrationForm.main);

        await createTelegramUserOnClickButton(registrationForm);
    } else {
        colContent.innerHTML = "Телеграм уже привязан";
    }
}