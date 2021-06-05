package com.browser.wallet.beans;

import lombok.Data;

@Data
public class TxReceiptEvent {
    private String contract_address;
    private String caller_addr;
    private String event_name;
    private String event_arg;
    private Long block_num;
    private Integer op_num;
}
