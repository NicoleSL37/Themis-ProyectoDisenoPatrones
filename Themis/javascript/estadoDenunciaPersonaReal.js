document.addEventListener('DOMContentLoaded', () => {
    const denunciaConsultada = JSON.parse(sessionStorage.getItem('denunciaConsultada'));

    const mensajeErrorDiv = document.getElementById('mensajeErrorPersonaReal'); 
    const detallesContainer = document.getElementById('resultadoDenuncia'); 
    const infoPersonalContainer = document.getElementById('informacionPersonalResultadoSection'); // CAMBIADO: Coincide con el ID de tu <section> en HTML


    // Elementos donde se mostrará la información de la denuncia de persona real
    // AJUSTADO: Los IDs ahora coinciden con tu HTML
    const estadoDenunciaEl = document.getElementById('estadoDenuncia');          // CAMBIADO
    const codigoDenunciaEl = document.getElementById('codigoResultado');        // CAMBIADO
    const tipoDenunciaEl = document.getElementById('tipoResultado');            // CAMBIADO
    const fechaRegistroEl = document.getElementById('fechaRegistro');           // CAMBIADO
    const descripcionDenunciaEl = document.getElementById('descripcionDenuncia'); // CAMBIADO
    const archivosAdjuntosList = document.getElementById('archivosAdjuntosList'); // CAMBIADO

    // Elementos de información personal
    // AJUSTADO: Los IDs ahora coinciden con tu HTML
    const tipoDocumentoEl = document.getElementById('tipoDocumentoResultado');    // CAMBIADO
    const numeroDocumentoEl = document.getElementById('numeroDocumentoResultado'); // CAMBIADO
    const correoEl = document.getElementById('correoResultado');              // CAMBIADO
    const apellidoPaternoEl = document.getElementById('apellidoPaternoResultado'); // CAMBIADO
    const apellidoMaternoEl = document.getElementById('apellidoMaternoResultado'); // CAMBIADO
    const nombreEl = document.getElementById('nombreResultado');              // CAMBIADO

    // Limpiar sessionStorage una vez que los datos se recuperan (opcional, pero buena práctica)
    sessionStorage.removeItem('denunciaConsultada');
    sessionStorage.removeItem('tipoDenunciaConsultada');

    if (!denunciaConsultada) {
        // No hay datos de la denuncia o hubo un error en la consulta anterior
        if (mensajeErrorDiv) { // Solo si el elemento existe en el HTML
            mensajeErrorDiv.textContent = 'No se encontraron datos para la denuncia de Persona Real. Por favor, realice una nueva consulta.';
            mensajeErrorDiv.classList.remove('hidden');
        } else {
            console.warn("Elemento 'mensajeErrorPersonaReal' no encontrado en el HTML. El mensaje de error no se mostrará.");
        }
        
        // Ocultar todos los contenedores de detalles si no hay datos
        if (detallesContainer) {
            detallesContainer.classList.add('hidden');
        }
        if (infoPersonalContainer) {
            infoPersonalContainer.classList.add('hidden');
        }
        return;
    }

    // Si hay datos, ocultar mensaje de error y mostrar los contenedores de detalles
    if (mensajeErrorDiv) {
        mensajeErrorDiv.classList.add('hidden');
    }
    
    // Asegurarse de que los contenedores estén visibles cuando hay datos
    if (detallesContainer) {
        detallesContainer.classList.remove('hidden');
    }
    if (infoPersonalContainer) {
        infoPersonalContainer.classList.remove('hidden');
    }

    // Rellenar los campos con la información de la denuncia de persona real
    // Añadimos comprobaciones `if (elemento)` por si algún ID no se encontrara, para evitar errores
    if (estadoDenunciaEl) estadoDenunciaEl.textContent = denunciaConsultada.estado || 'N/A';
    if (codigoDenunciaEl) codigoDenunciaEl.textContent = denunciaConsultada.codigoDenuncia || 'N/A';
    if (tipoDenunciaEl) tipoDenunciaEl.textContent = denunciaConsultada.tipo || 'Persona Real';
    if (fechaRegistroEl) fechaRegistroEl.textContent = denunciaConsultada.fechaRegistro || 'N/A';
    if (descripcionDenunciaEl) descripcionDenunciaEl.textContent = denunciaConsultada.descripcionHechos || 'Sin descripción.';

    // Rellenar los campos de información personal
    if (tipoDocumentoEl) tipoDocumentoEl.textContent = denunciaConsultada.tipoDocumento || 'N/A';
    if (numeroDocumentoEl) numeroDocumentoEl.textContent = denunciaConsultada.numeroDocumento || 'N/A';
    if (correoEl) correoEl.textContent = denunciaConsultada.correoElectronico || 'N/A';
    if (apellidoPaternoEl) apellidoPaternoEl.textContent = denunciaConsultada.apellidoPaterno || 'N/A';
    if (apellidoMaternoEl) apellidoMaternoEl.textContent = denunciaConsultada.apellidoMaterno || 'N/A';
    if (nombreEl) nombreEl.textContent = denunciaConsultada.nombres || 'N/A';

    // Manejo de archivos adjuntos para denuncia de persona real
    if (archivosAdjuntosList && denunciaConsultada.rutasArchivos && denunciaConsultada.rutasArchivos.length > 0) {
        archivosAdjuntosList.innerHTML = '';
        denunciaConsultada.rutasArchivos.forEach(rutaRelativaArchivo => {
            const li = document.createElement('li');
            const a = document.createElement('a');
            const urlBase = "http://localhost:8080/api/denuncias/descargar-archivos/";

            const nombreArchivoParaBackend = rutaRelativaArchivo;
            a.href = urlBase + nombreArchivoParaBackend;

            const nombreMostrar = nombreArchivoParaBackend;
            a.textContent = nombreMostrar;

            a.target = '_blank';
            li.appendChild(a);
            archivosAdjuntosList.appendChild(li);
        });
    } else if (archivosAdjuntosList) {
        archivosAdjuntosList.innerHTML = '<li>No hay archivos adjuntos.</li>';
    }
});