package org.eamcod.BardealApp.exception;

public class CompanyHasUserException extends RuntimeException{
    public CompanyHasUserException(Long id) {
        super("Can't delete. Company with id: " + id + " has one or more users.");
    }

}
