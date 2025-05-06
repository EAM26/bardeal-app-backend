package org.eamcod.BardealApp.exception;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(Long id) {
        super("Company with id: " + id + " not found");
    }

    public CompanyNotFoundException(String name) {
        super("Company with name: " + name + " not found.");
    }

    public CompanyNotFoundException( ) {
        super("Company not found.");
    }
}
