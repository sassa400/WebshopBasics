package com.sassa.webshopbasics.exception;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiError {

  private HttpStatus status;
  private String message;
  private List<String> errors;

  public ApiError(final HttpStatus status, final String message, final List<String> errors) {
    super();
    this.status = status;
    this.message = message;
    this.errors = errors;
  }

  public ApiError(final HttpStatus status, final String message, final String error) {
    super();
    this.status = status;
    this.message = message;
    errors = List.of(error);
  }
}