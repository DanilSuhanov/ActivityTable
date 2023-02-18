let menuProfile = document.querySelector("#switchProfile");
let menuTasks = document.querySelector("#switchTasks");
let menuSubs = document.querySelector("#switchSubs");
let menuHelp = document.querySelector("#switchHelp");

headerFetch = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Referer': null
}

async function switchMenu() {
    let colContent = document.querySelector("#colContent");

    const attributeClass = "class";
    const activeValue = "list-group-item list-group-item-action active"
    const passiveValue = "list-group-item list-group-item-action";

    menuProfile.onclick = function () {
        update(attributeClass, passiveValue);
        menuProfile.setAttribute(attributeClass, activeValue);

        colContent.innerHTML = `<div class="card">
                    <div class="row g-0">
                        <div class="col">
                            <div class="card-body">
                                <h5 class="card-title" id="cardUsername"></h5>
                                <p class="card-text" id="cardSubordinates"></p>
                                <p class="card-text"><button type="button" class="btn btn-primary">Изменить ифнормацию</button></p>
                            </div>
                        </div>
                    </div>
                  </div>`;
        profileLoad();
    }

    menuTasks.onclick = async function() {
        update(attributeClass, passiveValue);
        menuTasks.setAttribute(attributeClass, activeValue);
        await menuTaskLoad(colContent);
    }

    menuSubs.onclick = function () {
        update(attributeClass, passiveValue);
        menuSubs.setAttribute(attributeClass, activeValue);

        colContent.innerHTML = "";
    }

    menuHelp.onclick = function () {
        update(attributeClass, passiveValue);
        menuHelp.setAttribute(attributeClass, activeValue);

        colContent.innerHTML = "";
    }
}

function update (attributeClass, passiveValue) {
    menuProfile.setAttribute(attributeClass, passiveValue);
    menuTasks.setAttribute(attributeClass, passiveValue);
    menuSubs.setAttribute(attributeClass, passiveValue);
    menuHelp.setAttribute(attributeClass, passiveValue);
}

switchMenu();
profileLoad();