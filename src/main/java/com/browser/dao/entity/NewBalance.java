package com.browser.dao.entity;

import lombok.Data;

import java.util.List;

/**
 * @author xuj
 * @description
 * @date 2020/12/1
 */
@Data
public class NewBalance {

    /**
     * tokens
     */
    List<BlTokenBalance> tokenBalances;

    private BlMinerStatis xwcBanlance;


}
