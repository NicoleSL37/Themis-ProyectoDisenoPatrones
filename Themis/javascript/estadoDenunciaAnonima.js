document.addEventListener('DOMContentLoaded', () => {
    const denunciaConsultada = JSON.parse(sessionStorage.getItem('denunciaConsultada'));
    const mensajeErrorDiv = document.getElementById('mensajeErrorAnonima'); // Un elemento para errores específicos de esta página
    const detallesContainer = document.getElementById('resultadoDenuncia'); // Contenedor para los detalles de la denuncia

    // Elementos donde se mostrará la información de la denuncia anónima
    const estadoDenunciaEl = document.getElementById('estadoDenuncia');
    const codigoDenunciaEl = document.getElementById('codigoResultado');
    const tipoDenunciaEl = document.getElementById('tipoResultado');
    const fechaRegistroEl = document.getElementById('fechaRegistro');
    const descripcionDenunciaEl = document.getElementById('descripcionDenuncia');
    const archivosAdjuntosList = document.getElementById('archivosAdjuntosList');

    // Limpiar sessionStorage una vez que los datos se recuperan (opcional, pero buena práctica)
    sessionStorage.removeItem('denunciaConsultada');
    sessionStorage.removeItem('tipoDenunciaConsultada');

    if (!denunciaConsultada) {
        // No hay datos de la denuncia o hubo un error en la consulta anterior
        if (mensajeErrorDiv) {
            mensajeErrorDiv.textContent = 'No se encontraron datos para la denuncia anónima. Por favor, realice una nueva consulta.';
            mensajeErrorDiv.classList.remove('hidden');
        }
        if (detallesContainer) {
            detallesContainer.classList.add('hidden'); // Oculta el contenedor de detalles
        }
        return;
    }

    // Si hay datos, ocultar mensaje de error y mostrar el contenedor de detalles
    if (mensajeErrorDiv) {
        mensajeErrorDiv.classList.add('hidden');
    }
    if (detallesContainer) {
        detallesContainer.classList.remove('hidden');
    }

    // Rellenar los campos con la información de la denuncia anónima
    if (estadoDenunciaEl) estadoDenunciaEl.textContent = denunciaConsultada.estado || 'N/A';
    if (codigoDenunciaEl) codigoDenunciaEl.textContent = denunciaConsultada.codigoDenuncia || 'N/A';
    if (tipoDenunciaEl) tipoDenunciaEl.textContent = denunciaConsultada.tipo || 'Anónima'; // Puedes forzar "Anónima" si el tipo no viene del backend
    if (fechaRegistroEl) fechaRegistroEl.textContent = denunciaConsultada.fechaRegistro || 'N/A';
    if (descripcionDenunciaEl) descripcionDenunciaEl.textContent = denunciaConsultada.descripcionHechos || 'Sin descripción.';

    // Manejo de archivos adjuntos para denuncia anónima
    if (archivosAdjuntosList && denunciaConsultada.rutasArchivos && denunciaConsultada.rutasArchivos .length > 0) {
        archivosAdjuntosList.innerHTML = ''; // Limpiar lista
        const urlBase = "http://localhost:8080/api/denuncias-anonimas/archivo/"; 

    denunciaConsultada.rutasArchivos.forEach(rutaArchivo => { 
        const li = document.createElement('li');
        const a = document.createElement('a'); 

        // NO USAR encodeURIComponent aquí. La ruta debe pasarse tal cual al backend.
        a.href = urlBase + rutaArchivo; 

        const nombreArchivo = rutaArchivo.substring(rutaArchivo.lastIndexOf('/') + 1);
        a.textContent = nombreArchivo;
        a.target = '_blank'; 
        li.appendChild(a);
        archivosAdjuntosList.appendChild(li);
    });
    } else if (archivosAdjuntosList) {
        archivosAdjuntosList.innerHTML = '<li>No hay archivos adjuntos.</li>';
    }
});