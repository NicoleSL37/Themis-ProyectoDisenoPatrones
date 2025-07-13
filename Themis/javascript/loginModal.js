document.addEventListener('DOMContentLoaded', () => {
    // Obtener referencias a los elementos del modal de login
    const btnAbrirModalLogin = document.getElementById('btn-abrir-modal-login');
    const modalBackdropLogin = document.getElementById('modal-backdrop-login');
    const btnCerrarModalLogin = document.getElementById('btn-cerrar-modal-login');

    // Elementos para el ojo de ver/ocultar contraseña
    const togglePassword = document.querySelector('.modal-content .toggle-password');
    const loginForm = document.getElementById('login-form');
    const emailInput = document.getElementById('email-login');
    const passwordInput = document.getElementById('password-login');

    // Aquí se corrige la forma de obtener passwordError
    // passwordError debe ser el span de error, no el icono de ojo
    const emailError = emailInput ? emailInput.nextElementSibling : null;
    // Si togglePassword existe, passwordError es el siguiente hermano después de togglePassword.
    // Si no existe (si el ojo no está en el HTML), es el nextElementSibling del passwordInput.
    const passwordFormGroup = passwordInput ? passwordInput.closest('.form-group') : null;
    // Ahora, busca el span.error-message dentro de ese formGroup padre
    const passwordError = passwordFormGroup ? passwordFormGroup.querySelector('.error-message') : null;


    // Función para mostrar el mensaje de error personalizado
    function showCustomError(inputElement, errorMessageSpan, message) {
        if (errorMessageSpan && inputElement) {
            errorMessageSpan.textContent = message;
            errorMessageSpan.classList.add('active'); // Muestra el mensaje
            inputElement.classList.add('invalid'); // Marca el input como inválido para CSS
        }
    }

    // Función para ocultar el mensaje de error personalizado
    function hideCustomError(inputElement, errorMessageSpan) {
        if (errorMessageSpan && inputElement) {
            errorMessageSpan.textContent = '';
            errorMessageSpan.classList.remove('active'); // Oculta el mensaje
            inputElement.classList.remove('invalid'); // Quita la marca de inválido del input
        }
    }

    // *** NUEVA FUNCIÓN PARA RESETEAR EL FORMULARIO ***
    function resetLoginForm() {
        if (loginForm) {
            // Limpiar los valores de los inputs
            loginForm.reset();

            // Ocultar los mensajes de error y quitar las clases 'invalid'
            if (emailInput && emailError) {
                hideCustomError(emailInput, emailError);
            }
            if (passwordInput && passwordError) {
                hideCustomError(passwordInput, passwordError);
            }

            // Revertir el ícono de la contraseña a 'visibility_off' y tipo a 'password'
            if (togglePassword && passwordInput) {
                togglePassword.textContent = 'visibility_off';
                passwordInput.setAttribute('type', 'password');
            }
        }
    }

    // Función para abrir el modal de login
    if (btnAbrirModalLogin) {
        btnAbrirModalLogin.addEventListener('click', () => {
            if (modalBackdropLogin) {
                // Al abrir el modal, lo reseteamos para que se vea limpio
                resetLoginForm(); // Llama a la función aquí
                modalBackdropLogin.classList.add('active'); // Usamos 'flex' para centrarlo
            }
        });
    }

    // Función para cerrar el modal de login
    if (btnCerrarModalLogin) {
        btnCerrarModalLogin.addEventListener('click', () => {
            if (modalBackdropLogin) {
                modalBackdropLogin.classList.remove('active');
                // Al cerrar el modal, lo reseteamos también
                resetLoginForm(); // Llama a la función aquí
            }
        });
    }

    // Cerrar el modal de login haciendo clic fuera de su contenido
    if (modalBackdropLogin) {
        modalBackdropLogin.addEventListener('click', (e) => {
            if (e.target === modalBackdropLogin) {
                modalBackdropLogin.classList.remove('active');
                // Al cerrar con el backdrop, lo reseteamos
                resetLoginForm(); // Llama a la función aquí
            }
        });
    }

    // Lógica para ver/ocultar contraseña
    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function() {
            // Alternar el tipo de input entre 'password' y 'text'
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);

            // Cambiar el ícono del ojo
            if (this.textContent === 'visibility_off') {
                this.textContent = 'visibility';
            } else {
                this.textContent = 'visibility_off';
            }
        });
    }

    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            // Prevenir el envío por defecto para manejar la validación manualmente
            event.preventDefault();

            let formIsValid = true;

            // Validar Correo electrónico
            if (emailInput && emailError) {
                if (emailInput.value.trim() === '') { // .trim() quita espacios en blanco
                    showCustomError(emailInput, emailError, 'El correo electrónico es obligatorio.');
                    formIsValid = false;
                } else if (!emailInput.validity.valid) { // Valida formato de email si el navegador tiene soporte
                    showCustomError(emailInput, emailError, 'Por favor, introduce un correo electrónico válido.');
                    formIsValid = false;
                } else {
                    hideCustomError(emailInput, emailError);
                }
            }


            // Validar Contraseña
            if (passwordInput && passwordError) {
                if (passwordInput.value.trim() === '') {
                    showCustomError(passwordInput, passwordError, 'La contraseña es obligatoria.');
                    formIsValid = false;
                } else if (passwordInput.value.length < 6) { // Ejemplo: mínimo 6 caracteres
                    showCustomError(passwordInput, passwordError, 'La contraseña debe tener al menos 6 caracteres.');
                    formIsValid = false;
                } else {
                    hideCustomError(passwordInput, passwordError);
                }
            }


            // Si el formulario es válido, puedes enviarlo programáticamente o hacer un fetch/AJAX
            if (formIsValid) {
                // Aquí podrías enviar el formulario de forma asíncrona (AJAX/Fetch API)
                // O simplemente this.submit() para el envío normal (si quieres que procesar_login.php lo maneje)
                console.log('Formulario de login válido. Enviando...');
                this.submit(); // Envía el formulario si todo es correcto
            }
        });

        // Opcional: Validar al escribir (on input) para retroalimentación instantánea
        if (emailInput && emailError) {
            emailInput.addEventListener('input', () => {
                if (emailInput.value.trim() !== '' && emailInput.validity.valid) {
                    hideCustomError(emailInput, emailError);
                }
            });
        }

        if (passwordInput && passwordError) {
            passwordInput.addEventListener('input', () => {
                if (passwordInput.value.trim() !== '' && passwordInput.value.length >= 6) { // Ajusta la longitud mínima
                    hideCustomError(passwordInput, passwordError);
                }
            });
        }
    }
});