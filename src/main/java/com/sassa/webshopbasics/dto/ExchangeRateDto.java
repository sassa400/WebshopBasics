package com.sassa.webshopbasics.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeRateDto {

  private String brojTecajnice;
  private String datum_primjene;
  private String drzava;
  private String drzava_iso;
  private String sifra_valute;
  private String valuta;
  private String jedinica;
  private String kupovni_tecaj;
  private String srednji_tecaj;
  private String prodajni_tecaj;
}
