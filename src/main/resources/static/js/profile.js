async function profileLoad() {
    colContent.innerHTML = `<div class="card">
                    <div class="row g-0">
                        <div class="col">
                            <div class="card-body">
                                <h5 class="card-title" id="cardUsername"></h5>
                                <p class="card-text" id="cardSubordinates"></p>
                                <p id="cardText" class="card-text">
                                    <button id="editProfileButton" type="button" class="btn btn-primary">Изменить ифнормацию</button>
                                    <button id="implementButton" type="button" class="btn btn-primary">Исполнители</button>
                                </p>
                            </div>
                        </div>
                    </div>
                  </div>`;

    let editProfileButton = document.querySelector("#editProfileButton");
    let implemetButton = document.querySelector("#implementButton");

    let cardUsername = document.querySelector("#cardUsername");
    let cardImplement = document.querySelector("#cardSubordinates");

    let user = await (await fetch(`api/user`)).json();

    cardUsername.textContent = user.username;
    cardImplement.textContent = "Исполнители: " + user.subordinates.map(sub => " " + sub.username);

    implemetButton.onclick = async function() {
        await implementMenu();
    }
}


async function implementMenu() {
    colContent.innerHTML = "";
    let list = await getListGroup();

    colContent.appendChild(list);

    await addImplementersToList(list);
}

async function addImplementersToList(list) {
    let implementers = await (await fetch('api/user/getAllImp')).json();

    implementers.forEach(imp => {
        let li = document.createElement("li");
        li.setAttribute("class", "list-group-item");

        let row = getRow(10, 2);
        li.appendChild(row.row);

        let deleteImpButton = document.createElement("button");
        deleteImpButton.setAttribute("class", "btn btn-danger w-100");
        deleteImpButton.textContent = "Удалить";

        let text = document.createElement("div");
        text.textContent = imp.username;

        deleteImpButton.onclick = async function() {
            await deleteImpl(imp);
            list.removeChild(li);
        };

        row.col1.appendChild(text);
        row.col2.appendChild(deleteImpButton);

        list.appendChild(li);
    });
}

async function deleteImpl(imp) {
    await fetch('api/user/implement/delete', {
        method: 'POST',
        headers: headerFetch,
        body: imp.id
    });
}

async function getListGroup() {
    let list = document.createElement("ul");
    list.setAttribute("class", "list-group");

    return list;
}
