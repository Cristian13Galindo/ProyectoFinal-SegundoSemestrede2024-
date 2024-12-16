
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevenir el envío tradicional del formulario

    // Validar el formulario
    if (!validateLogin()) {
        return; // No continuar si la validación falla
    }

    const form = event.target;
    const formData = new FormData(form);
    const data = {
        action: formData.get('action'),
        username: formData.get('username'),
        password: formData.get('password')
    };

    // Crear una instancia de XMLHttpRequest
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/ProyectoFinal_war_exploded/users', true); // URL completa
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

    // Manejar la respuesta del servidor
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) { // Petición completa
            const messageDiv = document.getElementById('message');
            if (xhr.status === 200 || xhr.status === 401 || xhr.status === 404) { // Éxito o errores de autenticación
                try {
                    const response = JSON.parse(xhr.responseText);
                    if(response.status === 'success') {
                        // Mostrar mensaje de éxito
                        messageDiv.className = 'alert alert-success';
                        messageDiv.textContent = response.message || 'Inicio de sesión exitoso.';
                        messageDiv.classList.remove('d-none');
                        // Almacenar el nombre de usuario en localStorage
                        localStorage.setItem('username', response.username);
                        // Redirigir al dashboard
                        setTimeout(() => {
                            window.location.href = 'dashboard.html';
                        }, 2000); // 2 segundos
                    } else {
                        // Mostrar mensaje de error
                        messageDiv.className = 'alert alert-danger';
                        messageDiv.textContent = response.message || 'Error al iniciar sesión.';
                        messageDiv.classList.remove('d-none');
                    }
                } catch (e) {
                    // Manejar errores de parseo
                    messageDiv.className = 'alert alert-danger';
                    messageDiv.textContent = 'Respuesta del servidor inválida.';
                    messageDiv.classList.remove('d-none');
                }
            } else { // Otros errores (por ejemplo, 500)
                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    messageDiv.className = 'alert alert-danger';
                    messageDiv.textContent = errorResponse.message || 'Error al iniciar sesión.';
                    messageDiv.classList.remove('d-none');
                } catch (e) {
                    messageDiv.className = 'alert alert-danger';
                    messageDiv.textContent = 'Error al iniciar sesión.';
                    messageDiv.classList.remove('d-none');
                }
            }
        }
    };

    // Enviar la petición con los datos en formato JSON
    xhr.send(JSON.stringify(data));
});
