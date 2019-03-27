package o2o.exceptions;

/**
 * 商品操作异常
 */
public class ProductOperationException extends RuntimeException {

    public ProductOperationException(String message) {
        super(message);
    }
}
