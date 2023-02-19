let count = 0;
async function menuTaskLoad(colContent) {

    let username = await (await fetch('api/username')).text();

    colContent.innerHTML = `<ul class="list-group list-group-numbered" id="listContent"></ul>`;
    let listContent = document.querySelector("#listContent");

    fetch('api/tasks')
        .then(res => res.json())
        .then(tasks => {
            tasks.forEach(task => {
                let taskElement = createTaskElement();

                listContent.appendChild(taskElement.main);
                taskElement.head.textContent = task.title;
                taskElement.text.textContent = task.description;

                taskElement.head.onclick = function () {

                    let card = createCard();
                    let messageForm = createMessageForm();
                    let messageList = createMessageList();

                    loadMessages(messageList, task.id);

                    colContent.innerHTML = "";
                    colContent.appendChild(card.main);

                    card.title.textContent = task.title;
                    card.text.textContent = task.description;

                    colContent.appendChild(messageList);
                    colContent.appendChild(messageForm.main);

                    fetch('api/task/' + task.id + '/member')
                        .then(res => res.json())
                        .then(member => {
                            card.header.textContent = 'Ваша роль в задаче - ' + member.taskRole;

                            messageForm.button.onclick = function () {
                                if (messageForm.input.value === "") {
                                    console.log("Не ввел сообщение");//TODO
                                } else {
                                    sendMessage(task.id, messageForm.input.value);

                                    let messageElement = createMessage(messageForm.input.value, username);

                                    messageList.appendChild(messageElement.main);
                                    messageForm.input.value = "";
                                }
                            }
                        });
                }
            })
        });
}

function loadMessages(messageList, taskId) {
    fetch('api/task/' + taskId + '/messages')
        .then(res => res.json())
        .then(meses => {
            meses.forEach(mes => {
                fetch('api/usernameByMessageId/' + mes.id)
                    .then(username => username.text())
                    .then(usernameText => {
                        let mesElement = createMessage(mes.content, usernameText, mes.date);
                        mesElement.deleteButton.onclick = function () {
                            fetch('api/task/deleteMessageById', {
                                method: 'POST',
                                headers: headerFetch,
                                body: mes.id
                            });
                            messageList.removeChild(mesElement.main);
                        };

                        messageList.appendChild(mesElement.main);
                    });
            });
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

function createMessageForm() {
    let form = document.createElement("form");
    let container = document.createElement("div");
    container.setAttribute("class", "mb-3");
    let input = document.createElement("input");
    input.setAttribute("type", "text");
    input.setAttribute("class", "form-control");
    input.setAttribute("id", "messageInput");
    input.setAttribute("placeholder", "Ввод заметок по задаче");
    container.appendChild(input);
    form.appendChild(container);
    let button = document.createElement("div");
    button.setAttribute("class", "btn btn-success w-100");
    button.textContent = "Отправить заметку";
    container.appendChild(button);


    return {
        main: form,
        button: button,
        input: input
    };
}

function createMessageList() {
    let list = document.createElement("ul");
    list.setAttribute("class", "list-group");

    return list;
}

function createMessage(text, username) {

    let row = getRow();

    let main = document.createElement("li");
    main.setAttribute("class", "list-group-item");

    main.appendChild(row.row);

    let message = document.createElement("div");
    message.setAttribute("id", "mes" + count.toString());
    message.textContent = text;

    let author = document.createElement("label");
    author.setAttribute("class", "form-label");
    author.setAttribute("for", "mes" + count.toString());
    author.textContent = username;

    let butt = document.createElement("button");
    butt.setAttribute("class", "btn btn-danger w-100");
    butt.textContent = 'Удалить';

    row.col1.appendChild(author);
    row.col1.appendChild(message);
    row.col2.appendChild(butt);

    count = count + 1;

    return {
        main: main,
        message: message,
        author: author,
        deleteButton: butt
    };
}

function getRow() {
    let row = document.createElement("div");
    row.setAttribute("class", "row");

    let col1 = document.createElement("div");
    col1.setAttribute("class", "col-10");

    let col2 = document.createElement("div");
    col2.setAttribute("class", "col-2");

    row.appendChild(col1);
    row.appendChild(col2);

    return {
        row: row,
        col1: col1,
        col2: col2
    };
}

function createTaskElement() {
    let taskContent = document.createElement("li");
    let conteiner = document.createElement("div");
    conteiner.setAttribute("class", "ms-2 me-auto");
    let head = document.createElement("div");
    head.setAttribute("class", "fw-bold");
    let p = document.createElement("div");
    conteiner.appendChild(head);
    conteiner.appendChild(p);
    taskContent.setAttribute("class", "list-group-item d-flex justify-content-between align-items-start");
    taskContent.appendChild(conteiner);

    return {
        main: taskContent,
        head: head,
        text: p
    };
}