async function profileLoad() {
    colContent.innerHTML = `<div class="card">
                    <div class="row g-0">
                        <div class="col">
                            <div class="card-body">
                                <h5 class="card-title" id="cardUsername"></h5>
                                <p class="card-text" id="cardSubordinates"></p>
                                <p class="card-text" id="cardManagers"></p>
                                <p class="card-text" id="cardTelegram"></p>
                                <p id="cardText" class="card-text">
                                    <button id="editProfileButton" type="button" class="btn btn-primary">Изменить пароль</button>
                                    <button id="implementButton" type="button" class="btn btn-primary">Исполнители</button>
                                    <button id="requestButton" type="button" class="btn btn-primary">Заявки на руководство</button>
                                </p>
                            </div>
                        </div>
                    </div>
                  </div>`;

    let editProfileButton = document.querySelector("#editProfileButton");
    let implemetButton = document.querySelector("#implementButton");
    let requestButton = document.querySelector("#requestButton");

    let cardUsername = document.querySelector("#cardUsername");
    let cardImplement = document.querySelector("#cardSubordinates");
    let cardManagers = document.querySelector("#cardManagers");
    let cardTelegram = document.querySelector("#cardTelegram");

    let user = await (await fetch(`api/user`)).json();

    cardUsername.textContent = user.username;

    let implementers = await (await fetch('api/user/getAllImp')).json();

    await printCollection(implementers, cardImplement, "Исполнители");

    let managers = await (await fetch('api/user/getAllManagers')).json();

    await printCollection(managers, cardManagers, "Руководители");

    if (user.telegramUser != null) {
        cardTelegram.textContent = "Телеграм - " + user.telegramUser.username;
    } else {
        cardTelegram.textContent = "Телеграм не привязан";
    }

    implemetButton.onclick = async function() {
        await implementMenu(implementers);
    };

    requestButton.onclick = async function() {
        await requestMenu();
    };

    editProfileButton.onclick = async function() {
        await editProfileMenu();
    };
}

async function printCollection(colection, tag, title) {
    if (colection.length !== 0) {
        tag.textContent = title + ": ";

        for (let i = 0; i < colection.length; i++) {
            tag.textContent += colection[i].username;
            if (i !== colection.length - 1) {
                tag.textContent += ", ";
            }
        }
    } else {
        tag.textContent = "У вас нет - " + title;
    }
}

async function requestMenu() {
    await addInvitesToList(setListOnColContent());
}

async function addInvitesToList(list) {
    let invites = await (await  fetch('api/user/impInvites')).json();

    if (invites.length !== 0) {
        invites.forEach(invite => {
            let li = getLi();

            let row = getRow(8, 4);
            li.appendChild(row.row);

            let name = document.createElement("h5");
            name.textContent = invite.sender.username;
            row.col1.appendChild(name);

            let acceptButton = document.createElement("button");
            acceptButton.setAttribute("class", "btn btn-success w-50");
            acceptButton.textContent = "Принять";

            let rejectButton = document.createElement("button");
            rejectButton.setAttribute("class", "btn btn-danger w-50");
            rejectButton.textContent = "Отклонить";

            row.col2.appendChild(acceptButton);
            row.col2.appendChild(rejectButton);

            rejectButton.onclick = async function() {
                await rejectInvites(invite.id);
                list.removeChild(li);
            };

            acceptButton.onclick = async function() {
                await acceptInvites(invite.id);
                list.removeChild(li);
            };

            list.appendChild(li);
        });
    } else {
        colContent.innerHTML = '<h3>У вас нет приглашений</h3>';
    }
}

async function acceptInvites(id) {
    await fetch('api/user/invite/accept', {
        method: 'POST',
        headers: headerFetch,
        body: id
    });
}

async function rejectInvites(id) {
    await fetch('api/user/invite/reject', {
        method: 'POST',
        headers: headerFetch,
        body: id
    });
}


async function implementMenu(implementers) {
    await addImplementersToList(setListOnColContent(), implementers);
}

async function addImplementersToList(list, implementers) {
    implementers.forEach(imp => {
        let li = getLi();

        let row = getRow(10, 2);
        li.appendChild(row.row);

        let deleteImpButton = document.createElement("button");
        deleteImpButton.setAttribute("class", "btn btn-danger w-100");
        deleteImpButton.textContent = "Удалить";

        let text = document.createElement("div");
        text.textContent = imp.username;

        deleteImpButton.onclick = async function() {
            await deleteImpl(imp);
            list.removeChild(li);
        };

        row.col1.appendChild(text);
        row.col2.appendChild(deleteImpButton);

        list.appendChild(li);
    });

    let li = getLi();
    let findButton = document.createElement("button");
    findButton.setAttribute("class", "btn btn-primary w-100");
    findButton.textContent = "Найти исполнителей";

    li.appendChild(findButton);
    list.appendChild(li);

    findButton.onclick = async function() {
        await findImplementersMenu();
    };
}

async function findImplementersMenu() {
    colContent.innerHTML = "";
    let form = createSimpleForm("Никнейм исполнителя", "Отправить запрос");
    colContent.appendChild(form.main);

    form.button.onclick = async function() {
        let findUsername = form.input.value;
        let susses = await fetch('api/user/implementers/add', {
            method: 'POST',
            headers: headerFetch,
            body: findUsername
        });

        if (susses.status === 200) {
            let notice = getNotice("success", "Запрос отправлен!");
            addNotice(notice, colContent);
        } else {
            let notice = getNotice("danger", "Такого пользователя не существует!")
            addNotice(notice, colContent);
        }

        form.input.value = "";
    };
}

async function deleteImpl(imp) {
    await fetch('api/user/implement/delete', {
        method: 'POST',
        headers: headerFetch,
        body: imp.id
    });
}