-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 22-07-2025 a las 06:44:26
-- Versión del servidor: 9.1.0
-- Versión de PHP: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `themis_denuncias`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias_denuncia`
--

DROP TABLE IF EXISTS `categorias_denuncia`;
CREATE TABLE IF NOT EXISTS `categorias_denuncia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` text,
  `fecha_actualizacion` datetime(6) DEFAULT NULL,
  `fecha_registro` datetime(6) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKmcrpmbkvlm9ybuc16ew4t5f7m` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contenidos`
--

DROP TABLE IF EXISTS `contenidos`;
CREATE TABLE IF NOT EXISTS `contenidos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contenido` text NOT NULL,
  `es_publico` bit(1) NOT NULL,
  `titulo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `denuncias_anonimas_detalles`
--

DROP TABLE IF EXISTS `denuncias_anonimas_detalles`;
CREATE TABLE IF NOT EXISTS `denuncias_anonimas_detalles` (
  `correo_anonima` varchar(255) DEFAULT NULL,
  `denuncia_id` bigint NOT NULL,
  PRIMARY KEY (`denuncia_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `denuncias_anonimas_detalles`
--

INSERT INTO `denuncias_anonimas_detalles` (`correo_anonima`, `denuncia_id`) VALUES
('sobrinoluza@gmail.com', 2),
('sobrinoluza@gmail.com', 3),
('sobrinoluza@gmail.com', 4),
('sobrinoluza@gmail.com', 5),
('sobrinoluza@gmail.com', 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `denuncias_base`
--

DROP TABLE IF EXISTS `denuncias_base`;
CREATE TABLE IF NOT EXISTS `denuncias_base` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `es_anonima` bit(1) NOT NULL,
  `codigo_denuncia` varchar(255) NOT NULL,
  `departamento` varchar(255) NOT NULL,
  `descripcion_hechos` tinytext NOT NULL,
  `distrito` varchar(255) NOT NULL,
  `es_ahora` bit(1) NOT NULL,
  `estado` varchar(255) NOT NULL,
  `fecha_incidente` date DEFAULT NULL,
  `fecha_registro` datetime(6) NOT NULL,
  `hora_incidente` time(6) DEFAULT NULL,
  `provincia` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKmf1agjuv0hq48vhx8vknfcotn` (`codigo_denuncia`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `denuncias_base`
--

INSERT INTO `denuncias_base` (`id`, `es_anonima`, `codigo_denuncia`, `departamento`, `descripcion_hechos`, `distrito`, `es_ahora`, `estado`, `fecha_incidente`, `fecha_registro`, `hora_incidente`, `provincia`) VALUES
(1, b'0', 'REAL-9553D75C', 'Lima', 'dasdsad', 'Los Olivos', b'1', 'PENDIENTE', NULL, '2025-07-22 01:11:19.424054', NULL, 'Lima'),
(2, b'1', 'ANON-D753FB1F', 'Lima', 'asdasda', 'Los Olivos', b'1', 'PENDIENTE', NULL, '2025-07-22 01:11:58.992960', NULL, 'Lima'),
(3, b'1', 'ANON-3F98F707', 'Lima', 'sadasewq', 'Los Olivos', b'1', 'PENDIENTE', NULL, '2025-07-22 01:25:22.229521', NULL, 'Lima'),
(4, b'1', 'ANON-9E479CB7', 'Lima', 'wdasdsa', 'La Molina', b'1', 'PENDIENTE', NULL, '2025-07-22 01:25:45.836624', NULL, 'Lima'),
(5, b'1', 'ANON-2893E55D', 'Lima', 'weaewa', 'Lince', b'1', 'PENDIENTE', NULL, '2025-07-22 01:32:46.295102', NULL, 'Lima'),
(6, b'0', 'REAL-FF079F00', 'Lima', 'j kjdshdisadsa', 'Cieneguilla', b'1', 'PENDIENTE', NULL, '2025-07-22 01:35:08.183875', NULL, 'Lima'),
(7, b'1', 'ANON-3FA1BAB5', 'Lima', 'adjhasjdhase', 'Chaclacayo', b'1', 'PENDIENTE', NULL, '2025-07-22 01:35:52.945790', NULL, 'Lima');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `denuncias_persona_real_detalles`
--

DROP TABLE IF EXISTS `denuncias_persona_real_detalles`;
CREATE TABLE IF NOT EXISTS `denuncias_persona_real_detalles` (
  `apellido_materno` varchar(255) NOT NULL,
  `apellido_paterno` varchar(255) NOT NULL,
  `autorizo_datos` bit(1) NOT NULL,
  `correo_electronico_denunciante` varchar(255) DEFAULT NULL,
  `fecha_emision` date DEFAULT NULL,
  `fecha_nacimiento` date NOT NULL,
  `nombres` varchar(255) NOT NULL,
  `numero_celular` varchar(255) DEFAULT NULL,
  `numero_documento` varchar(255) NOT NULL,
  `sexo` varchar(255) NOT NULL,
  `tipo_documento` varchar(255) NOT NULL,
  `denuncia_id` bigint NOT NULL,
  `usuario_id` bigint DEFAULT NULL,
  PRIMARY KEY (`denuncia_id`),
  KEY `FKksj0b65ch4yv0ovx1ppvdecnk` (`usuario_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `denuncias_persona_real_detalles`
--

INSERT INTO `denuncias_persona_real_detalles` (`apellido_materno`, `apellido_paterno`, `autorizo_datos`, `correo_electronico_denunciante`, `fecha_emision`, `fecha_nacimiento`, `nombres`, `numero_celular`, `numero_documento`, `sexo`, `tipo_documento`, `denuncia_id`, `usuario_id`) VALUES
('Luza', 'Sobrino', b'1', 'sobrinoluza@gmail.com', '2025-07-22', '2025-07-22', 'Nicole Melina', '954843966', '72706664', 'Femenino', 'DNI', 1, NULL),
('Luza', 'Sobrino', b'1', 'sobrinoluza@gmail.com', '2025-07-22', '2025-07-22', 'Nicole Melina', '954843966', '72706664', 'Femenino', 'DNI', 6, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `denuncia_archivos`
--

DROP TABLE IF EXISTS `denuncia_archivos`;
CREATE TABLE IF NOT EXISTS `denuncia_archivos` (
  `denuncia_id` bigint NOT NULL,
  `ruta_archivo` varchar(512) DEFAULT NULL,
  KEY `FKg3eefpejssfo2m87p2fa6rio3` (`denuncia_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `denuncia_archivos`
--

INSERT INTO `denuncia_archivos` (`denuncia_id`, `ruta_archivo`) VALUES
(1, 'denuncias_real/REAL-9553D75C/254f288e-b745-4b8c-bc6f-549459ef1eb0.pdf'),
(2, 'ANON-D753FB1F/9e03c467-5442-4fb7-8459-84c7f3f08db2.pdf'),
(3, 'ANON-3F98F707/5145c91d-fe78-4656-aa9b-2a4b29223500.pdf'),
(4, 'ANON-9E479CB7/4b4b8a1c-d582-473a-88c4-f88a8fa4ebcc.pdf'),
(5, 'ANON-2893E55D/48a4afcc-38bd-4a3c-a7f9-47c03ab44014.pdf'),
(6, 'denuncias_real/REAL-FF079F00/6697e5d5-d9f4-44d9-981d-61a624bc3dec.pdf'),
(7, 'ANON-3FA1BAB5/0a8e5755-09d3-49d9-8ba5-e0978568a38d.pdf');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` bigint NOT NULL AUTO_INCREMENT,
  `contrasenia` varchar(255) NOT NULL,
  `correo_electronico` varchar(100) NOT NULL,
  `habilitado` bit(1) NOT NULL,
  `nombre_usuario` varchar(50) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `UKduxldumspflsqyka52vo72hse` (`correo_electronico`),
  UNIQUE KEY `UKof5vabgukahdwmgxk4kjrbu98` (`nombre_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `contrasenia`, `correo_electronico`, `habilitado`, `nombre_usuario`) VALUES
(1, '$2a$10$OkQYTObyQCraI7SL67PNRe6ZlnrzWlwW638QMtnbVpt90O0aUV0W2', 'admin@gmail.com', b'1', 'admin');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_roles`
--

DROP TABLE IF EXISTS `usuario_roles`;
CREATE TABLE IF NOT EXISTS `usuario_roles` (
  `id_usuario` bigint NOT NULL,
  `roles` enum('ADMINISTRADOR','USUARIO_REGISTRADO') DEFAULT NULL,
  KEY `FKor5vdfg7bv12b37vaawa9lee2` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuario_roles`
--

INSERT INTO `usuario_roles` (`id_usuario`, `roles`) VALUES
(1, 'ADMINISTRADOR');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `denuncias_anonimas_detalles`
--
ALTER TABLE `denuncias_anonimas_detalles`
  ADD CONSTRAINT `FKga2e9sivutsndk5b8se3l97ir` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias_base` (`id`);

--
-- Filtros para la tabla `denuncias_persona_real_detalles`
--
ALTER TABLE `denuncias_persona_real_detalles`
  ADD CONSTRAINT `FK7ca6vn4da81es3e66al89ulsq` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias_base` (`id`),
  ADD CONSTRAINT `FKksj0b65ch4yv0ovx1ppvdecnk` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `denuncia_archivos`
--
ALTER TABLE `denuncia_archivos`
  ADD CONSTRAINT `FKg3eefpejssfo2m87p2fa6rio3` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias_base` (`id`);

--
-- Filtros para la tabla `usuario_roles`
--
ALTER TABLE `usuario_roles`
  ADD CONSTRAINT `FKor5vdfg7bv12b37vaawa9lee2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
