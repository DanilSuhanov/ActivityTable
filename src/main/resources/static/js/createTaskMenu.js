async function createTaskMenu(colContent) {
    let form = createSimpleForm("Название задачи", "Создать задачу");
    let button = form.button;

    let desc = document.createElement("input");
    desc.setAttribute("type", "text");
    desc.setAttribute("class", "form-control");
    desc.setAttribute("placeholder", "Описание задачи");

    let deadline = document.createElement("input");
    deadline.setAttribute("type", "date");
    deadline.setAttribute("class", "form-control");
    deadline.setAttribute("placeholder", "Дата дедлайна");

    form.container.removeChild(form.button);
    form.container.appendChild(desc);
    form.container.appendChild(deadline);
    form.container.appendChild(button);

    form.button.onclick = async function() {
        if (form.input.value !== ""
            && desc.value !== "") {

            let data = {
                title: form.input.value,
                description: desc.value,
                completeness: 0,
                deadline: deadline.value
            };
            await fetch('api/tasks/create', {
                method: 'POST',
                headers: headerFetch,
                body: JSON.stringify(data)
            });

            form.input.value = "";
            desc.value = "";

            addNotice(getNotice("success", "Задача создана!"), colContent);
        } else {
            addNotice(getNotice("danger", "Одно из полей пусто!"), colContent);
        }
    };

    colContent.appendChild(form.main);
}