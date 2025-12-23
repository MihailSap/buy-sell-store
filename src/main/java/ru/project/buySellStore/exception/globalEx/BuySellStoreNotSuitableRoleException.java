package ru.project.buySellStore.exception.globalEx;

/**
 * Exception для ситуаций, когда Role не подходит
 */
public class BuySellStoreNotSuitableRoleException extends BuySellStoreException{
    public BuySellStoreNotSuitableRoleException(String message) {
        super(message);
    }
}