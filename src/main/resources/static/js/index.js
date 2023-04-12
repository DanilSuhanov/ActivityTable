let colContent = document.querySelector("#colContent");

let menuProfile = document.querySelector("#switchProfile");
let menuTasks = document.querySelector("#switchTasks");
let menuTasksGone = document.querySelector("#switchTasksGone");
let menuSubs = document.querySelector("#switchSubs");
let menuHelp = document.querySelector("#switchHelp");
let telegramMenu = document.querySelector("#telegramMenu");

headerFetch = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Referer': null
}

async function switchMenu() {
    const attributeClass = "class";
    const activeValue = "list-group-item list-group-item-action active"
    const passiveValue = "list-group-item list-group-item-action";

    menuProfile.onclick = async function () {
        update(attributeClass, passiveValue);
        menuProfile.setAttribute(attributeClass, activeValue);
        await profileLoad();
    }

    menuTasks.onclick = async function() {
        update(attributeClass, passiveValue);
        menuTasks.setAttribute(attributeClass, activeValue);
        await menuTaskLoad(colContent, false);
    }

    menuTasksGone.onclick = async function() {
        update(attributeClass, passiveValue);
        menuTasksGone.setAttribute(attributeClass, activeValue);
        await menuTaskLoad(colContent, true);
    }

    menuSubs.onclick = async function () {
        update(attributeClass, passiveValue);
        menuSubs.setAttribute(attributeClass, activeValue);
        colContent.innerHTML = "";

        await createTaskMenu(colContent);
    }

    menuHelp.onclick = async function () {
        update(attributeClass, passiveValue);
        menuHelp.setAttribute(attributeClass, activeValue);

        colContent.innerHTML = "";
    }

    telegramMenu.onclick = async function() {
        update(attributeClass, passiveValue);
        telegramMenu.setAttribute(attributeClass, activeValue);
        await loadTelegramMenu();
    }
}

function update (attributeClass, value) {
    menuProfile.setAttribute(attributeClass, value);
    menuTasks.setAttribute(attributeClass, value);
    menuTasksGone.setAttribute(attributeClass, value);
    menuSubs.setAttribute(attributeClass, value);
    menuHelp.setAttribute(attributeClass, value);
    telegramMenu.setAttribute(attributeClass, value);
}

switchMenu();
profileLoad();