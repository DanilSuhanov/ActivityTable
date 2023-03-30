let count = 0;

async function getTasks(verificated) {
    return (await (await fetch('api/tasks')).json())
        .filter((task) => task.verification === verificated);
}

function checkTasksOnEmpty(tasks, colContent) {
    if (tasks.length === 0) {
        let mesAboutNull = document.createElement("p");
        mesAboutNull.textContent = "Задачи отсутствуют"
        colContent.appendChild(mesAboutNull);
    }
}

async function getCurrentMember(task) {
    return await (await fetch('api/task/' + task.id + '/member')).json();
}

async function getUsernameCurrentUser() {
    return await (await fetch('api/username')).text();
}

async function menuTaskLoad(colContent, verificated) {
    let listContent = setListOnColContent();
    let tasks = await getTasks(verificated);
    checkTasksOnEmpty(tasks, colContent);

    for (let i = 0; i < tasks.length; i++) {
        let taskElement = await createTaskElement(tasks[i], listContent);
        listContent.appendChild(taskElement.li);
    }
}

async function exit(taskId, taskElement, listContent) {
    await fetch('api/tasks/' + taskId + '/exit');
    listContent.removeChild(taskElement);
}

async function loadMessages(messageList, taskId, username) {
    let meses = await (await fetch('api/task/' + taskId + '/messages')).json()

    for(let i = 0; i < meses.length; i++){
        let mesElement = await createMessage(meses[i], meses[i].member.user.username === username, messageList);
        messageList.appendChild(mesElement.main);
    }
}

async function sendMessage(taskId, content) {
    return await (await fetch('api/task/' + taskId + '/message', {
        method: 'POST',
        headers: headerFetch,
        body: content
    })).json();
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

async function deleteButton(deleteFunc, row, mes, list, main) {
    if (deleteFunc) {
        let butt = document.createElement("button");
        butt.setAttribute("class", "btn btn-danger w-100");
        butt.textContent = 'Удалить';

        row.col2.appendChild(butt);

        butt.onclick = async function () {
            await fetch('api/task/deleteMessageById', {
                method: 'POST',
                headers: headerFetch,
                body: mes.id
            });
            list.removeChild(main);
        };
    }
}

async function createMessage(mes, deleteFunc, list) {
    let row = getRow(10, 2);
    let main = document.createElement("li");

    if (mes.member.taskRole === "Руководитель") {
        main.setAttribute("class", "list-group-item list-group-item-primary");
    } else {
        main.setAttribute("class", "list-group-item");
    }

    main.appendChild(row.row);

    let message = document.createElement("div");
    message.setAttribute("id", "mes" + count.toString());
    message.textContent = mes.content;

    let author = document.createElement("label");
    author.setAttribute("class", "form-label");
    author.setAttribute("for", "mes" + count.toString());
    author.textContent = mes.member.user.username;

    row.col1.appendChild(author);
    row.col1.appendChild(message);

    await deleteButton(deleteFunc, row, mes, list, main);

    count = count + 1;

    return {
        main: main,
        message: message,
        author: author
    };
}

async function setColorByCompleteness(task, taskContent) {
    if (task.completeness === 100) {
        if (task.verification) {
            taskContent.setAttribute("class", "list-group-item list-group-item-success");
        } else {
            taskContent.setAttribute("class", "list-group-item list-group-item-warning");
        }
    }
}

async function getTaskView(task) {
    let taskLi = getLi();

    await setColorByCompleteness(task, taskLi);

    let row = getRow(9, 3);
    taskLi.appendChild(row.row);

    let container = document.createElement("div");
    container.setAttribute("class", "ms-2 me-auto");
    row.col1.appendChild(container);

    let head = document.createElement("div");
    head.setAttribute("class", "fw-bold");
    container.appendChild(head);

    let text = document.createElement("div");
    container.appendChild(text);

    return {
        li: taskLi,
        row: row,
        container: container,
        head: head,
        text: text
    };
}

async function getAcceptButton() {
    let rangeButton = document.createElement("button");
    rangeButton.setAttribute("class", "btn btn-success w-100");
    rangeButton.textContent = "Подтвердить";
    return rangeButton;
}

async function openTaskLogic(taskView, task, username, member) {
    taskView.head.onclick = async function () {

        let card = createCard();
        let messageList = getList();
        let range = getRange(task.completeness);
        let rangeButton = await getAcceptButton();

        await loadMessages(messageList, task.id, username);

        colContent.innerHTML = "";
        colContent.appendChild(card.main);

        card.title.textContent = task.title;
        card.text.textContent = task.description;

        colContent.appendChild(messageList);

        if (!task.verification) {
            let messageForm = createSimpleForm("Ввод заметок по задаче", "Отправить заметку");
            colContent.appendChild(messageForm.main);

            messageForm.button.onclick = async function () {
                if (messageForm.input.value === "") {
                    let notice = getNotice("danger", "Вы не ввели текст сообщения");
                    addNotice(notice, colContent);
                } else {
                    let mes = await sendMessage(task.id, messageForm.input.value);
                    let messageElement = await createMessage(mes, true, messageList);

                    messageList.appendChild(messageElement.main);
                    messageForm.input.value = "";
                }
            }
        }

        colContent.appendChild(range.container);

        card.header.textContent = 'Ваша роль в задаче - ' + member.taskRole;

        if (member.taskRole !== "Руководитель") {
            range.container.appendChild(rangeButton);

            range.input.onchange = async function () {
                range.label.textContent = "Завершённость задачи - " + range.input.value + "%";
            };

            rangeButton.onclick = async function () {
                await fetch('api/task/' + task.id + '/completeness', {
                    method: 'POST',
                    headers: headerFetch,
                    body: range.input.value
                });

                let notice = getNotice("success", "Завершённость задачи записана!");
                addNotice(notice, colContent);
            };
        } else {
            range.input.disabled = true;

            if (task.completeness === 100 && !task.verification) {
                colContent.appendChild(getVerificationButton(task));
            }
        }
    }
}

async function createTaskElement(task, listContent) {
    let taskView = await getTaskView(task);
    let member = await getCurrentMember(task);
    let username = await getUsernameCurrentUser();

    taskView.head.textContent = task.title;
    taskView.text.textContent = task.description;

    await openTaskLogic(taskView, task, username, member);

    if (!task.verification) {

        let small2 = document.createElement("small");
        let exitTaskButton = document.createElement("button");
        exitTaskButton.appendChild(small2);
        exitTaskButton.setAttribute("class", "btn btn-danger w-100");
        taskView.row.col2.appendChild(exitTaskButton);

        let addButton = getAddMemberButton();

        if (member.taskRole === "Руководитель") {
            small2.textContent = "Удалить";

            exitTaskButton.setAttribute("class", "btn btn-danger w-50");
            taskView.row.col2.appendChild(addButton.button);
        } else {
            small2.textContent = "Покинуть";
        }

        addButton.button.onclick = async function () {
            await addMember(task);
        };

        exitTaskButton.onclick = async function () {
            await exit(task.id, taskContent, listContent);
        }
    }

    return taskView;
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

function getVerificationButton(task) {
    let button = document.createElement("div");
    button.setAttribute("class", "btn btn-success w-100");
    button.textContent = "Подтвердить выполнение задачи";

    button.onclick = async function() {
        await fetch('api/tasks/' + task.id + '/verification');

        let notice = getNotice('success', 'Выполнение задачи подтверждено!');
        addNotice(notice, colContent);

        colContent.removeChild(button);
    }

    return button;
}