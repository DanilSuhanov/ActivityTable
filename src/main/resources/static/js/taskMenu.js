function menuTaskLoad(colContent) {

    colContent.innerHTML = `<ul class="list-group list-group-numbered" id="listContent"></ul>`;
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

                head.onclick = function () {



                }
            })
        });
}