package com.hashedin.hu16starktech.exceptions;

public class UserExistsByEmailIdException extends Exception {
    public UserExistsByEmailIdException(String message) {
        super(message);
    }
}
