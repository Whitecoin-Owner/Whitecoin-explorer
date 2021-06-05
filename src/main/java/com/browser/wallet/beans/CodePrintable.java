package com.browser.wallet.beans;

import lombok.Data;

import java.util.List;

@Data
public class CodePrintable {
    private List<String> abi;
    private List<String> offline_abi;
    private List<String> events;
    private String code_hash;
}
