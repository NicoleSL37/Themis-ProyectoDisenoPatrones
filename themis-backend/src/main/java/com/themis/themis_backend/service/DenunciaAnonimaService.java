package com.themis.themis_backend.service;

import com.themis.themis_backend.model.DenunciaAnonima;
import com.themis.themis_backend.repository.DenunciaAnonimaRepository;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DenunciaAnonimaService {
    private static final Logger logger = LoggerFactory.getLogger(DenunciaAnonimaService.class);

    private final DenunciaAnonimaRepository denunciaAnonimaRepository;
    private final Path rootLocation;

    @Autowired
    public DenunciaAnonimaService(DenunciaAnonimaRepository denunciaAnonimaRepository, @Value("${file.upload-dir.anonymous}") String uploadDir) {
        this.denunciaAnonimaRepository = denunciaAnonimaRepository;
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
                logger.info("Directorio de carga de archivos inicializado: " + this.rootLocation.toString());
            }
        } catch (IOException e) {
            logger.error("Error al crear el directorio raíz de subida: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el directorio de subida para DenunciaService", e);
        }
    }

    @Transactional
    public DenunciaAnonima guardarDenuncia(DenunciaAnonima denuncia, List<MultipartFile> files) throws IOException {

        logger.info("\n--- INICIO guardarDenuncia (Service) ---");
        logger.debug("DEBUG SERVICE: Cantidad de archivos recibidos en el servicio: " + (files != null ? files.size() : 0));

        DenunciaAnonima savedDenuncia = denunciaAnonimaRepository.save(denuncia);
        logger.debug("DEBUG SERVICE: Denuncia inicial guardada, ID: " + savedDenuncia.getId());

        List<String> rutasArchivosFinales = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            String denunciaCode = savedDenuncia.getCodigoDenuncia();
            Path denunciaFolderPath = rootLocation.resolve(denunciaCode);

            logger.debug("DEBUG SERVICE: Creando directorio para archivos: " + denunciaFolderPath);
            Files.createDirectories(denunciaFolderPath);

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String fileExtension = "";
                    if (originalFilename != null && originalFilename.contains(".")) {
                        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }
                    String fileName = UUID.randomUUID().toString() + fileExtension;
                    Path filePath = denunciaFolderPath.resolve(fileName);

                    logger.debug("DEBUG SERVICE: Guardando archivo en: " + filePath.toAbsolutePath());
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    String relativePath = denunciaCode + "/" + fileName;
                    rutasArchivosFinales.add(relativePath);
                    logger.debug("DEBUG SERVICE: Ruta relativa generada: " + relativePath);
                }
            }
        }

        savedDenuncia.setRutasArchivos(rutasArchivosFinales);
        logger.debug("DEBUG SERVICE: Rutas asignadas al objeto Denuncia: " + savedDenuncia.getRutasArchivos());

        DenunciaAnonima finalSavedDenuncia = denunciaAnonimaRepository.save(savedDenuncia);
        logger.debug("DEBUG SERVICE: Denuncia actualizada con rutas. ID: " + finalSavedDenuncia.getId());
        logger.info("--- FIN guardarDenuncia (Service) ---\n");
        return finalSavedDenuncia;
    }

    public Resource loadFileAsResource(String filename) throws FileNotFoundException {
        logger.info("\n--- INICIO loadFileAsResource (Anonima) ---");
        logger.debug("DEBUG SERVICE (Anonima): filename recibido: " + filename);

        String cleanedFilename = filename;
        if (filename.startsWith("/")) {
            cleanedFilename = filename.substring(1);
        }
        logger.debug("DEBUG SERVICE (Anonima): filename limpiado: " + cleanedFilename);

        Path filePath = this.rootLocation.resolve(cleanedFilename).normalize();

        logger.debug("DEBUG SERVICE (Anonima): Ruta física construida (filePath): " + filePath.toString());
        logger.debug("DEBUG SERVICE (Anonima): rootLocation (absoluta y normalizada): " + this.rootLocation.toString());

        if (!filePath.startsWith(this.rootLocation)) {
            logger.error("ERROR SERVICE (Anonima): ALERTA DE SEGURIDAD - Intento de acceso a ruta fuera del directorio permitido.");
            logger.error("ERROR SERVICE (Anonima): Ruta fuera de bounds: " + filePath.toString());
            logger.error("ERROR SERVICE (Anonima): Directorio raíz: " + this.rootLocation.toString());
            throw new FileNotFoundException("Ruta de archivo inválida para denuncia anónima: Acceso fuera del directorio permitido.");
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                logger.debug("DEBUG SERVICE (Anonima): Archivo encontrado y es legible: " + filePath.toString());
                logger.info("--- FIN loadFileAsResource (Anonima - ÉXITO) ---\n");
                return resource;
            } else {
                logger.error("ERROR SERVICE (Anonima): Archivo NO encontrado o NO legible en la ruta: " + filePath.toString());
                logger.info("--- FIN loadFileAsResource (Anonima - FALLO) ---\n");
                throw new FileNotFoundException("Archivo anónimo no encontrado o no se puede leer: " + filename);
            }
        } catch (MalformedURLException ex) {
            logger.error("ERROR SERVICE (Anonima): URL malformada para el archivo: " + filename + " - " + ex.getMessage());
            throw new FileNotFoundException("URL malformada al cargar archivo anónimo: " + filename + " - " + ex.getMessage());
        }
    }

    public Optional<DenunciaAnonima> findByCodigoDenuncia(String codigoDenuncia) {
        return denunciaAnonimaRepository.findByCodigoDenuncia(codigoDenuncia);
    }
}
