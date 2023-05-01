async function importantNotice(text, to) {
    let data = {
        content: text,
        to: to
    }

    await fetch('api/telegram/addNotification', {
        method: 'POST',
        headers: headerFetch,
        body: JSON.stringify(data)
    });
}
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
        container: container,
        button: button,
        input: input
    };
}

function getLi() {
    let li = document.createElement("li");
    li.setAttribute("class", "list-group-item");

    return li;
}

function setListOnColContent() {
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

function getCheckBox(content) {
    let container = document.createElement("div");
    container.setAttribute("class", "form-check form-switch");

    let input = document.createElement("input");
    input.setAttribute("class", "form-check-input");
    input.setAttribute("type", "checkbox");
    input.setAttribute("role", "switch");
    input.setAttribute("id", "switchFor" + content);
    container.appendChild(input);

    let label = document.createElement("label");
    label.setAttribute("class", "form-check-label");
    label.setAttribute("for", "switchFor" + content);
    label.textContent = content;
    container.appendChild(label);

    return {
        container: container,
        input: input,
        label: label
    };
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

function getRowCol3(size1, size2, size3) {
    let row = document.createElement("div");
    row.setAttribute("class", "row");

    let col1 = document.createElement("div");
    col1.setAttribute("class", "col-" + size1);

    let col2 = document.createElement("div");
    col2.setAttribute("class", "col-" + size2);

    let col3 = document.createElement("div");
    col3.setAttribute("class", "col-" + size3);

    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);

    return {
        row: row,
        col1: col1,
        col2: col2,
        col3: col3
    };
}

function getRange(value) {
    let list = getList();

    let li = getLi();
    list.appendChild(li);

    let label = document.createElement("label");
    label.setAttribute("class", "form-label");
    label.setAttribute("for", "range" + count);
    label.textContent = "Завершённость задачи - " + value + "%";
    li.appendChild(label);

    let input = document.createElement("input");
    input.setAttribute("class", "form-range");
    input.setAttribute("type", "range");
    input.setAttribute("min", "0");
    input.setAttribute("max", "100");
    input.setAttribute("step", "5");
    input.setAttribute("id", "range" + count);
    input.value = value;
    li.appendChild(input);

    count = count + 1;

    return {
        container: list,
        label: label,
        input: input
    };
}