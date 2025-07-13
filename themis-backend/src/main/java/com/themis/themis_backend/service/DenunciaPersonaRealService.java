package com.themis.themis_backend.service;

import com.themis.themis_backend.model.DenunciaPersonaReal;
import com.themis.themis_backend.repository.DenunciaPersonaRealRepository;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DenunciaPersonaRealService {

    private final DenunciaPersonaRealRepository denunciaPersonaRealRepository;
    private final Path rootLocation;

    @Autowired
    public DenunciaPersonaRealService(DenunciaPersonaRealRepository denunciaPersonaRealRepository) {
        this.denunciaPersonaRealRepository = denunciaPersonaRealRepository;
        this.rootLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(this.rootLocation);
                System.out.println("Directorio de carga de archivos inicializado: " + this.rootLocation.toString());

            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el directorio de subida", e);
        }
    }

    @Transactional
    public DenunciaPersonaReal guardarDenuncia(DenunciaPersonaReal denuncia, List<MultipartFile> archivos) throws IOException {
        DenunciaPersonaReal savedDenuncia = denunciaPersonaRealRepository.save(denuncia);

        List<String> rutasGuardadas = new ArrayList<>();
        if (archivos != null && !archivos.isEmpty()) {
            String denunciaCode = savedDenuncia.getCodigoDenuncia();
            Path denunciaFolderPath = rootLocation.resolve("denuncias_real/" + denunciaCode);

            Files.createDirectories(denunciaFolderPath);

            for (MultipartFile archivo : archivos) {
                if (!archivo.isEmpty()) {
                    String originalFilename = archivo.getOriginalFilename();
                    String fileExtension = "";
                    if (originalFilename != null && originalFilename.contains(".")) {
                        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }
                    String fileName = UUID.randomUUID().toString() + fileExtension;
                    Path filePath = denunciaFolderPath.resolve(fileName);

                    Files.copy(archivo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    String relativePath = rootLocation.relativize(filePath).toString().replace("\\", "/");
                    rutasGuardadas.add(relativePath);
                }

            }
        }
        savedDenuncia.setRutasArchivos(rutasGuardadas);
        return denunciaPersonaRealRepository.save(savedDenuncia);
    }

    public Resource loadFileAsResource(String filename) throws FileNotFoundException { // Lanza esta excepción
        System.out.println("\n--- INICIO loadFileAsResource ---");
        System.out.println("DEBUG SERVICE: filename recibido: " + filename);

        String cleanedFilename = filename;
        if (filename.startsWith("/")) {
            cleanedFilename = filename.substring(1);
        }
        System.out.println("DEBUG SERVICE: filename limpiado: " + cleanedFilename);

        Path filePath = this.rootLocation.resolve(cleanedFilename).normalize();

        System.out.println("DEBUG SERVICE: Ruta física construida (filePath): " + filePath.toString());
        System.out.println("DEBUG SERVICE: rootLocation (absoluta y normalizada): " + this.rootLocation.toString());

        // Validacion de seguridad
        if (!filePath.startsWith(this.rootLocation)) {
            System.err.println("ERROR SERVICE: ALERTA DE SEGURIDAD - Intento de acceso a ruta fuera del directorio permitido.");
            System.err.println("ERROR SERVICE: Ruta fuera de bounds: " + filePath.toString());
            System.err.println("ERROR SERVICE: Directorio raíz: " + this.rootLocation.toString());
            throw new FileNotFoundException("Ruta de archivo inválida: Acceso fuera del directorio permitido."); // Cambiado a FileNotFoundException
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                System.out.println("DEBUG SERVICE: Archivo encontrado y es legible: " + filePath.toString());
                System.out.println("--- FIN loadFileAsResource (ÉXITO) ---\n");
                return resource;
            } else {
                System.err.println("ERROR SERVICE: Archivo NO encontrado o NO legible en la ruta: " + filePath.toString());
                System.out.println("--- FIN loadFileAsResource (FALLO) ---\n");
                throw new FileNotFoundException("Archivo no encontrado o no se puede leer: " + filename);
            }
        } catch (MalformedURLException ex) {
            System.err.println("ERROR SERVICE: URL malformada para el archivo: " + filename + " - " + ex.getMessage());
            throw new FileNotFoundException("URL malformada al cargar archivo: " + filename + " - " + ex.getMessage());
        }
    }

    public Optional<DenunciaPersonaReal> findByCodigoDenunciaAndTipoDocumentoAndNumeroDocumento(
            String codigoDenuncia, String tipoDocumento, String numeroDocumento) {
        return denunciaPersonaRealRepository.findByCodigoDenunciaAndTipoDocumentoAndNumeroDocumento(codigoDenuncia, tipoDocumento, numeroDocumento);
    }

    public Optional<DenunciaPersonaReal> findByCodigoDenuncia(String codigoDenuncia) {
        return denunciaPersonaRealRepository.findByCodigoDenuncia(codigoDenuncia);
    }

    public Optional<DenunciaPersonaReal> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento) {
        return denunciaPersonaRealRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);
    }

    public Optional<DenunciaPersonaReal> findByTipoDocumentoAndNumeroDocumentoAndCodigoDenuncia(String tipoDocumento, String numeroDocumento, String codigoDenuncia) {
        return denunciaPersonaRealRepository.findByTipoDocumentoAndNumeroDocumentoAndCodigoDenuncia(tipoDocumento, numeroDocumento, codigoDenuncia);
    }
}
