package ru.project.buy_sell_store.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.project.buy_sell_store.dto.ErrorDTO;
import ru.project.buy_sell_store.exception.globalEx.BuySellStoreConflictException;
import ru.project.buy_sell_store.exception.globalEx.BuySellStoreNotFoundException;

import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает, когда отсутсвует объект из базы данных
     */
    @ExceptionHandler(BuySellStoreNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundEx(BuySellStoreNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(ex.getMessage()));
    }

    /**
     * Обрабатывает конфликты
     */
    @ExceptionHandler(BuySellStoreConflictException.class)
    public ResponseEntity<ErrorDTO> handleConflictEx(BuySellStoreConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO(ex.getMessage()));
    }

    /**
     * Обрабатывает, когда запрашивается несуществующий ресурс
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundEx(NoResourceFoundException ex) {
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(description));
    }

    /**
     * Обрабатывает все неожиданные ошибки, не попавшие под другие обработчики
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleInternalServerEx(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(ex.getMessage()));
    }
}
