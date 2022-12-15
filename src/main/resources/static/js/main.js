const userFetch = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findUserByUsername: async () => await fetch(`api/user`)
}

async function header() {
    let temp = '';
    const info = document.querySelector('#user-info');
    await userFetch.findUserByUsername()
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

header();