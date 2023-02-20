function getList() {
    let list = document.createElement("ul");
    list.setAttribute("class", "list-group");

    return list;
}

function createSimpleForm(placeholder, buttonText) {
    let form = document.createElement("form");
    let container = document.createElement("div");
    container.setAttribute("class", "mb-3");
    let input = document.createElement("input");
    input.setAttribute("type", "text");
    input.setAttribute("class", "form-control");
    input.setAttribute("placeholder", placeholder);
    container.appendChild(input);
    form.appendChild(container);
    let button = document.createElement("div");
    button.setAttribute("class", "btn btn-success w-100");
    button.textContent = buttonText;
    container.appendChild(button);


    return {
        main: form,
        button: button,
        input: input
    };
}

function getLi() {
    let li = document.createElement("li");
    li.setAttribute("class", "list-group-item");

    return li;
}

function setListOnColumn() {
    colContent.innerHTML = "";
    let list = getList();
    colContent.appendChild(list);

    return list;
}

function getNotice(type, text) {
    let notice = document.createElement("div");
    notice.setAttribute("class", "alert alert-" + type);
    notice.textContent = text;

    return notice;
}

function addNotice(notice, tag) {
    tag.appendChild(notice);
    notice.onclick = function () {
        tag.removeChild(notice);
    };
}