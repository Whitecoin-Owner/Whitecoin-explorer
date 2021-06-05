package com.browser.wallet.beans;

import lombok.Data;

import java.util.List;

@Data
public class SimpleContractInfo {
    private String id;
    private String owner_address;
    private String owner_name;
    private String name;
    private String description;
    private String type_of_contract;
    private Long registered_block;
    private String registered_trx;
    private String native_contract_key;
    private List<Object> derived;
    private CodePrintable code_printable;
    private String createtime;
}
