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

    const attributeClass = "class";
    const activeValue = "list-group-item list-group-item-action active"
    const passiveValue = "list-group-item list-group-item-action";

    menuProfile.onclick = function () {
        unActive();
        menuProfile.setAttribute(attributeClass, activeValue);
    }

    menuTasks.onclick = function () {
        unActive();
        menuTasks.setAttribute(attributeClass, activeValue);
    }

    menuSubs.onclick = function () {
        unActive();
        menuSubs.setAttribute(attributeClass, activeValue);
    }

    menuHelp.onclick = function () {
        unActive();
        menuHelp.setAttribute(attributeClass, activeValue);
    }

    function unActive () {
        menuProfile.setAttribute(attributeClass, passiveValue);
        menuTasks.setAttribute(attributeClass, passiveValue);
        menuSubs.setAttribute(attributeClass, passiveValue);
        menuHelp.setAttribute(attributeClass, passiveValue);
    }
}

switchMenu();
profileLoad();