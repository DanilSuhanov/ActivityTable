async function deletePost() {
    document.querySelector('#deleteButton').onclick = async function () {

        const modal = document.querySelector('#modal');

        let userId = modal.querySelector('#userId').value;

        await fetch(`api/users/${userId}`, {method: 'DELETE', headers: userFetch.head})
        await table();
    }
}