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
                                    messageList.appendChild(createMessage(messageForm.input.value, username).message);
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
                        messageList.appendChild(createMessage(mes.content, usernameText).message);
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
    let message = document.createElement("li");
    message.setAttribute("class", "list-group-item");
    message.textContent = text;

    let author = document.createElement("span");
    author.setAttribute("class", "badge bg-secondary");
    author.textContent = username;
    message.appendChild(author);

    return {
        message: message,
        author: author
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