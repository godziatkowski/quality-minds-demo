package com.example.demo.web.currency;

import javax.validation.constraints.Size;

record NewCurrency(String name, @Size(min = 3,
                                      max = 3) String isoCode) {

}
