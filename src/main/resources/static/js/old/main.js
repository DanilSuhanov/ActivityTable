let roleList = [
    {id: 1, authority: "ROLE_USER"},
    {id: 2, authority: "ROLE_ADMIN"}
]

const userFetch = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findUser: async () => await fetch(`api/user`),
    findAllUsers: async () => await fetch('api/users'),
    addNewUser: async (user) => await fetch('api/users', {method: 'POST', headers: userFetch.head, body: JSON.stringify(user)}),
    findOneUser: async (id) => await fetch(`api/users/${id}`)
}

async function header() {
    let temp = '';
    const info = document.querySelector('#user-info');
    await userFetch.findUser()
        .then(res => res.json())
        .then(user => {
            temp += `
             <span id="user-info" class="navbar-brand" style="color:#f5f5f5">
               ${user.username} with roles <span>${user.roles.map(role => " " + role.authority.slice(5))}</span>
            </span>
            `;
        });
    info.innerHTML = temp;
}

async function userTable() {
    const table = document.querySelector('#user-table');
    const tableBody = table.querySelector('#user-table-body');
    const row = await userFetch.findUser().then(res => res.json());

    tableBody.innerHTML = "";

    const rowElement = document.createElement("tr");
    let values = Object.values(row);

    for (let i = 0; i < 4; i++) {
        const cellElement = document.createElement("td");

        cellElement.textContent = values[i];
        rowElement.appendChild(cellElement);
    }

    const cellRoles = document.createElement("td");
    for (const role of values[4]) {
        cellRoles.textContent += (role.authority.slice(5) + " ");
    }
    rowElement.appendChild(cellRoles);

    tableBody.appendChild(rowElement);
}

async function table() {
    const table = document.querySelector('#admin-table');
    const tableBody = table.querySelector('tbody');
    const rows = await userFetch.findAllUsers().then(res => res.json());

    tableBody.innerHTML = "";

    for (const row of rows) {
        const rowElement = document.createElement("tr");
        let values = Object.values(row);

        for (let i = 0; i < 4; i++) {
            const cellElement = document.createElement("td");

            cellElement.textContent = values[i];
            rowElement.appendChild(cellElement);
        }

        const cellRoles = document.createElement("td");
        for (const role of values[4]) {
            cellRoles.textContent += (role.authority.slice(5) + " ");
        }
        rowElement.appendChild(cellRoles);

        const editTd = document.createElement("td");
        const editButton = document.createElement("button");
        editButton.setAttribute("class", "btn btn-primary");
        editButton.setAttribute("data-bs-toggle", "modal");
        editButton.setAttribute("data-bs-target", "#modal");
        editButton.setAttribute("data-userid", values[0]);
        editButton.textContent = "Edit";
        editButton.onclick = function () {
            editModal(values[0]);
        }
        editTd.appendChild(editButton);
        rowElement.appendChild(editTd);

        const deleteTd = document.createElement("td");
        const deleteButton = document.createElement("button");
        deleteButton.setAttribute("class", "btn btn-danger");
        deleteButton.setAttribute("data-bs-toggle", "modal");
        deleteButton.setAttribute("data-bs-target", "#modal");
        deleteButton.setAttribute("data-userid", values[0]);
        deleteButton.textContent = "Delete";
        deleteButton.onclick = function () {
            deleteModal(values[0]);
        }
        deleteTd.appendChild(deleteButton);
        rowElement.appendChild(deleteTd);

        tableBody.appendChild(rowElement);
    }
}

async function editModal(userId) {
    await userFetch.findOneUser(userId)
        .then(res => res.json())
        .then(user => {
            document.querySelector('#mod-con').innerHTML = `<div class="modal-header">
                    <h5 class="modal-title" id="editModalLabel">Edit user</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
                </div>
                <form>
                    <div class="modal-body text-center mx-auto" style="width: 50%">

                        <div class="mb-3">
                            <label for="userId"><b>ID</b></label>
                            <input class="form-control" type="number" value="${user.id}" id="userId" name="id" placeholder="ID" readonly>
                        </div>

                        <div class="mb-3">
                            <label for="username"><b>Name</b></label>
                            <input class="form-control" type="text" value="${user.username}" id="username" name="name" placeholder="Name">
                        </div>

                        <div class="mb-3">
                            <label for="password"><b>Password</b></label>
                            <input class="form-control" type="password" value="${user.password}" id="password" name="password" placeholder="Password">
                        </div>

                        <div class="mb-3">
                            <label for="email"><b>Email</b></label>
                            <input class="form-control" type="email" value="${user.email}" id="email" name="email" placeholder="Email">
                        </div>

                        <div class="mb-3">
                            <label for="roles"><b>Role</b></label>
                            <br>
                            <select style="width: 100%" id="roles" class="form-control select" multiple size="2" name="role">
                                <option value="ROLE_USER">User</option>
                                <option value="ROLE_ADMIN">Admin</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <div id="editButton" class="btn btn btn-success">Edit</div>
                    </div>
                </form>`;
            editPost();
        })
}

async function deleteModal(userId) {
    await userFetch.findOneUser(userId)
        .then(res => res.json())
        .then(user => {
            document.querySelector('#mod-con').innerHTML = `<div class="modal-header">
                    <h5 class="modal-title" id="deleteModalLabel">Delete user</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
                </div>
                <form>
                    <div class="modal-body text-center mx-auto" style="width: 50%">

                        <div class="mb-3">
                            <label for="userId"><b>ID</b></label>
                            <input class="form-control" type="number" value="${user.id}" id="userId" name="id" placeholder="ID" readonly>
                        </div>

                        <div class="mb-3">
                            <label for="delete_name"><b>Name</b></label>
                            <input class="form-control" type="text" value="${user.username}" id="delete_name" name="name" placeholder="Name" readonly>
                        </div>

                        <div class="mb-3">
                            <label for="delete_password"><b>Password</b></label>
                            <input class="form-control" type="password" value="${user.password}" id="delete_password" name="password" placeholder="Password" readonly>
                        </div>

                        <div class="mb-3">
                            <label for="delete_email"><b>Email</b></label>
                            <input class="form-control" type="email" value="${user.email}" id="delete_email" name="email" placeholder="Email" readonly>
                        </div>

                        <div class="mb-3">
                            <label for="delete_select"><b>Role</b></label>
                            <br>
                            <select style="width: 100%" id="delete_select" class="form-control select" multiple size="2" name="role" disabled>
                                <option value="ROLE_USER">User</option>
                                <option value="ROLE_ADMIN">Admin</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <div id="deleteButton" class="btn btn-danger">Delete</div>
                    </div>
                </form>`;
            deletePost();
        })
}

async function funcSwitch() {
    const card = document.querySelector("#card");
    const nav1 = document.querySelector("#nav-1");
    const nav2 = document.querySelector("#nav-2");

    document.querySelector("#users-table").onclick = function () {
        nav1.setAttribute("class", "nav-link active");
        nav2.setAttribute("class", "nav-link");
        card.innerHTML = `<h5 class="card-header">
                        All users
                    </h5>
                    <div class="card-body">
                        <blockquote class="blockquote mb-0">
                            <div>
                                <table class="table" id="admin-table">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Password</th>
                                        <th>Email</th>
                                        <th>Role</th>
                                        <th>Edit</th>
                                        <th>Delete</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </blockquote>
                    </div>`;
        table();
        profileSwitch();
    }
    document.querySelector("#new-user").onclick = function () {
        nav1.setAttribute("class", "nav-link");
        nav2.setAttribute("class", "nav-link active");
        card.innerHTML = `<h5 class="card-header">
              Add new user
            </h5>
            <div class="card-body">
              <blockquote class="blockquote mb-0">
                <div class="container d-flex justify-content-center">

                  <form id="new-user-form" class="text-center" style="max-width: 25%;">

                    <div class="mb-3">
                      <label for="new-user-name"><b>Name</b></label>
                      <input class="form-control" type="text" id="new-user-name" name="username" placeholder="Name">
                    </div>

                    <div class="mb-3">
                      <label for="new-user-password"><b>Password</b></label>
                      <input class="form-control" type="password" id="new-user-password" name="password" placeholder="Password">
                    </div>

                    <div class="mb-3">
                      <label for="new-user-email"><b>Email</b></label>
                      <input class="form-control" type="email" id="new-user-email" name="email" placeholder="Email">
                    </div>

                    <div class="mb-3">
                      <label for="new-user-role"><b>Role</b></label>
                      <br>
                      <select multiple style="width: 100%" id="new-user-role" class="form-control select" size="2" name="role">
                        <option value="ROLE_USER">User</option>
                        <option value="ROLE_ADMIN">Admin</option>
                      </select>
                    </div>

                    <div id="new-user-sub" class="btn btn btn-success">Add new user</div>

                  </form>

                </div>
              </blockquote>
            </div>`;
        createUser();
    }
}

async function profileSwitch() {
    const contentCol = document.querySelector('#content-col');
    const adminButton = document.querySelector('#admin-prof');
    const userButton = document.querySelector('#user-prof');

    document.querySelector('#admin-prof').onclick = function () {
        adminButton.setAttribute("class", "btn btn-primary text-light text-left");
        userButton.setAttribute("class", "btn btn-light text-primary text-left");

        contentCol.innerHTML = `<h1 style="margin-top: 10px">Admin panel</h1>
            <ul class="nav nav-tabs">
                    <li class="nav-item" id="users-table">
                        <a id="nav-1" class="nav-link active" aria-current="page">Users table</a>
                    </li>
                    <li class="nav-item" id="new-user">
                        <a id="nav-2" class="nav-link" aria-current="page">New User</a>
                    </li>
                </ul>
            <div class="card" id="card">
                    <h5 class="card-header">
                        All users
                    </h5>
                    <div class="card-body">
                        <blockquote class="blockquote mb-0">
                            <div>
                                <table class="table" id="admin-table">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Password</th>
                                        <th>Email</th>
                                        <th>Role</th>
                                        <th>Edit</th>
                                        <th>Delete</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </blockquote>
                    </div>
                </div>`;
        table();
        funcSwitch();
    }
    document.querySelector('#user-prof').onclick = function () {
        userView();
    }
}

async function userView() {
    document.querySelector('#admin-prof').setAttribute("class", "btn btn-light text-primary text-left");
    document.querySelector('#user-prof').setAttribute("class", "btn btn-primary text-light text-left");

    document.querySelector('#content-col').innerHTML = `<h1 style="margin-top: 10px">User information-page</h1>
            <div class="card">
                <h5 class="card-header">
                    About user
                </h5>
                <div class="card-body">
                    <blockquote class="blockquote mb-0">
                        <div>
                            <table id="user-table" class="table">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Password</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                </tr>
                                </thead>
                                <tbody id="user-table-body">
                                </tbody>
                            </table>
                        </div>
                    </blockquote>
                </div>
            </div>`;
    userTable();
}

async function rightCheck() {
    const user = await userFetch.findUser().then(res => res.json());
    for (const role of user.roles) {
        if (role.authority === "ROLE_ADMIN") {
            return;
        }
    }
    userView();
    document.querySelector('#admin-prof').remove();
}

rightCheck();
header();
table();
funcSwitch();
profileSwitch();