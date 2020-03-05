import zhLocale from 'element-ui/lib/locale/lang/zh-CN'
const cn = {
  nav: {
    home: '主页',
    blocks: '区块',
    transactions: '交易记录',
    contracts: '合约',
    richlist: '富豪榜'
  },
  alert: {
    noSpace:'没有足够空间',
    enterContent:'请输入',
    notSearch:'没找到对应的搜索结果'
  },
  search: {
    placeholder: '地址/交易hash/合约地址/块高/账户名'
  },
  home: {
    blockchinaInfo: {
      title: '区块信息',
      totalSupply: '供应量',
      transactions: '交易数',
      blockHeight: '区块高度',
      blockReward: '区块奖励',
      crossAsset: '其他资产',
      accountCount: '账户数量'
    },
    valueInfo: {
      title: 'Value Info',
      price: '价格($)',
      priceBTC: '价格(BTC)',
      change: '涨幅(24H)'
    },
    blocks: {
      title: '区块',
      more: '更多',
      all: '全部',
      height: '高度',
      age: '时间',
      transactions: '交易',
      miner: '播报方',
      size: '大小',
      reward: '奖励'
    },
    transaction: {
      title: '交易',
      more: '更多',
      all: '全部',
      txHash: '交易hash',
      block: '区块',
      types: '类型',
      age: '时间',
      value: '金额',
      fee: '手续费'
    },
    transactionLine: {
      today: '今天',
      week: '本周',
      month: '本月',
      summary: '交易统计'
    }
  },
  transferDetails: {
    tips: {
      overview: '概览',
      event:'事件'
    },
    information: {
      title: '交易信息',
      txHash: '交易hash:',
      status: '交易结果状态:',
      type: '交易结果类型:',
      height: '区块高度:',
      timeStamp: '时间:'
    },
    details: {
      title: '明细',
      transfer: '转账',
      txHash: '交易hash:',
      value: '金额:',
      from: '发送方:',
      memo: '备注:',
      address: '合约地址:',
      gasLimit: 'Gas Limit:',
      gasPrice:'Gas Price:',
      inoutData: '输入:',
      contractInvoke: '合约调用',
      contractAddress:'合约地址:',
      callerAddress:'调用者:',
      actualTxCost_Fee:'交易手续费:',
      acceptance:'接受',
      id:'ID:',
      exchange:'Exchange:',
      amount:'金额:',
      contractRegister: '合约注册',
      authorAddress:'创建人:',
      inheritFrom:'继承自:',
      rate:'速率:',
      contractTransfer:'合约充值',
      contractUpgrade:'合约升级',
      contractName:'合约名称',
      description:'描述:',
      crosschainRecord:'充值记录',
      minerName:'播报方:',
      minerAddress:'播报方地址:',
      fee:'手续费:',
      to:'接收方:',
      crosschainWithdraw:'提现',
      crosschainWithdrawResult:'提现结果',
      accountBind:'账户绑定',
      asset:'资产:',
      tunnelAddress:'通道地址:',
      guardRefundCrosschainTrx:'Guard Refund Crosschain Trx',
      guardAddress:'Guard地址:',
      guardID:'Guard ID:',
      cancelledTxHash:'取消交易hash:',
      accountCreate:'账户创建',
      name:'名称',
      minerCreate:'矿工创建',
      minerGenerateMultiAsset:'矿工创建多资产',
      hotAddress:'热钱包地址:',
      coldAddress:'冷钱包地址:',
      payBack:'奖励',
      ownerAddress:'拥有人:',
      acceptanceCreate:'Acceptance Create',
      valueTarget:'Value(Target):',
      acceptanceCancel:'Acceptance Cancel',
      acceptanceID:'Acceptance ID',
      toAddress:'接收人',
      fromAddress:'发送人'
    }
  },
  miner: {
    overview: {
      title: '矿工概览',
      name: '名称:',
      address: '地址:',
      contracts: '合约:',
      transaction: '交易:',
      rewards: '奖励:'
    },
    myTransactions: {
      title: '我的交易',
      txHash: '交易hash',
      block: '区块',
      types: '类型',
      age: '时间',
      from: '发送方',
      to: '接收方',
      value: '金额',
      fee: '手续费'
    },
    myContracts: {
      title: '我的合约',
      address: '合约地址',
      types: '类型',
      call: '调用次数',
      create: '创建时间',
      last: '最后使用时间'
    }
  },
  contracts: {
    title: '合约',
    contractAddress: '合约地址',
    types: '类型',
    authorAddress: '创建人',
    callTimes: '调用次数',
    createTime: '创建时间',
    lastTime: '最后使用时间',
    total_span_before: '一共发现',
    total_span_after: '个合约'
  },
  richlist: {
    title: '富豪榜',
    address: '地址',
    accountName: '账户名',
    amount: '金额'
  },
  contractOverview: {
    title: '合约概览',
    contractAddress: '合约地址:',
    authorAddress: '创建人:',
    transaction: '交易:',
    balance: '余额',
    createTxn: '创建交易:',
    tableTitle: '交易',
    api:'ABI',
    txHash: '交易hash',
    block: '区块',
    time: '时间',
    callerAddress: '调用者',
    value: '金额',
    fee: '手续费'
  },
  blocks: {
    title: '区块',
    height: '高度',
    age: '时间',
    txn: '交易数',
    miner: '播报者',
    size: '大小',
    reward: '奖励',
    total_span_before: '一共有',
    total_span_after: '个块',
    overview: {
      name: '概览',
      title: '区块信息',
      hash: 'Hash:',
      height: '高度:',
      timeStamp: "时间:",
      transactions: '交易:',
      miner: '播报者:',
      size: '大小:',
      rewards: '奖励:',
      version: '版本:',
      merkleRoot: 'Merkle Root:'
    },
    transaction: {
      name: '交易',
      txHash: '交易hash',
      types: '类型',
      value: '金额',
      fee: '手续费'
    }
  },
  transaction:{
    title:'交易',
    all:'全部',
    transfer:'转账',
    withdraw:'提现',
    recharge:'充值',
    contract:'合约',
    wage:'奖励',
    acceptance:'接收',
    foreclose: '赎回',
    mortgage: '投票',
    other:'其他',
    txHash:'交易hash',
    block:'区块',
    types:'类型',
    age:'时间',
    from:'发送方',
    to:'接收方',
    value:'金额',
    fee:'手续费',
    address: '地址',
    contractAddress:'合约地址',
    authorAddress:'创建人',
    createTime:'创建时间',
    exchange:'交易',
    balance:'余额',
    callerTimes:'调用次数',
    txs_found_before: '发现 ',
    txs_found_after: '笔交易'
  },
  address: {
    overview: {
      title: '地址概览',
      name: '账户名:',
      address: '地址:',
      contracts: '合约:',
      transaction: '交易:',
      rewards: '奖励:',
      balances: '余额:',
      lockBalance:'投票资产:',
      paybackBalances: '奖励金额:',
      abnormal_address: '异常地址'
    },
    myTransactions: {
      title: '我的交易',
      txHash: '交易hash',
      block: '高度',
      types: '类型',
      age: '时间',
      from: '发送方',
      to: '接收方',
      value: '金额',
      fee: '手续费'
    },
    myContracts: {
      title: '我的合约',
      address: '合约地址',
      types: '类型',
      call: '调用次数',
      create: '创建时间',
      last: '最后使用时间'
    }
  },
  ...zhLocale
}
export default cn;
