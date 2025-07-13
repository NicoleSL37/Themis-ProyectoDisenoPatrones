document.addEventListener('DOMContentLoaded', () => {
    const btnAbrirModalTipoDenuncia = document.getElementById('btn-abrir-modal-denuncia');
    const modalBackdropTipoDenuncia = document.getElementById('modal-backdrop-denuncia');
    const btnCerrarModalTipoDenuncia = document.getElementById('btn-cerrar-modal-denuncia');
    const btnIniciarDenunciaPersonaReal = document.getElementById('btn-iniciar-persona-real'); 
    const btnIniciarDenunciaAnonimo = document.getElementById('btn-iniciar-anonimo'); // ASUME ESTE ID PARA EL BOTÓN ANÓNIMO EN TU MODAL
    // Si tienes otros botones de denuncia (ej. Anónimo), asegúrate de que también tengan IDs únicos y se manejen aquí.

    // --- Funciones para abrir/cerrar el modal de selección ---
    function openModalTipoDenuncia() {
        if (modalBackdropTipoDenuncia) {
            modalBackdropTipoDenuncia.classList.add('active');
            document.body.style.overflow = 'hidden'; // Evita el scroll del fondo
        }
    }

    function closeModalTipoDenuncia() {
        if (modalBackdropTipoDenuncia) {
            modalBackdropTipoDenuncia.classList.remove('active');
            document.body.style.overflow = ''; // Restaura el scroll del fondo
        }
    }

    // --- Asignación de Event Listeners para el modal de selección ---
    if (btnAbrirModalTipoDenuncia) {
        btnAbrirModalTipoDenuncia.addEventListener('click', openModalTipoDenuncia);
    } else {
        console.error('Error: El botón "Iniciar Denuncia" (tarjeta principal) con ID "btn-abrir-modal-denuncia" no fue encontrado.');
    }

    if (btnCerrarModalTipoDenuncia) {
        btnCerrarModalTipoDenuncia.addEventListener('click', closeModalTipoDenuncia);
    } else {
        console.error('Error: El botón "Cerrar Modal" con ID "btn-cerrar-modal-denuncia" no fue encontrado.');
    }

    if (modalBackdropTipoDenuncia) {
        modalBackdropTipoDenuncia.addEventListener('click', (event) => {
            if (event.target === modalBackdropTipoDenuncia) {
                closeModalTipoDenuncia();
            }
        });
    } else {
        console.error('Error: El backdrop del modal con ID "modal-backdrop-denuncia" no fue encontrado.');
    }

    // --- Event Listener para el botón "Iniciar Denuncia (Persona Real)" dentro del modal ---
    if (btnIniciarDenunciaPersonaReal) {
        btnIniciarDenunciaPersonaReal.addEventListener('click', (event) => {
            event.preventDefault(); 
            console.log('Botón "Iniciar Denuncia (Persona Real)" clicado. Redirigiendo...');
            closeModalTipoDenuncia(); 
            // IMPORTANTE: Asegúrate de que esta ruta sea la correcta para tu archivo personaReal.html
            window.location.href = '/html/personaReal.html'; // Asegúrate de que esta ruta es la que usas
        });
    } else {
        console.error('Error: El botón "Iniciar Denuncia (Persona Real)" con ID "btn-iniciar-persona-real" NO FUE ENCONTRADO en el DOM.');
    }
    // --- NUEVO Event Listener para el botón "Iniciar Denuncia (Anónimo)" dentro del modal ---
    if (btnIniciarDenunciaAnonimo) {
        btnIniciarDenunciaAnonimo.addEventListener('click', (event) => {
            event.preventDefault();
            console.log('Botón "Iniciar Denuncia (Anónimo)" clicado. Redirigiendo...');
            closeModalTipoDenuncia();
            window.location.href = '/html/denunciaAnonimo.html'; // <--- RUTA A LA NUEVA PÁGINA ANÓNIMA
        });
    } else {
        console.error('Error: El botón "Iniciar Denuncia (Anónimo)" con ID "btn-iniciar-anonimo" NO FUE ENCONTRADO en el DOM.');
    }
});