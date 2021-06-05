package com.browser.service.impl;

import com.browser.dao.entity.BlTxContractBalanceChange;
import com.browser.dao.mapper.BlTxContractBalanceChangeMapper;
import com.browser.service.TxContractBalanceChangeService;
import com.browser.wallet.beans.TxReceiptContractBalanceChange;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TxContractBalanceChangeServiceImpl implements TxContractBalanceChangeService {
    @Resource
    private BlTxContractBalanceChangeMapper txContractBalanceChangeMapper;

    @Override
    public int insert(BlTxContractBalanceChange record) {
        return txContractBalanceChangeMapper.insert(record);
    }

    @Override
    public BlTxContractBalanceChange selectByTrxIdAndChangeTypeAndChangeSeq(String trxId, String changeType, int changeSeq) {
        BlTxContractBalanceChange cond = new BlTxContractBalanceChange();
        cond.setTrxId(trxId);
        cond.setChangeType(changeType);
        cond.setChangeSeq(changeSeq);
        List<BlTxContractBalanceChange> records = txContractBalanceChangeMapper.selectAllByCond(cond);
        if(records.isEmpty()) {
            return null;
        } else {
            return records.get(0);
        }
    }

    @Override
    public List<BlTxContractBalanceChange> selectAllByTrxId(String trxId) {
        BlTxContractBalanceChange cond = new BlTxContractBalanceChange();
        cond.setTrxId(trxId);
        return txContractBalanceChangeMapper.selectAllByCond(cond);
    }
}
