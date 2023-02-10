async function createUser() {
    $('#new-user-sub').click(async () =>  {
        let addUserForm = $('#new-user-form')
        let username = addUserForm.find('#new-user-name').val().trim();
        let password = addUserForm.find('#new-user-password').val().trim();
        let email = addUserForm.find('#new-user-email').val().trim();
        let checkedRoles = () => {
            let array = []
            let options = document.querySelector('#new-user-role').options
            for (let i = 0; i < options.length; i++) {
                if (options[i].selected) {
                    array.push(roleList[i])
                }
            }
            return array;
        }
        let data = {
            username: username,
            password: password,
            email: email,
            roles: checkedRoles()
        }

        await userFetch.addNewUser(data);

        addUserForm.find('#new-user-name').val('');
        addUserForm.find('#new-user-password').val('');
        addUserForm.find('#new-user-email').val('');
        addUserForm.find(checkedRoles()).val('');
    });
}