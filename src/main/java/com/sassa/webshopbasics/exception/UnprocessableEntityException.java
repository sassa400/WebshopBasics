package com.sassa.webshopbasics.exception;

public class UnprocessableEntityException extends RuntimeException {
  public UnprocessableEntityException(String errorMessage) {
    super(errorMessage);
  }
}