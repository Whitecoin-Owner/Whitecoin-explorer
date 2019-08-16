package com.browser.service;

import java.util.List;

import com.browser.dao.entity.BlBlock;
import com.browser.protocol.EUDataGridResult;

public interface BlockService {
	
	 /**
     * 
    * @Title: selectByBlockNum 
    * @Description:根据快号查询快信息
    * @author David
    * @param 
    * @return TblBcBlock 
    * @throws
     */
	BlBlock selectByBlockNum(Long blocknUm);
    
    /**
     * 
    * @Title: queryBlockNum 
    * @Description:获取最大的快号
    * @author David
    * @param 
    * @return int 
    * @throws
     */
    Long queryBlockNum();
    

    void insertBatchBlock(List<BlBlock> list);

    void insertSelective(BlBlock blBlock);

    EUDataGridResult getBlockInfoList(BlBlock blBlock);


}
