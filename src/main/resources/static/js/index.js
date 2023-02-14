let menuProfile = document.querySelector("#switchProfile");
let menuTasks = document.querySelector("#switchTasks");
let menuSubs = document.querySelector("#switchSubs");
let menuHelp = document.querySelector("#switchHelp");

async function profileLoad() {
    await fetch(`api/user`)
        .then(res => res.json())
        .then(user => {
            document.querySelector("#cardUsername").textContent = user.username;
            document.querySelector("#cardSubordinates")
                .textContent = "Подчинённые: " + user.subordinates.map(sub => " " + sub.username);
        });
}

async function switchMenu() {
    let colContent = document.querySelector("#colContent");

    const attributeClass = "class";
    const activeValue = "list-group-item list-group-item-action active"
    const passiveValue = "list-group-item list-group-item-action";

    menuProfile.onclick = function () {
        update();
        menuProfile.setAttribute(attributeClass, activeValue);

        switchContent(`<div class="card">
                    <div class="row g-0">
                        <div class="col">
                            <div class="card-body">
                                <h5 class="card-title" id="cardUsername"></h5>
                                <p class="card-text" id="cardSubordinates"></p>
                                <p class="card-text"><button type="button" class="btn btn-primary">Изменить ифнормацию</button></p>
                            </div>
                        </div>
                    </div>
                  </div>`);
        profileLoad();
    }

    menuTasks.onclick = async function () {
        update();
        menuTasks.setAttribute(attributeClass, activeValue);

        switchContent(`<ul class="list-group list-group-numbered" id="listContent"></ul>`);
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
                })
            });
    }

    menuSubs.onclick = function () {
        update();
        menuSubs.setAttribute(attributeClass, activeValue);

        switchContent("");
    }

    menuHelp.onclick = function () {
        update();
        menuHelp.setAttribute(attributeClass, activeValue);

        switchContent("");
    }

    function update () {
        menuProfile.setAttribute(attributeClass, passiveValue);
        menuTasks.setAttribute(attributeClass, passiveValue);
        menuSubs.setAttribute(attributeClass, passiveValue);
        menuHelp.setAttribute(attributeClass, passiveValue);
    }

    function switchContent(content) {
        colContent.innerHTML = content;
    }
}

switchMenu();
profileLoad();