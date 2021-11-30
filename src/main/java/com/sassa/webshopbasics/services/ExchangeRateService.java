package com.sassa.webshopbasics.services;

import com.sassa.webshopbasics.dto.ExchangeRateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class ExchangeRateService {
  @Autowired
  private Environment env;

  @Cacheable("exchangeRate")
  public BigDecimal getEurRate(String date) {
    RestTemplate restTemplate = new RestTemplate();

    String url = env.getProperty("url.hnb") + "/tecajn/v2?valuta=EUR&datum-primjene=" + date;
    ExchangeRateDto[] exchangeRateDto = restTemplate.getForObject(url, ExchangeRateDto[].class);
    return (exchangeRateDto == null || exchangeRateDto.length == 0) ? null
            : new BigDecimal(exchangeRateDto[0].getSrednji_tecaj().replace(",", "."));
  }
}
