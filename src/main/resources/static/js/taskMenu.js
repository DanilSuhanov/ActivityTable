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

        taskElement.head.onclick = async function () {

            let card = createCard();
            let messageForm = createSimpleForm("Ввод заметок по задаче", "Отправить заметку");
            let messageList = getList();

            loadMessages(messageList, tasks[i].id);

            colContent.innerHTML = "";
            colContent.appendChild(card.main);

            card.title.textContent = tasks[i].title;
            card.text.textContent = tasks[i].description;

            colContent.appendChild(messageList);
            colContent.appendChild(messageForm.main);

            card.header.textContent = 'Ваша роль в задаче - ' + member.taskRole;

            messageForm.button.onclick = function () {
                if (messageForm.input.value === "") {
                    let notice = getNotice("danger", "Вы не ввели текст сообщения");
                    addNotice(notice, colContent);
                } else {
                    sendMessage(tasks[i].id, messageForm.input.value);

                    let messageElement = createMessage(messageForm.input.value, username, member.taskRole, true);

                    messageList.appendChild(messageElement.main);
                    messageForm.input.value = "";
                }
            }
        }
    }
}

function loadMessages(messageList, taskId) {
    fetch('api/task/' + taskId + '/messages')
        .then(res => res.json())
        .then(meses => {
            for(let i = 0; i < meses.length; i++){
                let mesElement = createMessage(meses[i].content, meses[i].member.user.username, meses[i].member.taskRole, false);
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
        });
}

function sendMessage(taskId, content) {
    fetch('api/task/' + taskId + '/message', {
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

function createMessage(text, username, role, isNew) {

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

    if (!isNew) {
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

    let row = getRow(10, 2);
    taskContent.appendChild(row.row);

    let conteiner = document.createElement("div");
    conteiner.setAttribute("class", "ms-2 me-auto");
    row.col1.appendChild(conteiner);

    let head = document.createElement("div");
    head.setAttribute("class", "fw-bold");
    conteiner.appendChild(head);

    let p = document.createElement("div");
    conteiner.appendChild(p);

    let exitTaskButton = document.createElement("button");
    exitTaskButton.setAttribute("class", "btn btn-danger w-100");
    row.col2.appendChild(exitTaskButton);

    if (role === "Руководитель") {
        exitTaskButton.textContent = "Удалить задачу";
    } else {
        exitTaskButton.textContent = "Покинуть задачу";
    }

    return {
        main: taskContent,
        head: head,
        text: p,
        exit: exitTaskButton
    };
}

async function exitTask() {

}