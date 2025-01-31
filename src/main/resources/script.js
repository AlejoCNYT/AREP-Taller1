document.addEventListener("DOMContentLoaded", function() {

    window.loadGetMsg = function() {
        let nameVar = document.getElementById("name").value;
        fetch(`/hello?name=${nameVar}`)
            .then(response => response.text())
            .then(data => {
                document.getElementById("getrespmsg").innerHTML = data;
            })
            .catch(error => console.error("Error en GET:", error));
    };

    window.loadPostMsg = function() {
        let name = document.getElementById("postname").value;
        fetch("/hellopost", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `name=${encodeURIComponent(name)}`
        })
        .then(response => response.text())
        .then(data => {
            document.getElementById("postrespmsg").innerHTML = data.message;
        })
        .catch(error => console.error("Error en POST:", error));
    };
});

document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector("form[action='/style.css']");
    form.addEventListener("submit", function(event) {
        event.preventDefault(); // Evita que el formulario se envíe de forma tradicional

        fetch("/style.css")
            .then(response => response.text())
            .then(data => {
                document.getElementById("getrespmsg").innerText = data;
            })
            .catch(error => console.error("Error:", error));
    });
});
