// src/main/webapp/js/validation.js

function validateLogin() {
    const usernameInput = document.getElementById('login_username');
    const passwordInput = document.getElementById('login_password');

    if (!usernameInput || !passwordInput) {
        console.error("Elementos de entrada no encontrados en el DOM.");
        return false;
    }

    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    if (username === "" || password === "") {
        showMessage("Por favor, rellena todos los campos.", "danger");
        return false;
    }

    // Puedes añadir más validaciones según tus necesidades

    return true;
}

/**
 * Función para mostrar mensajes al usuario.
 * @param {string} message - El mensaje a mostrar.
 * @param {string} type - El tipo de mensaje ('success', 'danger', etc.).
 */
function showMessage(message, type) {
    const messageDiv = document.getElementById('message');
    if (messageDiv) {
        messageDiv.className = `alert alert-${type}`;
        messageDiv.textContent = message;
        messageDiv.classList.remove('d-none');
    }
}
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