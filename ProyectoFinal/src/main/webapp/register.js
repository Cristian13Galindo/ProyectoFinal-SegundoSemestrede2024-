
document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevenir el envío tradicional del formulario

    const form = event.target;
    const formData = new FormData(form);
    const data = {
        action: formData.get('action'),
        username: formData.get('username'),
        email: formData.get('email'),
        password: formData.get('password')
    };

    // Validar los datos antes de enviar (opcional, ya que tienes validation.js)
    if (!validateRegister()) {
        return;
    }

    // Crear una instancia de XMLHttpRequest
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/ProyectoFinal_war_exploded/users', true); // URL completa
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

    // Manejar la respuesta del servidor
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) { // Petición completa
            const messageDiv = document.getElementById('message');
            if (xhr.status === 200 || xhr.status === 409) { // Éxito o conflicto (usuario existente)
                try {
                    const response = JSON.parse(xhr.responseText);
                    if(response.status === 'success') {
                        // Mostrar mensaje de éxito
                        messageDiv.className = 'alert alert-success';
                        messageDiv.textContent = response.message || 'Registro exitoso. Puedes iniciar sesión ahora.';
                        messageDiv.classList.remove('d-none');
                        // Opcional: Redirigir al usuario después de unos segundos
                        setTimeout(() => {
                            window.location.href = 'login.html';
                        }, 3000); // 3 segundos
                        // Resetear el formulario
                        form.reset();
                    } else {
                        // Mostrar mensaje de error
                        messageDiv.className = 'alert alert-danger';
                        messageDiv.textContent = response.message || 'Error al registrar el usuario.';
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
                    messageDiv.textContent = errorResponse.message || 'Error al registrar el usuario.';
                    messageDiv.classList.remove('d-none');
                } catch (e) {
                    messageDiv.className = 'alert alert-danger';
                    messageDiv.textContent = 'Error al registrar el usuario.';
                    messageDiv.classList.remove('d-none');
                }
            }
        }
    };

    // Enviar la petición con los datos en formato JSON
    xhr.send(JSON.stringify(data));
});
// Validación para el formulario de registro
function validateRegister() {
    const username = document.getElementById('reg_username').value.trim();
    const email = document.getElementById('reg_email').value.trim();
    const password = document.getElementById('reg_password').value.trim();

    if(username === '' || email === '' || password === '') {
        alert('Por favor, completa todos los campos.');
        return false;
    }

    // Validar formato de correo electrónico
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if(!emailPattern.test(email)) {
        alert('Por favor, introduce un correo electrónico válido.');
        return false;
    }

    // Validar longitud de la contraseña
    if(password.length < 6) {
        alert('La contraseña debe tener al menos 6 caracteres.');
        return false;
    }

    return true;
}