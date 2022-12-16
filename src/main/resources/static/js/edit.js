async function editPost() {
    document.querySelector('#editButton').onclick = async function () {

        const modal = document.querySelector('#modal');

        let checkedRoles = () => {
            let array = []
            let options = document.querySelector('#roles').options
            for (let i = 0; i < options.length; i++) {
                if (options[i].selected) {
                    array.push(roleList[i])
                }
            }
            return array;
        }

        let userId = modal.querySelector('#userId').value;
        let username = modal.querySelector('#username').value;
        let password = modal.querySelector('#password').value;
        let email = modal.querySelector('#email').value;
        let data = {
            userId: userId,
            username: username,
            password: password,
            email: email,
            roles: checkedRoles()
        };

        await fetch(`api/users/${userId}`, {method: 'PUT', headers: userFetch.head, body: JSON.stringify(data)})
        await table();
    }
}