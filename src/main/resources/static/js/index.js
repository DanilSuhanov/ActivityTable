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
    let menuProfile = document.querySelector("#switchProfile");
    let menuTasks = document.querySelector("#switchTasks");
    let menuSubs = document.querySelector("#switchSubs");
    let menuHelp = document.querySelector("#switchHelp");

    let colContent = document.querySelector("#colContent");

    const attributeClass = "class";
    const activeValue = "list-group-item list-group-item-action active"
    const passiveValue = "list-group-item list-group-item-action";

    menuProfile.onclick = function () {
        unActive();
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

    menuTasks.onclick = function () {
        unActive();
        menuTasks.setAttribute(attributeClass, activeValue);

        switchContent(`<ol class="list-group list-group-numbered">
                  <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="ms-2 me-auto">
                      <div class="fw-bold">Subheading</div>
                      Content for list item
                    </div>
                    <span class="badge bg-primary rounded-pill">14</span>
                  </li>
                  <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="ms-2 me-auto">
                      <div class="fw-bold">Subheading</div>
                      Content for list item
                    </div>
                    <span class="badge bg-primary rounded-pill">14</span>
                  </li>
                  <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="ms-2 me-auto">
                      <div class="fw-bold">Subheading</div>
                      Content for list item
                    </div>
                    <span class="badge bg-primary rounded-pill">14</span>
                  </li>
                </ol>`);
    }

    menuSubs.onclick = function () {
        unActive();
        menuSubs.setAttribute(attributeClass, activeValue);

        switchContent("");
    }

    menuHelp.onclick = function () {
        unActive();
        menuHelp.setAttribute(attributeClass, activeValue);

        switchContent("");
    }

    function unActive () {
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