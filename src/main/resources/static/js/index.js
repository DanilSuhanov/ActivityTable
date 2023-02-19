let colContent = document.querySelector("#colContent");

let menuProfile = document.querySelector("#switchProfile");
let menuTasks = document.querySelector("#switchTasks");
let menuSubs = document.querySelector("#switchSubs");
let menuHelp = document.querySelector("#switchHelp");

headerFetch = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Referer': null
}

function getRow(size1, size2) {
    let row = document.createElement("div");
    row.setAttribute("class", "row");

    let col1 = document.createElement("div");
    col1.setAttribute("class", "col-" + size1);

    let col2 = document.createElement("div");
    col2.setAttribute("class", "col-" + size2);

    row.appendChild(col1);
    row.appendChild(col2);

    return {
        row: row,
        col1: col1,
        col2: col2
    };
}

async function switchMenu() {
    const attributeClass = "class";
    const activeValue = "list-group-item list-group-item-action active"
    const passiveValue = "list-group-item list-group-item-action";

    menuProfile.onclick = function () {
        update(attributeClass, passiveValue);
        menuProfile.setAttribute(attributeClass, activeValue);
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