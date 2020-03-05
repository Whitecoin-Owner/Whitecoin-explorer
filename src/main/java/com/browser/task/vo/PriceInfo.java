package com.browser.task.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PriceInfo {
    private BigDecimal volume;
    private BigDecimal price;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal change;
}
