let menuProfile = document.querySelector("#switchProfile");
let menuTasks = document.querySelector("#switchTasks");
let menuSubs = document.querySelector("#switchSubs");
let menuHelp = document.querySelector("#switchHelp");

async function switchMenu() {
    let colContent = document.querySelector("#colContent");

    const attributeClass = "class";
    const activeValue = "list-group-item list-group-item-action active"
    const passiveValue = "list-group-item list-group-item-action";

    menuProfile.onclick = function () {
        update(attributeClass, passiveValue);
        menuProfile.setAttribute(attributeClass, activeValue);

        createCard(colContent);
        profileLoad();
    }

    menuTasks.onclick = function() {
        update(attributeClass, passiveValue);
        menuTasks.setAttribute(attributeClass, activeValue);
        menuTaskLoad(colContent);
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