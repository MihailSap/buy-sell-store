package ru.project.buySellStore.exception.handler;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.project.buySellStore.dto.ErrorDTO;
import ru.project.buySellStore.exception.globalEx.BuySellStoreConflictException;
import ru.project.buySellStore.exception.globalEx.BuySellStoreException;
import ru.project.buySellStore.exception.globalEx.BuySellStoreNotFoundException;
import ru.project.buySellStore.exception.globalEx.BuySellStoreNotSuitableRoleException;

import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Обрабатывает, когда отсутствует объект из базы данных
     */
    @ExceptionHandler(BuySellStoreNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundEx(BuySellStoreNotFoundException ex) {
        logger.error("NotFoundException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(ex.getMessage()));
    }

    /**
     * Обрабатывает конфликты
     */
    @ExceptionHandler(BuySellStoreConflictException.class)
    public ResponseEntity<ErrorDTO> handleConflictEx(BuySellStoreConflictException ex) {
        logger.error("ConflictException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO(ex.getMessage()));
    }

    @ExceptionHandler(BuySellStoreNotSuitableRoleException.class)
    public ResponseEntity<ErrorDTO> handleNotSuitableRoleEx(BuySellStoreNotSuitableRoleException ex) {
        logger.error("NotSuitableRoleException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO(ex.getMessage()));
    }

    /**
     * Обрабатывает ситуации, когда доступ запрещен
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDeniedEx(AccessDeniedException ex){
        logger.error("AccessDeniedException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO(ex.getMessage()));
    }

    /**
     * Обрабатывает, когда запрашивается несуществующий ресурс
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundEx(NoResourceFoundException ex) {
        logger.error("NoResourceFoundException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("Ресурс /%s не существует".formatted(ex.getResourcePath())));
    }

    /**
     * Обрабатывает ошибки валидации входных данных
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDTO> handle(MethodArgumentNotValidException ex) {
        String description = "%s %s".formatted("Неправильно заполнены поля.",
                ex.getBindingResult().getFieldErrors().stream().map((fe) -> {
                    String fieldName = fe.getField();
                    return fieldName + ": " + fe.getDefaultMessage();
                }).collect(Collectors.joining(", ")));
        logger.error("ValidationException: {}", description, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(description));
    }

    /**
     * Обрабатывает все неожиданные ошибки, не попавшие под другие обработчики
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleInternalServerEx(Exception ex) {
        logger.error("Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(ex.getMessage()));
    }
}
