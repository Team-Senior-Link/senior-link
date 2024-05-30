function toggleEdit() {
    var elementsToToggle = [
        "title", "introduction", "career", "specialty", "location", "timeFree"
    ];
    var toggleButton = document.getElementById("toggleButton");

    elementsToToggle.forEach(function (id) {
        var displayElement = document.getElementById(id + "Display");
        var editElement = document.getElementById(id + "Edit");
        displayElement.classList.toggle("hidden");
        editElement.classList.toggle("hidden");
    });

    if (toggleButton.innerText === "Edit") {
        toggleButton.innerText = "Save";
    } else {
        saveData();
        toggleButton.innerText = "Edit";
    }
}

function saveData() {
    var elementsToSave = [
        "title", "introduction", "career", "specialty", "location", "timeFree"
    ];

    elementsToSave.forEach(function (id) {
        var inputElement = document.getElementById(id + "Input").value;
        var displayElement = document.getElementById(id + "Display");
        displayElement.innerText = inputElement;
    });

}

