function addUser() {
    // Obtener los valores de los campos del formulario
    let nameUser = document.getElementById("input-nameUser").value.trim();
    let password = document.getElementById("input-password").value.trim();
    
    // Validar los campos
    if (!nameUser || !password) {
        alert("Por favor, completa todos los campos.");
        return;
    }

    // Crear el objeto con los datos del nuevo usuario
    let userData = {
        nameUser: nameUser,
        password: password
    };

    // URL del servicio para agregar un nuevo usuario
    let url = 'http://localhost:8080/Library/rest/ManagementUser/createUser';

    // Enviar la solicitud al servidor
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ocurrió un error en la respuesta del servidor: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        alert("Se agregó el usuario exitosamente.");
        window.location.href = "./dashboard.html"; // Redirigir al dashboard
    })
    .catch(error => {
        console.error('Ocurrió el siguiente error con la operación:', error);
    });
}
