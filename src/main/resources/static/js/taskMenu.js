function menuTaskLoad(colContent) {

    colContent.innerHTML = `<ul class="list-group list-group-numbered" id="listContent"></ul>`;
    let listContent = document.querySelector("#listContent");

    fetch('api/tasks')
        .then(res => res.json())
        .then(tasks => {
            tasks.forEach(task => {
                let taskContent = document.createElement("li");

                let conteiner = document.createElement("div");
                conteiner.setAttribute("class", "ms-2 me-auto");

                let head = document.createElement("div");
                head.setAttribute("class", "fw-bold");

                let p = document.createElement("div");

                conteiner.appendChild(head);
                conteiner.appendChild(p);

                taskContent.setAttribute("class", "list-group-item d-flex justify-content-between align-items-start");

                head.textContent = task.title;
                p.textContent = task.description;

                taskContent.appendChild(conteiner);
                listContent.appendChild(taskContent);

                head.onclick = function () {
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

                    fetch('api/task/' + task.id + '/member')
                        .then(res => res.json())
                        .then(member => {
                           cardHeader.textContent = 'Ваша роль в задаче - ' + member.taskRole;
                        });

                    colContent.innerHTML = "";
                    colContent.appendChild(card);

                    cardTitle.textContent = task.title;
                    cardText.textContent = task.description;
                }
            })
        });
}