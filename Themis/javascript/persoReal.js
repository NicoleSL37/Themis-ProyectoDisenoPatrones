document.addEventListener('DOMContentLoaded', () => {
    // --- Referencias a elementos del DOM ---
    const btnContinuarPersonal = document.getElementById('btn-continuar-personal');
    const btnAtrasIncidente = document.getElementById('btn-atras-incidente');
    const btnRegistrarDenuncia = document.getElementById('btn-registrar-denuncia'); // <-- Ya tienes esta referencia

    const sectionDatosPersonales = document.getElementById('section-datos-personales');
    const sectionDetallesIncidente = document.getElementById('section-detalles-incidente');

    // Campos de Datos Personales
    const nombresInput = document.getElementById('nombres');
    const apellidoPaternoInput = document.getElementById('apellido-paterno');
    const apellidoMaternoInput = document.getElementById('apellido-materno');

    const hiddenTipoDocumentoSelect = document.getElementById('tipo-documento'); // Este es el <select> original oculto
    const customSelectWrapper = document.querySelector('.custom-select-wrapper'); // Contenedor del select simulado
    const customSelectTrigger = document.querySelector('.custom-select-trigger'); // El div visible que se clica
    const selectedValueSpan = document.querySelector('.selected-value'); // El <span> que muestra el valor seleccionado
    const customOptionsList = document.querySelector('.custom-options'); // La lista <ul> de opciones


    const numeroDocumentoInput = document.getElementById('numero-documento');
    const sexoInput = document.getElementById('sexo');
    const fechaNacimientoInput = document.getElementById('fecha-nacimiento');
    const fechaEmisionInput = document.getElementById('fecha-emision');
    const fechaEmisionGroup = document.getElementById('fecha-emision-group'); // El div contenedor de fecha de emisión
    const numeroCelularInput = document.getElementById('numero-celular');
    const correoElectronicoInput = document.getElementById('correo-electronico');
    const autorizoDatosCheckbox = document.getElementById('autorizo-datos');

    // Campos de Detalles del Incidente
    const fechaIncidenteInput = document.getElementById('fecha-incidente');
    const horaIncidenteInput = document.getElementById('hora-incidente');
    const ahoraIncidenteCheckbox = document.getElementById('ahora-incidente');
    const departamentoSelect = document.getElementById('departamento');
    const provinciaSelect = document.getElementById('provincia');
    const distritoSelect = document.getElementById('distrito');
    const descripcionHechosTextarea = document.getElementById('descripcion-hechos');

    // Referencias para la carga de archivos
    const fileUploadArea = document.getElementById('file-upload-area');
    const archivosEvidenciaInput = document.getElementById('archivos-evidencia');
    const fileList = document.getElementById('file-list');

    const denunciaSuccessModal = document.getElementById('denunciaSuccessModal');
    const denunciaSuccessCloseButton = document.querySelector('.denuncia-success-close-button');
    const modalOkButton = document.getElementById('modal-ok-button');
    const codigoDenunciaDisplay = document.getElementById('copiarCodigoModal');

    // --- Variables globales para datos ---
    let ubigeoData = []; // Almacenará los datos de ubigeo cargados del JSON

    // --- Funciones auxiliares ---

    // Función para mostrar una sección y ocultar las demás
    function showSection(sectionToShow) {
        const allSections = document.querySelectorAll('.form-section');
        allSections.forEach(section => {
            section.classList.remove('active');
            section.style.display = 'none';
        });

        sectionToShow.classList.add('active');
        sectionToShow.style.display = 'flex'; // O 'block' si la sección no es un contenedor flex
        window.scrollTo({ top: 0, behavior: 'smooth' }); // Desplazar al inicio de la página para la nueva sección
    }

    // Función para validar campos obligatorios
    function validateRequiredFields(fields) {
        for (const field of fields) {
            // Manejo especial para el select oculto
            if (field === hiddenTipoDocumentoSelect) {
                if (!field.value) {
                    // Si el custom select no tiene un valor seleccionado
                    // Podrías añadir un feedback visual aquí para el custom select
                    return false;
                }
            } else if (field.type === 'checkbox') {
                if (!field.checked) {
                    return false;
                }
            } else if (field.tagName === 'SELECT') { // Para selects normales (ubigeo, sexo)
                if (!field.value) {
                    return false;
                }
            } else { // Para inputs de texto, fecha, etc.
                if (!field.value.trim()) {
                    return false;
                }
            }
        }
        return true;
    }

    async function loadUbigeoData() {
        try {
            // Asegúrate de que esta ruta sea correcta para tu archivo ubigeo.json
            const response = await fetch('/javascript/ubigeo.json');
            ubigeoData = await response.json();
            populateDepartamentos(); // Llenar el select de departamentos al cargar
            console.log('Ubigeo.json cargado con éxito');
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

    if (customSelectWrapper && customSelectTrigger && selectedValueSpan && customOptionsList && hiddenTipoDocumentoSelect) {
        if (hiddenTipoDocumentoSelect.value === 'DNI') {
            fechaEmisionGroup.style.display = 'flex'; // Usamos 'flex' si tu contenedor es flex, si no, 'block'
            fechaEmisionInput.setAttribute('required', 'required');
        } else {
            fechaEmisionGroup.style.display = 'none';
            fechaEmisionInput.removeAttribute('required');
            fechaEmisionInput.value = ''; // Limpia el valor si no es DNI
        }

        // Evento para abrir/cerrar el custom select
        customSelectTrigger.addEventListener('click', () => {
            customSelectWrapper.classList.toggle('active');
            customOptionsList.style.display = customSelectWrapper.classList.contains('active') ? 'block' : 'none';
        });

        // Evento para seleccionar una opción en el custom select
        customOptionsList.querySelectorAll('li').forEach(option => {
            option.addEventListener('click', () => {
                const value = option.dataset.value; // Obtiene el valor de data-value
                const text = option.textContent;     // Obtiene el texto de la opción

                selectedValueSpan.textContent = text;     // Actualiza el texto visible del custom select
                hiddenTipoDocumentoSelect.value = value; // Actualiza el valor del SELECT REAL OCULTO

                customSelectWrapper.classList.remove('active');
                customOptionsList.style.display = 'none';

                // --- Lógica CLAVE: Habilitar/Deshabilitar Fecha de Emisión del DNI ---
                if (value === 'DNI') {
                    fechaEmisionGroup.style.display = 'flex'; // Muestra el contenedor
                    fechaEmisionInput.setAttribute('required', 'required'); // Hace el campo requerido
                } else {
                    fechaEmisionGroup.style.display = 'none'; // Oculta el contenedor
                    fechaEmisionInput.value = ''; // Limpia el valor si se oculta o cambia a otro tipo
                    fechaEmisionInput.removeAttribute('required'); // Deja de ser requerido
                }
            });
        });

        // Cerrar custom select si se hace clic fuera
        document.addEventListener('click', (e) => {
            if (!customSelectWrapper.contains(e.target) && !customSelectTrigger.contains(e.target)) {
                customSelectWrapper.classList.remove('active');
                customOptionsList.style.display = 'none';
            }
        });
    }
    if (btnContinuarPersonal) {
        btnContinuarPersonal.addEventListener('click', (event) => {
            // --- VALIDACIÓN DE LA PRIMERA SECCIÓN (Datos Personales) ---
            const fieldsToValidatePersonal = [
                nombresInput,
                apellidoPaternoInput,
                apellidoMaternoInput,
                hiddenTipoDocumentoSelect, // Usar el select oculto para la validación
                numeroDocumentoInput,
                sexoInput,
                fechaNacimientoInput,
                autorizoDatosCheckbox
            ];

            if (!validateRequiredFields(fieldsToValidatePersonal)) {
                alert('Por favor, rellena todos los campos obligatorios de "Datos Personales".');
                return;
            }

            // Validación específica para DNI y fecha de emisión (usando el valor del select oculto)
            if (hiddenTipoDocumentoSelect.value === 'DNI' && !fechaEmisionInput.value.trim()) {
                alert('Por favor, introduce la Fecha de Emisión del DNI.');
                return;
            }

            if (!autorizoDatosCheckbox.checked) {
                alert('Debes autorizar el tratamiento de tus datos para continuar.');
                return;
            }

            // Si todas las validaciones pasan, avanza a la siguiente sección
            showSection(sectionDetallesIncidente);
        });
    }

    // Listener para el botón "Atrás" de la segunda sección
    if (btnAtrasIncidente) {
        btnAtrasIncidente.addEventListener('click', () => {
            showSection(sectionDatosPersonales);
        });
    }

    // Listener para el botón "Registrar Denuncia" (último paso)
    if (btnRegistrarDenuncia) {
        btnRegistrarDenuncia.addEventListener('click', async (event) => {
            event.preventDefault();

            // --- VALIDACIÓN DE LA SEGUNDA SECCIÓN (Detalles del Incidente y Descripción/Pruebas) ---
            const fieldsToValidateIncidente = [
                departamentoSelect,
                provinciaSelect,
                distritoSelect,
                descripcionHechosTextarea
            ];

            if (!ahoraIncidenteCheckbox.checked && (!fechaIncidenteInput.value.trim() || !horaIncidenteInput.value.trim())) {
                alert('Por favor, introduce la Fecha y Hora del Incidente o marca "Ahora".');
                return;
            }
            if (!validateRequiredFields(fieldsToValidateIncidente)) {
                alert('Por favor, selecciona el Departamento, Provincia y Distrito e ingresa la descripción de los hechos.');
                return;
            }

            // *** INSERTAR AQUÍ: Deshabilitar el botón antes de iniciar el envío ***
            btnRegistrarDenuncia.disabled = true;
            // Opcional: btnRegistrarDenuncia.textContent = 'Enviando...'; // Puedes cambiar el texto del botón

            console.log('Formulario de Denuncia de Persona Real listo para enviar. Preparando FormData...');

            // --- Recolección de datos y envío al servidor ---
            const formData = new FormData();

            // Datos de la sección "Datos Personales"
            formData.append('nombres', nombresInput.value.trim());
            formData.append('apellidoPaterno', apellidoPaternoInput.value.trim());
            formData.append('apellidoMaterno', apellidoMaternoInput.value.trim());
            formData.append('tipoDocumento', hiddenTipoDocumentoSelect.value); // Usa el valor del select oculto
            formData.append('numeroDocumento', numeroDocumentoInput.value.trim());
            formData.append('sexo', sexoInput.value.trim());

            let fechaNacimientoFormatted = '';
            if (fechaNacimientoInput && fechaNacimientoInput.value.trim()) {
                const [year, month, day] = fechaNacimientoInput.value.trim().split('-'); // Divide 'YYYY-MM-DD'
                fechaNacimientoFormatted = `${day}/${month}/${year}`; // Recompone a 'DD/MM/YYYY'
            }
            formData.append('fechaNacimiento', fechaNacimientoFormatted);

            // Convertir fechaEmision a DD/MM/YYYY (solo si existe y es DNI)
            let fechaEmisionFormatted = '';
            // Asegurarse de que el campo de fecha de emisión tiene un valor y que el tipo de documento es DNI
            if (hiddenTipoDocumentoSelect.value === 'DNI' && fechaEmisionInput && fechaEmisionInput.value.trim()) {
                const [yearE, monthE, dayE] = fechaEmisionInput.value.trim().split('-'); // Divide 'YYYY-MM-DD'
                fechaEmisionFormatted = `${dayE}/${monthE}/${yearE}`; // Recompone a 'DD/MM/YYYY'
            }
            formData.append('fechaEmision', fechaEmisionFormatted); // Puede ser vacío, el backend lo maneja

            // Campos opcionales
            formData.append('numeroCelular', numeroCelularInput.value.trim());
            formData.append('correoElectronico', correoElectronicoInput.value.trim());

            formData.append('autorizoDatos', autorizoDatosCheckbox.checked);

            // Datos de la sección "Detalles del Incidente"
            formData.append('esAhora', ahoraIncidenteCheckbox.checked);

            let fechaIncidenteFormatted = '';
            if (fechaIncidenteInput && fechaIncidenteInput.value.trim()) {
                const [yearI, monthI, dayI] = fechaIncidenteInput.value.trim().split('-');
                fechaIncidenteFormatted = `${dayI}/${monthI}/${yearI}`;
            }
            formData.append('fechaIncidente', fechaIncidenteFormatted);
            formData.append('horaIncidente', horaIncidenteInput.value.trim());

            formData.append('departamento', departamentoSelect.value);
            formData.append('provincia', provinciaSelect.value);
            formData.append('distrito', distritoSelect.value);
            formData.append('descripcionHechos', descripcionHechosTextarea.value.trim());

            // Archivos de evidencia
            const files = archivosEvidenciaInput.files;
            for (let i = 0; i < files.length; i++) {
                formData.append('archivosEvidencia', files[i]); // 'archivosEvidencia' debe coincidir con el @RequestParam en tu backend
            }

            // **MUY IMPORTANTE: Esta URL debe coincidir con el @RequestMapping de tu controlador de Spring Boot**
            const backendUrl = 'http://localhost:8080/api/denuncias-personas-reales';

            console.log('Contenido de FormData antes del envío:');
            for (let pair of formData.entries()) {
                console.log(pair[0] + ': ' + pair[1]);
            }
            console.log('--- Fin de FormData ---');

            try {
                const response = await fetch(backendUrl, {
                    method: 'POST',
                    body: formData // No es necesario Content-Type para FormData
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('Error HTTP al registrar la denuncia:', response.status, errorText);
                    throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
                }

                const data = await response.json();
                console.log('Denuncia de Persona Real registrada:', data);
                if (denunciaSuccessModal) {
                    denunciaSuccessModal.classList.add('active'); // Muestra el modal añadiendo la clase 'active'
                }
                if (codigoDenunciaDisplay && data.codigo) { // Asegúrate de que 'data.codigo' es el nombre correcto
                        codigoDenunciaDisplay.textContent = data.codigo;
                    }

                let redirectionTimeout; // Variable para almacenar el timeout

                const redirectToMainPage = () => {
                    clearTimeout(redirectionTimeout); // Limpia cualquier timeout anterior
                    // Oculta el modal antes de redirigir (opcional, pero buena práctica)
                    if (denunciaSuccessModal) {
                        denunciaSuccessModal.classList.remove('active');
                    }
                    window.location.href = '/html/paginaPrincipal.html'; // Ajusta esta ruta si es necesario
                };
                if (denunciaSuccessCloseButton) {
                    denunciaSuccessCloseButton.onclick = redirectToMainPage;
                }
                if (modalOkButton) {
                    modalOkButton.onclick = redirectToMainPage;
                }

                // Opcional: Redirigir automáticamente después de unos segundos si no hacen clic
                redirectionTimeout = setTimeout(redirectToMainPage, 60000);

                if (codigoDenunciaDisplay) {
                    codigoDenunciaDisplay.addEventListener('click', () => {
                        const codigo = codigoDenunciaDisplay.textContent; // Obtiene el texto actual del código

        // Usa la API del portapapeles moderna
                    navigator.clipboard.writeText(codigo).then(() => {
                        alert('¡Código copiado al portapapeles!');
                        console.log('Código copiado:', codigo);
                    }).catch(err => {
                        console.error('Error al copiar el código:', err);
                        alert('No se pudo copiar el código. Por favor, cópialo manualmente.');
                    });
                });
            }

            } catch (error) {
                console.error('Error al registrar la denuncia de Persona Real:', error);
                alert('Hubo un error al registrar tu denuncia. Por favor, inténtalo de nuevo.');
            } finally {
                // *** INSERTAR AQUÍ: Re-habilitar el botón cuando la solicitud finalice (éxito o error) ***
                btnRegistrarDenuncia.disabled = false;
                btnRegistrarDenuncia.textContent = 'Registrar Denuncia';
                // Opcional: btnRegistrarDenuncia.textContent = 'Registrar Denuncia'; // Restaura el texto del botón
            }
        });
    }

    // Lógica para el checkbox "Ahora:" (deshabilita Fecha y Hora si está marcado)
    if (ahoraIncidenteCheckbox && fechaIncidenteInput && horaIncidenteInput) {
        ahoraIncidenteCheckbox.addEventListener('change', () => {
            if (ahoraIncidenteCheckbox.checked) {
                const now = new Date();
                const year = now.getFullYear();
                const month = String(now.getMonth() + 1).padStart(2, '0');
                const day = String(now.getDate()).padStart(2, '0');
                const date = `${year}-${month}-${day}`;

                const hours = String(now.getHours()).padStart(2, '0');
                const minutes = String(now.getMinutes()).padStart(2, '0');
                const time = `${hours}:${minutes}`;

                fechaIncidenteInput.disabled = true;
                fechaIncidenteInput.value = date;
                horaIncidenteInput.disabled = true;
                horaIncidenteInput.value = time;
            } else {
                fechaIncidenteInput.disabled = false;
                fechaIncidenteInput.value = '';
                horaIncidenteInput.disabled = false;
                horaIncidenteInput.value = '';
            }
        });
    }

    // --- Lógica para la carga de archivos (Drag & Drop y Click) ---
    if (fileUploadArea && archivosEvidenciaInput && fileList) {
        // Al hacer click en el área de carga, activa el input de archivo
        fileUploadArea.addEventListener('click', () => {
            archivosEvidenciaInput.click();
        });

        // Manejo de la selección de archivos (cuando se hace click o drag & drop)
        archivosEvidenciaInput.addEventListener('change', (event) => {
            handleFiles(event.target.files);
        });

        // Prevenir el comportamiento por defecto de arrastrar y soltar para permitir la carga
        ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
            fileUploadArea.addEventListener(eventName, preventDefaults, false);
            document.body.addEventListener(eventName, preventDefaults, false); // También en el body para evitar que el navegador abra el archivo
        });

        // Resaltar el área de carga cuando un archivo es arrastrado sobre ella
        ['dragenter', 'dragover'].forEach(eventName => {
            fileUploadArea.addEventListener(eventName, highlight, false);
        });

        // Remover el resaltado cuando el archivo sale del área
        ['dragleave', 'drop'].forEach(eventName => {
            fileUploadArea.addEventListener(eventName, unhighlight, false);
        });

        // Manejar el evento de "drop" (cuando se suelta un archivo)
        fileUploadArea.addEventListener('drop', handleDrop, false);

        function preventDefaults(e) {
            e.preventDefault();
            e.stopPropagation();
        }

        function highlight() {
            fileUploadArea.classList.add('highlight');
        }

        function unhighlight() {
            fileUploadArea.classList.remove('highlight');
        }

        function handleDrop(e) {
            const dt = e.dataTransfer;
            const files = dt.files;
            archivosEvidenciaInput.files = files; // Asigna los archivos al input real
            handleFiles(files);
        }

        function handleFiles(files) {
            fileList.innerHTML = ''; // Limpiar lista anterior
            if (files.length > 0) {
                for (let i = 0; i < files.length; i++) {
                    const file = files[i];
                    const listItem = document.createElement('div');
                    listItem.textContent = file.name;
                    fileList.appendChild(listItem);
                }
            } else {
                fileList.textContent = 'Ningún archivo seleccionado.'; // Mensaje si no hay archivos
            }
        }
    }
});