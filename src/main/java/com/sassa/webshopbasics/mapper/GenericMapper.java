package com.sassa.webshopbasics.mapper;

public interface GenericMapper<D, E> {
  D mapDto(E entity);

  E map(D dto);
}
