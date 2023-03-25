let count = 0;
async function menuTaskLoad(colContent) {

    let username = await (await fetch('api/username')).text();

    colContent.innerHTML = `<ul class="list-group" id="listContent"></ul>`;
    let listContent = document.querySelector("#listContent");

    let tasks = await (await fetch('api/tasks')).json();

    for (let i = 0; i < tasks.length; i++) {

        let member = await (await fetch('api/task/' + tasks[i].id + '/member')).json();

        let taskElement = createTaskElement(member.taskRole);

        listContent.appendChild(taskElement.main);
        taskElement.head.textContent = tasks[i].title;
        taskElement.text.textContent = tasks[i].description;

        taskElement.addMember.button.onclick = async function() {
            await addMember(tasks[i]);
        };

        taskElement.exit.onclick = async function() {
            await exit(tasks[i].id, taskElement, listContent);
        }

        taskElement.head.onclick = async function () {

            let card = createCard();
            let messageForm = createSimpleForm("Ввод заметок по задаче", "Отправить заметку");
            let messageList = getList();

            let range = getRange(tasks[i].completeness);
            let rangeButton = document.createElement("button");
            rangeButton.setAttribute("class", "btn btn-success w-100");
            rangeButton.textContent = "Подтвердить";

            await loadMessages(messageList, tasks[i].id, username);

            colContent.innerHTML = "";
            colContent.appendChild(card.main);

            card.title.textContent = tasks[i].title;
            card.text.textContent = tasks[i].description;

            colContent.appendChild(messageList);
            colContent.appendChild(messageForm.main);

            colContent.appendChild(range.container);

            card.header.textContent = 'Ваша роль в задаче - ' + member.taskRole;

            if (member.taskRole !== "Руководитель") {
                range.container.appendChild(rangeButton);

                range.input.onchange = async function() {
                    range.label.textContent = "Завершённость задачи - " + range.input.value + "%";
                };

                rangeButton.onclick = async function () {
                    await fetch('api/task/' + tasks[i].id + '/completeness', {
                        method: 'POST',
                        headers: headerFetch,
                        body: range.input.value
                    });

                    let notice = getNotice("success", "Завершённость задачи записана!");
                    addNotice(notice, colContent);
                };
            } else {
                range.input.disabled = true;
            }

            messageForm.button.onclick = function () {
                if (messageForm.input.value === "") {
                    let notice = getNotice("danger", "Вы не ввели текст сообщения");
                    addNotice(notice, colContent);
                } else {
                    sendMessage(tasks[i].id, messageForm.input.value);

                    let messageElement = createMessage(messageForm.input.value, username, member.taskRole, false);

                    messageList.appendChild(messageElement.main);
                    messageForm.input.value = "";
                }
            }
        }
    }
}

async function exit(taskId, taskElement, listContent) {
    await fetch('api/tasks/' + taskId + '/exit');
    listContent.removeChild(taskElement.main);
}

async function loadMessages(messageList, taskId, username) {
    let meses = await (await fetch('api/task/' + taskId + '/messages')).json()

    for(let i = 0; i < meses.length; i++){
        let mesElement = createMessage(meses[i].content, meses[i].member.user.username,
            meses[i].member.taskRole, meses[i].member.user.username === username);
        mesElement.deleteButton.onclick = function () {
            fetch('api/task/deleteMessageById', {
                method: 'POST',
                headers: headerFetch,
                body: meses[i].id
            });
            messageList.removeChild(mesElement.main);
        };

        messageList.appendChild(mesElement.main);
    }
}

async function sendMessage(taskId, content) {
    await fetch('api/task/' + taskId + '/message', {
        method: 'POST',
        headers: headerFetch,
        body: content
    });
}

function createCard() {
    let card = document.createElement("div");
    card.setAttribute("class", "card");
    let cardHeader = document.createElement("div");
    cardHeader.setAttribute("class", "card-header");
    let cardBody = document.createElement("div");
    cardBody.setAttribute("class", "card-body");
    let cardTitle = document.createElement("h5");
    cardTitle.setAttribute("class", "card-title");
    let cardText = document.createElement("p");
    cardText.setAttribute("class", "card-text");

    card.appendChild(cardHeader);
    card.appendChild(cardBody);
    cardBody.appendChild(cardTitle);
    cardBody.appendChild(cardText);

    return {
        main: card,
        header: cardHeader,
        title: cardTitle,
        text: cardText
    };
}

function createMessage(text, username, role, deleteFunc) {

    let row = getRow(10, 2);

    let main = document.createElement("li");

    if (role === "Руководитель") {
        main.setAttribute("class", "list-group-item list-group-item-primary");
    } else {
        main.setAttribute("class", "list-group-item");
    }

    main.appendChild(row.row);

    let message = document.createElement("div");
    message.setAttribute("id", "mes" + count.toString());
    message.textContent = text;

    let author = document.createElement("label");
    author.setAttribute("class", "form-label");
    author.setAttribute("for", "mes" + count.toString());
    author.textContent = username;

    row.col1.appendChild(author);
    row.col1.appendChild(message);

    let butt = document.createElement("button");
    butt.setAttribute("class", "btn btn-danger w-100");
    butt.textContent = 'Удалить';

    if (deleteFunc) {
        row.col2.appendChild(butt);
    }

    count = count + 1;

    return {
        main: main,
        message: message,
        author: author,
        deleteButton: butt
    };
}

function createTaskElement(role) {
    let taskContent = getLi();

    let row = getRow(9, 3);
    taskContent.appendChild(row.row);

    let conteiner = document.createElement("div");
    conteiner.setAttribute("class", "ms-2 me-auto");
    row.col1.appendChild(conteiner);

    let head = document.createElement("div");
    head.setAttribute("class", "fw-bold");
    conteiner.appendChild(head);

    let p = document.createElement("div");
    conteiner.appendChild(p);

    let small2 = document.createElement("small");
    let exitTaskButton = document.createElement("button");
    exitTaskButton.appendChild(small2);
    exitTaskButton.setAttribute("class", "btn btn-danger w-100");
    row.col2.appendChild(exitTaskButton);

    let addButton = getAddMemberButton();

    if (role === "Руководитель") {
        small2.textContent = "Удалить";

        exitTaskButton.setAttribute("class", "btn btn-danger w-50");
        row.col2.appendChild(addButton.button);
    } else {
        small2.textContent = "Покинуть";
    }

    return {
        main: taskContent,
        head: head,
        text: p,
        exit: exitTaskButton,
        addMember: addButton
    };
}

function getAddMemberButton() {
    let small = document.createElement("small");
    let addButton = document.createElement("button");
    addButton.setAttribute("class", "btn btn-success w-50");
    small.textContent = "Участники";
    addButton.appendChild(small);

    return {
        button: addButton,
        text: small
    };
}

async function exitTask() {

}

async function addMember(task) {
    colContent.innerHTML = "";

    let implementers = await (await fetch('api/user/implementers')).json();
    let members = await (await fetch('api/task/' + task.id + '/members')).json();
    let memberNames = members.map(item => item.user.username);

    let checks = [];
    let checkLi = getLi();
    checkLi.setAttribute("class", "list-group-item list-group-item-light");
    colContent.appendChild(checkLi);

    for (let i = 0; i < implementers.length; i++) {
        let checkBox = getCheckBox(implementers[i].username);
        if (memberNames.includes(implementers[i].username)) {
            checkBox.input.checked = true;
        }
        checks.push(checkBox);

        checks.push();
        checkLi.appendChild(checks[i].container);
    }

    console.log(memberNames)

    let button = document.createElement("button");
    button.setAttribute("class", "btn btn-success w-100");
    button.textContent = "Сохранить";

    button.onclick = async function() {
        members = await (await fetch('api/task/' + task.id + '/members')).json();
        memberNames = members.map(item => item.user.username);

        let changes = false;

        for (let i = 0; i < checks.length; i ++) {
            if (memberNames.includes(checks[i].label.textContent) && checks[i].input.checked === false) {
                changes = true;
                await fetch('api/task/' + task.id + '/deleteMember', {
                    method: 'POST',
                    headers: headerFetch,
                    body: checks[i].label.textContent
                });
                console.log("Удалён " + checks[i].label.textContent);
            } else if (!(memberNames.includes(checks[i].label.textContent)) && checks[i].input.checked === true) {
                changes = true;
                await fetch('api/task/' + task.id + '/addUser', {
                    method: 'POST',
                    headers: headerFetch,
                    body: checks[i].label.textContent
                });
                console.log("Добавлен " + checks[i].label.textContent);
            }
        }

        if (changes) {
            let notice = getNotice("success", "Изменения внесены!");
            addNotice(notice, colContent);
        } else {
            let notice = getNotice("danger", "Изменений не произашло!");
            addNotice(notice, colContent);
        }
    };

    colContent.appendChild(button);
}