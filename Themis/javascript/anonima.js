document.addEventListener('DOMContentLoaded', () => {
    // --- Referencias de elementos HTML ---
    // Carga de archivos
    const fileUploadAreaAnonimo = document.getElementById('file-upload-area');
    const archivosEvidenciaInputAnonimo = document.getElementById('archivos-evidencia-anonimo');
    const fileListAnonimo = document.getElementById('file-list');

    // Botón final de envío
    const btnRegistrarDenunciaAnonima = document.getElementById('btn-registrar-denuncia-anonima');

    // Checkbox "Ahora" y campos de Fecha/Hora
    const ahoraCheckboxAnonimo = document.getElementById('ahora-incidente-anonimo');
    const fechaIncidenteInputAnonimo = document.getElementById('fecha-incidente-anonimo');
    const horaIncidenteInputAnonimo = document.getElementById('hora-incidente-anonimo');

    // Campos de Ubicación (Departamento, Provincia, Distrito)
    const departamentoSelect = document.getElementById('departamento-anonimo');
    const provinciaSelect = document.getElementById('provincia-anonimo');
    const distritoSelect = document.getElementById('distrito-anonimo');

    // Campo de Correo y Descripción
    const correoElectronicoAnonimoInput = document.getElementById('correo-electronico-anonimo');
    const descripcionHechosInputAnonimo = document.getElementById('descripcion-hechos-anonimo');

    const denunciaSuccessModal = document.getElementById('denunciaSuccessModal');
    const denunciaSuccessCloseButton = document.querySelector('.denuncia-success-close-button');
    const modalOkButton = document.getElementById('modal-ok-button');

    let ubigeoData = []; // Variable para almacenar los datos del JSON de ubigeo

    // --- Lógica para el checkbox "Ahora:" ---
    if (ahoraCheckboxAnonimo && fechaIncidenteInputAnonimo && horaIncidenteInputAnonimo) {
        ahoraCheckboxAnonimo.addEventListener('change', () => {
            if (ahoraCheckboxAnonimo.checked) {
                const now = new Date();
                // Formatear la fecha a YYYY-MM-DD
                const year = now.getFullYear();
                const month = String(now.getMonth() + 1).padStart(2, '0');
                const day = String(now.getDate()).padStart(2, '0');
                const date = `${year}-${month}-${day}`;

                // Formatear la hora a HH:MM
                const hours = String(now.getHours()).padStart(2, '0');
                const minutes = String(now.getMinutes()).padStart(2, '0');
                const time = `${hours}:${minutes}`;

                fechaIncidenteInputAnonimo.value = date;
                horaIncidenteInputAnonimo.value = time;
                fechaIncidenteInputAnonimo.disabled = true;
                horaIncidenteInputAnonimo.disabled = true;
            } else {
                fechaIncidenteInputAnonimo.disabled = false;
                fechaIncidenteInputAnonimo.value = '';
                horaIncidenteInputAnonimo.disabled = false;
                horaIncidenteInputAnonimo.value = '';
            }
        });
    }

    // --- Lógica para la carga de archivos (Drag & Drop y Click) ---
    if (fileUploadAreaAnonimo && archivosEvidenciaInputAnonimo && fileListAnonimo) {
        fileUploadAreaAnonimo.addEventListener('click', () => {
            archivosEvidenciaInputAnonimo.click();
        });

        archivosEvidenciaInputAnonimo.addEventListener('change', (event) => {
            handleFilesAnonimo(event.target.files);
        });

        ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
            fileUploadAreaAnonimo.addEventListener(eventName, preventDefaults, false);
            document.body.addEventListener(eventName, preventDefaults, false);
        });

        ['dragenter', 'dragover'].forEach(eventName => {
            fileUploadAreaAnonimo.addEventListener(eventName, highlight, false);
        });

        ['dragleave', 'drop'].forEach(eventName => {
            fileUploadAreaAnonimo.addEventListener(eventName, unhighlight, false);
        });

        fileUploadAreaAnonimo.addEventListener('drop', handleDropAnonimo, false);

        function preventDefaults (e) {
            e.preventDefault();
            e.stopPropagation();
        }

        function highlight() {
            fileUploadAreaAnonimo.classList.add('highlight');
        }

        function unhighlight() {
            fileUploadAreaAnonimo.classList.remove('highlight');
        }

        function handleDropAnonimo(e) {
            const dt = e.dataTransfer;
            const files = dt.files;
            archivosEvidenciaInputAnonimo.files = files;
            handleFilesAnonimo(files);
        }

        function handleFilesAnonimo(files) {
            fileListAnonimo.innerHTML = '';
            if (files.length === 0) {
                fileListAnonimo.textContent = 'Ningún archivo seleccionado.';
                return;
            }
            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                const listItem = document.createElement('div');
                listItem.textContent = file.name;
                fileListAnonimo.appendChild(listItem);
            }
        }
    }

    // --- Lógica para la carga de Departamentos, Provincias y Distritos (UBIGEO) ---

    // Función para cargar los datos de ubigeo desde el JSON
    async function loadUbigeoData() {
        try {
            // Asegúrate de que esta ruta sea correcta para tu archivo ubigeo.json
            // Si el archivo JSON está en la misma carpeta que este JS: './ubigeo.json'
            // Si está en 'javascript/data/ubigeo.json' y este JS está en 'javascript/': './data/ubigeo.json'
            const response = await fetch('/javascript/ubigeo.json');
            ubigeoData = await response.json();
            populateDepartamentos(); // Llenar el select de departamentos al cargar
        } catch (error) {
            console.error('Error al cargar los datos de ubigeo:', error);
            alert('Error al cargar la información geográfica. Por favor, recarga la página o verifica el archivo ubigeo.json.');
        }
    }

    // Función para llenar el select de Departamentos
    function populateDepartamentos() {
        departamentoSelect.innerHTML = '<option value="">Seleccione Departamento</option>';
        ubigeoData.forEach(dep => {
            const option = document.createElement('option');
            option.value = dep.departamento;
            option.textContent = dep.departamento;
            departamentoSelect.appendChild(option);
        });
        provinciaSelect.disabled = true;
        distritoSelect.disabled = true;
        provinciaSelect.innerHTML = '<option value="">Seleccione Provincia</option>';
        distritoSelect.innerHTML = '<option value="">Seleccione Distrito</option>';
    }

    // Event Listener para el cambio en Departamento
    if (departamentoSelect) {
        departamentoSelect.addEventListener('change', () => {
            const selectedDepartamento = departamentoSelect.value;
            provinciaSelect.innerHTML = '<option value="">Seleccione Provincia</option>';
            distritoSelect.innerHTML = '<option value="">Seleccione Distrito</option>';
            provinciaSelect.disabled = true;
            distritoSelect.disabled = true;

            if (selectedDepartamento) {
                const departamento = ubigeoData.find(dep => dep.departamento === selectedDepartamento);
                if (departamento) {
                    departamento.provincias.forEach(prov => {
                        const option = document.createElement('option');
                        option.value = prov.provincia;
                        option.textContent = prov.provincia;
                        provinciaSelect.appendChild(option);
                    });
                    provinciaSelect.disabled = false;
                }
            }
        });
    }

    // Event Listener para el cambio en Provincia
    if (provinciaSelect) {
        provinciaSelect.addEventListener('change', () => {
            const selectedDepartamento = departamentoSelect.value;
            const selectedProvincia = provinciaSelect.value;
            distritoSelect.innerHTML = '<option value="">Seleccione Distrito</option>';
            distritoSelect.disabled = true;

            if (selectedDepartamento && selectedProvincia) {
                const departamento = ubigeoData.find(dep => dep.departamento === selectedDepartamento);
                if (departamento) {
                    const provincia = departamento.provincias.find(prov => prov.provincia === selectedProvincia);
                    if (provincia) {
                        provincia.distritos.forEach(dist => {
                            const option = document.createElement('option');
                            option.value = dist;
                            option.textContent = dist;
                            distritoSelect.appendChild(option);
                        });
                        distritoSelect.disabled = false;
                    }
                }
            }
        });
    }

    // Llamada inicial para cargar los datos de ubigeo cuando el DOM esté listo
    loadUbigeoData();

    // --- Listener para el botón "Registrar Denuncia" (Envío al Backend) ---
    if (btnRegistrarDenunciaAnonima) {
        btnRegistrarDenunciaAnonima.addEventListener('click', async (event) => {
            event.preventDefault();

            // --- Obtener valores de los campos para validación y envío ---
            const fechaIncidente = fechaIncidenteInputAnonimo.value.trim();
            const horaIncidente = horaIncidenteInputAnonimo.value.trim();
            const departamento = departamentoSelect.value; // ¡Corregido! Ahora toma el valor del select
            const provincia = provinciaSelect.value;     // ¡Corregido! Ahora toma el valor del select
            const distrito = distritoSelect.value;       // ¡Corregido! Ahora toma el valor del select
            const ahoraChecked = ahoraCheckboxAnonimo.checked;
            const descripcionHechos = descripcionHechosInputAnonimo.value.trim(); // ¡Corregido!
            const correoElectronicoAnonimo = correoElectronicoAnonimoInput.value.trim(); // ¡Corregido!

            // --- VALIDACIONES DEL FORMULARIO ANÓNIMO ---
            if (!ahoraChecked && (!fechaIncidente || !horaIncidente)) {
                alert('Por favor, introduce la Fecha y Hora del Incidente o marca "Ahora".');
                return;
            }
            if (!departamento || !provincia || !distrito) {
                alert('Por favor, selecciona el Departamento, Provincia y Distrito.');
                return;
            }
            if (!descripcionHechos) {
                alert('Por favor, ingresa la descripción de los hechos.');
                return;
            }

            if (correoElectronicoAnonimo && !/\S+@\S+\.\S+/.test(correoElectronicoAnonimo)) {
                alert('Por favor, introduce un formato de correo electrónico válido.');
                return;
            }

            console.log('Formulario de Denuncia Anónima listo para enviar. Preparando FormData...');

            // --- Construir FormData para el envío al backend ---
            const formData = new FormData();
            formData.append('correo', correoElectronicoAnonimo);
            formData.append('fechaIncidente', fechaIncidente);
            formData.append('horaIncidente', horaIncidente);
            formData.append('ahora', ahoraChecked);
            formData.append('departamento', departamento);
            formData.append('provincia', provincia);
            formData.append('distrito', distrito);
            formData.append('descripcionHechos', descripcionHechos);

            // Adjuntar archivos si existen
            if (archivosEvidenciaInputAnonimo.files.length > 0) {
                for (let i = 0; i < archivosEvidenciaInputAnonimo.files.length; i++) {
                    formData.append('archivos', archivosEvidenciaInputAnonimo.files[i]);
                }
            }

            try {
                const backendUrl = 'http://localhost:8080/api/denuncias-anonimas';

                const response = await fetch(backendUrl, {
                    method: 'POST',
                    body: formData
                });

                const result = await response.json();

                if (response.ok) {
                    console.log('Denuncia anónima registrada:', result);

                    if (denunciaSuccessModal) {
                        denunciaSuccessModal.classList.add('active'); // Muestra el modal
                    }

                    let redirectionTimeout; // Variable para almacenar el timeout

                    const redirectToMainPage = () => {
                        clearTimeout(redirectionTimeout); // Limpia cualquier timeout anterior
                        if (denunciaSuccessModal) {
                            denunciaSuccessModal.classList.remove('active'); // Oculta el modal
                        }
                        window.location.href = '/html/paginaPrincipal.html'; // Redirige
                    };

                    // Escuchar el clic en la 'x' o en el botón 'Aceptar' del modal
                    if (denunciaSuccessCloseButton) {
                        denunciaSuccessCloseButton.onclick = redirectToMainPage;
                    }
                    if (modalOkButton) {
                        modalOkButton.onclick = redirectToMainPage;
                    }

                    // Opcional: Redirigir automáticamente después de unos segundos si no hacen clic
                    redirectionTimeout = setTimeout(redirectToMainPage, 5000); 
                    // --- Limpiar el formulario después de un envío exitoso ---
                    correoElectronicoAnonimoInput.value = '';
                    fechaIncidenteInputAnonimo.value = '';
                    horaIncidenteInputAnonimo.value = '';
                    ahoraCheckboxAnonimo.checked = false;
                    fechaIncidenteInputAnonimo.disabled = false;
                    horaIncidenteInputAnonimo.disabled = false;
                    // Restablecer los selects de ubicación
                    populateDepartamentos(); // Vuelve a cargar y restablece los selects de ubicación
                    descripcionHechosInputAnonimo.value = '';
                    archivosEvidenciaInputAnonimo.value = ''; // Limpiar input de tipo file
                    if (fileListAnonimo) {
                    fileListAnonimo.innerHTML = 'Ningún archivo seleccionado.';
                } else {
                    alert('Error al registrar la denuncia: ' + result.message);
                    console.error('Error del servidor:', result);
                }
            }
            } catch (error) {
                console.error('Error de red o conexión al servidor:', error);
                alert('No se pudo conectar con el servidor. Asegúrate de que el backend esté funcionando y la URL sea correcta.');
            }
        });
    } else {
        console.error('Error: El botón "Registrar Denuncia" con ID "btn-registrar-denuncia-anonima" no fue encontrado.');
    }
});