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
    const codigoDenunciaDisplay = document.getElementById('codigoDenunciaDisplay');
    const copiarCodigoButton = document.getElementById('copiarCodigoModal');
    // --- Variables globales para datos ---
    let ubigeoData = []; // Almacenará los datos de ubigeo cargados del JSON

    const redirectToMainPage = () => {
        if(denunciaSuccessModal) {
            denunciaSuccessModal.classList.remove('active');
        }
        window.location.href = '../html/paginaPrincipal.html'; // Redirige a la página principal
    };
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
            const response = await fetch('../javascript/ubigeo.json');
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

    if(fileUploadArea && archivosEvidenciaInput) {
        fileUploadArea.addEventListener('click', () =>  {
            console.log('Click en el área de carga de archivos. Activando input de archivos...');
            archivosEvidenciaInput.click(); // Simula el clic en el input de archivos
        });
    }else {
        console.error('No se encontraron los elementos fileUploadArea o archivosEvidenciaInput. Verifica tus IDs.');
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
    ahoraIncidenteCheckbox.addEventListener('change', () => {
        const isChecked = ahoraIncidenteCheckbox.checked;
        fechaIncidenteInput.disabled = isChecked;
        horaIncidenteInput.disabled = isChecked;
        if (isChecked) {
            fechaIncidenteInput.value = ''; // Limpia el campo si se marca "Ahora"
            horaIncidenteInput.value = ''; // Limpia el campo si se marca "Ahora"
        }
    });

    if(archivosEvidenciaInput) {
        archivosEvidenciaInput.addEventListener('change', (event) => {
            fileList.innerHTML = ''; // Limpiar la lista de archivos
            const files = archivosEvidenciaInput.files;
            if (files.length > 0) {
                for (let i = 0; i < files.length; i++) {
                    const file = files[i];
                    const listItem = document.createElement('li');
                    listItem.textContent = file.name;
                    fileList.appendChild(listItem);
                }
            } else {
                const listItem = document.createElement('li');
                listItem.textContent = 'No se han seleccionado archivos.';
                fileList.appendChild(listItem);
            }
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

            btnRegistrarDenuncia.disabled = true;
            console.log('Formulario de Denuncia de Persona Real listo para enviar. Preparando FormData...');

            const denunciaData = {
                fechaIncidente: ahoraIncidenteCheckbox.checked ? null : fechaIncidenteInput.value.trim(),
                horaIncidente: ahoraIncidenteCheckbox.checked ? null : horaIncidenteInput.value.trim(),
                esAhora: ahoraIncidenteCheckbox.checked,
                departamento: departamentoSelect.value,
                provincia: provinciaSelect.value,
                distrito: distritoSelect.value, // Corregido aquí también
                descripcionHechos: descripcionHechosTextarea.value.trim(),

                nombres: nombresInput.value.trim(),
                apellidoPaterno: apellidoPaternoInput.value.trim(),
                apellidoMaterno: apellidoMaternoInput.value.trim(),
                tipoDocumento: hiddenTipoDocumentoSelect.value,
                numeroDocumento: numeroDocumentoInput.value.trim(),
                sexo: sexoInput.value,
                fechaNacimiento: fechaNacimientoInput.value.trim(),

                fechaEmision: fechaEmisionInput.value.trim() || null, // Envía null si está vacío
                numeroCelular: numeroCelularInput.value.trim() || null, // Envía null si está vacío
                correoElectronico: correoElectronicoInput.value.trim() || null, // Envía null si está vacío
                autorizoDatos: autorizoDatosCheckbox.checked
            }
            // --- Recolección de datos y envío al servidor ---
            const formData = new FormData();

            formData.append('denuncia', JSON.stringify(denunciaData)); // Añade los datos de la denuncia como JSON
            console.log('JSON de datos adjuntado a FormData:', JSON.stringify(denunciaData));

            const archivosFiles = archivosEvidenciaInput.files;
            if (archivosFiles.length > 0) {
                for (let i = 0; i < archivosFiles.length; i++) {
                    formData.append('archivosEvidencia', archivosFiles[i]); // Añade cada
                    console.log(`Archivo añadido: ${archivosFiles[i].name}`);
                }
            } else {
                console.log('No se adjuntaron archivos de evidencia.');
            }
            try {
                const response = await fetch('http://localhost:8080/api/denuncias-personas-reales', {
                    method: 'POST',
                    body: formData // El navegador establecerá el Content-Type: multipart/form-data automáticamente
                });

                if (!response.ok) {
                    const errorData = await response.json();
                    console.error('Error al registrar denuncia (Respuesta NO OK):', errorData);
                    alert(`Error al registrar la denuncia: ${errorData.message || response.statusText}`);
                } else {
                    const responseData = await response.json();
                    console.log('Denuncia registrada con éxito:', responseData);
                    if(codigoDenunciaDisplay) {
                        codigoDenunciaDisplay.textContent = responseData.codigoDenuncia;
                    }
                    if(denunciaSuccessModal){
                        denunciaSuccessModal.classList.add('active');
                    }
                    if(denunciaSuccessCloseButton){
                        denunciaSuccessCloseButton.removeEventListener('click', redirectToMainPage); // Asegúrate de eliminar el listener anterior
                        denunciaSuccessCloseButton.addEventListener('click', redirectToMainPage);
                    }
                    if(modalOkButton) {
                        modalOkButton.removeEventListener('click', redirectToMainPage); // Asegúrate de eliminar el listener anterior
                        modalOkButton.addEventListener('click', redirectToMainPage);
                    }
                }
            } catch (error) {
                console.error('Error de red o en la solicitud:', error);
                alert('Ocurrió un error de conexión o inesperado. Por favor, inténtalo de nuevo.');
            } finally {
                btnRegistrarDenuncia.disabled = false; // Habilitar el botón de nuevo
            }
        });
    }

    if (copiarCodigoButton) {
        copiarCodigoButton.addEventListener('click', () => {
            const codigo = codigoDenunciaDisplay.textContent; // Obtener el texto del strong
            navigator.clipboard.writeText(codigo).then(() => {
                alert('Código copiado al portapapeles: ' + codigo);
            }).catch(err => {
                console.error('Error al copiar el código: ', err);
                alert('No se pudo copiar el código automáticamente. Por favor, cópielo manualmente: ' + codigo);
            });
        });
    }

    if(archivosEvidenciaInput) {
        archivosEvidenciaInput.addEventListener('change', (event) => {
            fileList.innerHTML = ''; // Limpiar la lista de archivos
            const files = archivosEvidenciaInput.files;
            if (files.length > 0) {
                for (let i = 0; i < files.length; i++) {
                    const file = files[i];
                    const listItem = document.createElement('li');
                    listItem.textContent = file.name;
                    fileList.appendChild(listItem);
                }
            } else {
                const listItem = document.createElement('li');
                listItem.textContent = 'No se han seleccionado archivos.';
                fileList.appendChild(listItem);
            }
        });
    }   

    // --- Manejo del Modal de Éxito ---
});
