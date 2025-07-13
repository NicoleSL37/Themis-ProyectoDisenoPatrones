document.addEventListener('DOMContentLoaded', () => {
    // Asegurarse de que todos los elementos existan antes de intentar usarlos.
    // Los nombres de las variables se mantienen como estaban, pero los IDs buscan lo que está en tu HTML.
    const tipoDenunciaSelect = document.getElementById('tipoDenuncia');
    const camposPersonaReal = document.getElementById('camposPersonaReal');
    const formConsultaDenuncia = document.getElementById('formConsultaDenuncia');
    
    // Estos son los elementos que causaban el error 'null' si no estaban presentes.
    // Ahora los obtenemos y verificamos su existencia antes de manipularlos.
    const resultadoConsultaDiv = document.getElementById('resultadoConsulta');
    const mensajeErrorConsultaP = document.getElementById('mensajeErrorConsulta'); 

    const tipoDocumentoInput = document.getElementById('tipoDocumento');
    const numeroDocumentoInput = document.getElementById('numeroDocumento');
    const codigoDenunciaInput = document.getElementById('codigoDenuncia');

    // Inicializar el estado de los campos de Persona Real (ocultos por defecto)
    // Se verifica que 'camposPersonaReal' exista antes de manipularlo.
    if (camposPersonaReal) { 
        camposPersonaReal.classList.add('hidden');
        // Se verifica que los inputs existan antes de modificar sus atributos.
        if (tipoDocumentoInput) tipoDocumentoInput.removeAttribute('required');
        if (numeroDocumentoInput) numeroDocumentoInput.removeAttribute('required');
    }


    // Manejar el cambio en el selector de Tipo de Denuncia
    // Se verifica que 'tipoDenunciaSelect' exista antes de añadir el event listener.
    if (tipoDenunciaSelect) {
        tipoDenunciaSelect.addEventListener('change', () => {
            if (tipoDenunciaSelect.value === 'personaReal') {
                if (camposPersonaReal) camposPersonaReal.classList.remove('hidden');
                if (tipoDocumentoInput) tipoDocumentoInput.setAttribute('required', 'true');
                if (numeroDocumentoInput) numeroDocumentoInput.setAttribute('required', 'true');
            } else {
                if (camposPersonaReal) camposPersonaReal.classList.add('hidden');
                if (tipoDocumentoInput) tipoDocumentoInput.removeAttribute('required');
                if (numeroDocumentoInput) numeroDocumentoInput.removeAttribute('required');
                if (tipoDocumentoInput) tipoDocumentoInput.value = '';
                if (numeroDocumentoInput) numeroDocumentoInput.value = '';
            }
            // Ocultar resultados previos y mensajes de error al cambiar el tipo de denuncia
            // Se verifica que 'resultadoConsultaDiv' y 'mensajeErrorConsultaP' existan.
            if (resultadoConsultaDiv) resultadoConsultaDiv.classList.add('hidden');
            if (mensajeErrorConsultaP) mensajeErrorConsultaP.textContent = '';
        });
    }

    // Manejar el envío del formulario de consulta
    // Se verifica que 'formConsultaDenuncia' exista antes de añadir el event listener.
    if (formConsultaDenuncia) {
        formConsultaDenuncia.addEventListener('submit', async (event) => {
            event.preventDefault();

            // Limpiar y ocultar resultados/mensajes anteriores
            // Se verifica que 'resultadoConsultaDiv' y 'mensajeErrorConsultaP' existan.
            if (resultadoConsultaDiv) resultadoConsultaDiv.classList.add('hidden');
            if (mensajeErrorConsultaP) mensajeErrorConsultaP.textContent = '';

            // Se verifica que los inputs existan antes de acceder a sus valores.
            const tipoDenuncia = tipoDenunciaSelect ? tipoDenunciaSelect.value : '';
            const codigoDenuncia = codigoDenunciaInput ? codigoDenunciaInput.value : '';
            const tipoDocumento = tipoDocumentoInput ? tipoDocumentoInput.value : '';
            const numeroDocumento = numeroDocumentoInput ? numeroDocumentoInput.value : '';

            // Validaciones en el frontend
            if (!tipoDenuncia) {
                if (mensajeErrorConsultaP) mensajeErrorConsultaP.textContent = 'Por favor, seleccione un tipo de denuncia.';
                if (resultadoConsultaDiv) resultadoConsultaDiv.classList.remove('hidden');
                return;
            }
            if (!codigoDenuncia) {
                if (mensajeErrorConsultaP) mensajeErrorConsultaP.textContent = 'Por favor, ingrese el código de denuncia.';
                if (resultadoConsultaDiv) resultadoConsultaDiv.classList.remove('hidden');
                return;
            }

            if (tipoDenuncia === 'personaReal' && (!tipoDocumento || !numeroDocumento)) {
                if (mensajeErrorConsultaP) mensajeErrorConsultaP.textContent = 'Para Denuncia de Persona Real, complete el Tipo y Número de Documento.';
                if (resultadoConsultaDiv) resultadoConsultaDiv.classList.remove('hidden');
                return;
            }

            let url = `http://localhost:8080/api/consultar-denuncia?tipoDenuncia=${tipoDenuncia}&codigoDenuncia=${codigoDenuncia}`;

            if (tipoDenuncia === 'personaReal') {
                url += `&tipoDocumento=${encodeURIComponent(tipoDocumento)}&numeroDocumento=${encodeURIComponent(numeroDocumento)}`;
            }


            console.log("URL de consulta generada:", url);

            try {
                const response = await fetch(url);
                const data = await response.json();

                if (response.ok) { // Si la respuesta HTTP es 2xx (éxito)
                    sessionStorage.setItem('denunciaConsultada', JSON.stringify(data));
                    sessionStorage.setItem('tipoDenunciaConsultada', tipoDenuncia);

                    if (tipoDenuncia === 'anonima') {
                        window.location.href = 'estadoDenunciaAnonima.html';
                    } else if (tipoDenuncia === 'personaReal') {
                        window.location.href = 'estadoDenunciaPersonaReal.html';
                    }

                } else { // Si la respuesta HTTP es un error (ej. 400, 404, 500)
                    if (mensajeErrorConsultaP) mensajeErrorConsultaP.textContent = data.mensaje || 'Error al consultar la denuncia. Por favor, intente de nuevo.';
                    if (resultadoConsultaDiv) resultadoConsultaDiv.classList.remove('hidden');
                }
            } catch (error) {
                console.error('Error de red o al procesar la solicitud:', error);
                if (mensajeErrorConsultaP) mensajeErrorConsultaP.textContent = 'Error de conexión con el servidor. Asegúrese de que el backend esté funcionando y sea accesible.';
                if (resultadoConsultaDiv) resultadoConsultaDiv.classList.remove('hidden');
            }
        });
    }
});