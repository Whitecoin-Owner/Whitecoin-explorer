import enLocale from 'element-ui/lib/locale/lang/en'
const japan = {
  nav: {
    home: 'ホームページ',
    blocks: 'ブロックチェーン',
    transactions: '取引履歴',
    contracts: 'コントラクト'
  },
  alert: {
    noSpace:'十分なスペースがありません',
    enterContent:'入力してください。',
    notSearch:'対応する検索結果が見つかりません'
  },
  search: {
    placeholder: 'アドレス/トランザクションハッシュ/コントラクトアドレス/ブロックの高さ/アカウント名'
  },
  home: {
    blockchinaInfo: {
      title: 'ブロック情報',
      totalSupply: '供給量',
      transactions: 'トランザクション数',
      blockHeight: 'ブロックの高さ',
      blockReward: 'ブロック報酬',
      crossAsset: 'その他の資産',
      accountCount: 'アカウントの数'
    },
    valueInfo: {
      title: 'バリューの情報',
      price: '価格',
      priceBTC: '価格（BTC）',
      change: '上がり幅（24H）'
    },
    blocks: {
      title: 'ブロック',
      more: 'もっと見る',
      all: '全部',
      height: '高さ',
      age: '時間',
      transactions: 'トランザクション',
      miner: 'マイナー',
      size: 'サイズ',
      reward: '報酬'
    },
    transaction: {
      title: 'トランザクション',
      more: 'もっと見る',
      all: '全部',
      txHash: 'トレーディングハッシュ ',
      block: 'ブロック',
      types: 'タイプ ',
      age: '時間',
      value: '金額',
      fee: '手数料'
    },
    transactionLine: {
      today: '今日',
      week: '今週',
      month: '今月',
      summary: 'トランザクション統計'
    }
  },
  transferDetails: {
    tips: {
      overview: '概要',
      event:'イベント'
    },
    information: {
      title: '取引情報',
      txHash: '取引ハッシュ',
      status: '取引結果ステータス',
      type: '取引結果種類',
      height: 'ブロックの高さ',
      timeStamp: '時間:'
    },
    details: {
      title: '明細',
      transfer: '送金',
      txHash: '取引ハッシュ',
      value: '金額',
      from: '送信者',
      memo: '注',
      address: 'コントラクトアドレス',
      gasLimit: 'ガスリミット',
      gasPrice:'ガスプライス',
      inoutData: '入力',
      contractInvoke: 'コントラクト呼び出し',
      contractAddress:'コントラクトアドレス',
      callerAddress:'発信者',
      actualTxCost_Fee:'取引手数料',
      acceptance:'受け入れる',
      id:'ID',
      exchange:'エクスチェンジ',
      amount:'金額',
      contractRegister: 'コントラクト登録',
      authorAddress:'創設者',
      inheritFrom:'',
      rate:'レート',
      contractTransfer:'コントラクト入金',
      contractUpgrade:'コントラクトのアップグレード',
      contractName:'コントラクト名称',
      description:'説明',
      crosschainRecord:'入金履歴',
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
      paybackBalances: '奖励金额:'
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
  ...enLocale
}
export default japan;