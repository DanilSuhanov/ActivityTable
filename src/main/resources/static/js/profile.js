function createCard(colContent) {
    colContent.innerHTML = `<div class="card">
                    <div class="row g-0">
                        <div class="col">
                            <div class="card-body">
                                <h5 class="card-title" id="cardUsername"></h5>
                                <p class="card-text" id="cardSubordinates"></p>
                                <p class="card-text"><button type="button" class="btn btn-primary">Изменить ифнормацию</button></p>
                            </div>
                        </div>
                    </div>
                  </div>`;
}

async function profileLoad() {
    await fetch(`api/user`)
        .then(res => res.json())
        .then(user => {
            document.querySelector("#cardUsername").textContent = user.username;
            document.querySelector("#cardSubordinates")
                .textContent = "Исполнители: " + user.subordinates.map(sub => " " + sub.username);
        });
}