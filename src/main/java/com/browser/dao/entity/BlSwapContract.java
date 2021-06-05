package com.browser.dao.entity;

import lombok.Data;

@Data
public class BlSwapContract {
    private Long id;
    private String contractAddress;
    private String token1;
    private String token2;
    private Boolean verified;
}
