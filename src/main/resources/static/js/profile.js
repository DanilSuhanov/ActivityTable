async function profileLoad() {
    await fetch(`api/user`)
        .then(res => res.json())
        .then(user => {
            document.querySelector("#cardUsername").textContent = user.username;
            document.querySelector("#cardSubordinates")
                .textContent = "Исполнители: " + user.subordinates.map(sub => " " + sub.username);
        });
}