package br.com.welitoncardozo.formula1.services.exceptions;

public class IntegrityViolation extends RuntimeException{
	
	public IntegrityViolation(String message) {
		super(message);
	}

}
