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