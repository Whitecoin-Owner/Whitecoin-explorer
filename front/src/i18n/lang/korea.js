import enLocale from 'element-ui/lib/locale/lang/en'
const korea = {
  nav: {
    home: '홈',
    blocks: '블록',
    transactions: '거래 내역',
    contracts: '컨트렉트',
    richlist: '풍부한 목록'
  },
  alert: {
    noSpace:'사용 가능한 공간이 부족합니다',
    enterContent:'입력해주세요',
    notSearch:'검색 결과를 찾을 수 없습니다'
  },
  search: {
    placeholder: '주소/컨트렉트 주소/블록/계정명'
  },
  home: {
    blockchinaInfo: {
      title: '블록 정보',
      totalSupply: '공급량',
      transactions: '거래수',
      blockHeight: '블록 높이',
      blockReward: '블록 리워드',
      crossAsset: '기타 자산',
      accountCount: '계정 수량'
    },
    valueInfo: {
      title: 'Value Info',
      price: ' 가격($)',
      priceBTC: '가격(BTC)',
      change: '변동(24H)'
    },
    blocks: {
      title: '블록',
      more: '더 보기',
      all: '전부',
      height: '높이',
      age: '시간',
      transactions: '거래',
      miner: '마이너',
      size: '사이즈',
      reward: '리워드'
    },
    transaction: {
      title: '거래',
      more: '더 보기',
      all: '전부',
      txHash: '거래 해시',
      block: '블록',
      types: '유형',
      age: '시간',
      value: '금액',
      fee: '수수료'
    },
    transactionLine: {
      today: '오늘',
      week: '이번주',
      month: '이번달',
      summary: '거래 통계'
    }
  },
  transferDetails: {
    tips: {
      overview: '개요',
      event:'이벤트'
    },
    information: {
      title: '거래 정보',
      txHash: '거래 해시:',
      status: '거래 결과 상태:',
      type: '거래 결과 유형:',
      height: '블록 높이:',
      timeStamp: '시간:'
    },
    details: {
      title: '거래 정보',
      transfer: '이체',
      txHash: '거래 해시:',
      value: '금액:',
      from: '에서:',
      memo: '비고:',
      address: '컨트렉트 주소:',
      gasLimit: 'Gas Limit:',
      gasPrice:'Gas Price:',
      inoutData: '입력:',
      contractInvoke: '컨트렉트 호출',
      contractAddress:'컨트렉트 주소:',
      callerAddress:'호출자 주소:', 
      actualTxCost_Fee:'거래 수수료:',
      acceptance:'접수',
      id:'ID:',
      exchange:'Exchange:',
      amount:'금액:',
      contractRegister: '컨트렉트 가입',
      authorAddress:'생성자:',
      inheritFrom:'~에서 물려받음:',
      rate:'레이트:',
      contractTransfer:'컨트렉트 충전',
      contractUpgrade:'컨트렉트 업데이트',
      contractName:'컨트렉트 명칭',
      description:'설명:',
      crosschainRecord:'거래 내역',
      minerName:'마이너 이름:',
      minerAddress:'마이너 주소:',
      fee:'수수료:',
      to:'받는 사람:',
      crosschainWithdraw:'출금', 
      crosschainWithdrawResult:'출금 결과',
      accountBind:'계정 바인드',
      asset:'자산:',
      tunnelAddress:'게이트웨이 주소:',
      guardRefundCrosschainTrx:'Guard Refund Crosschain Trx',
      guardAddress:'Guard주소:',
      guardID:'Guard ID:',
      cancelledTxHash:'거래 해시 취소:',
      accountCreate:'계정 만들기',
      name:'이름',
      minerCreate:'마이너 만들기', 
      minerGenerateMultiAsset:'마이너 멀티 자산 생성',
      hotAddress:'핫 월렛 주소:',
      coldAddress:'콜드 월렛 주소:',
      payBack:'상금',
      ownerAddress:'보유자:',
      acceptanceCreate:'Acceptance Create',
      valueTarget:'Value(Target):',
      acceptanceCancel:'Acceptance Cancel',
      acceptanceID:'Acceptance ID',
      toAddress:'받는 사람',
      fromAddress:'보내는 사람'
    }
  },
  miner: {
    overview: {
      title: '마이너 개요',
      name: '이름:',
      address: '주소:', 
      contracts: '컨트렉트:',
      transaction: '거래:',
      rewards: '리워드:'
    },
    myTransactions: {
      title: '내 거래', 
      txHash: '거래 해시',
      block: '블록',
      types: '유형',
      age: '시간',
      from: '에서',
      to: '~로', 
      value: '금액',
      fee: '수수료'
    },
    myContracts: {
      title: '내 컨트렉트',
      address: '컨트렉트 주소',
      types: '유형',
      call: '호출 횟수',
      create: '생성 시간',
      last: '마지막 사용 시간'
    }
  },
  contracts: {
    title: '컨트렉트',
    contractAddress: '컨트렉트 주소',
    types: '유형',
    authorAddress: '생성자',
    callTimes: '호출 횟수',
    createTime: '생성 시간',
    lastTime: '마지막 사용 시간',
    total_span_before: '총 발견',
    total_span_after: '_개의 컨트렉트'
  },
  richlist: {
    title: '풍부한 목록',
    address: '주소',
    accountName: '계정 이름',
    amount: '금액'
  },
  contractOverview: {
    title: '컨트렉트 개요',
    contractAddress: '컨트렉트 주소:',
    authorAddress: '생성자:',
    transaction: '거래:',
    balance: '잔액',
    createTxn: '거래 하기:',
    tableTitle: '거래',
    api:'ABI',
    txHash: '거래 해시',
    block: '블록',
    time: '시간',
    callerAddress: '호출 주소',
    value: '금액',
    fee: '수수료'
  },
  blocks: {
    title: '블록',
    height: '높이',
    age: '시간',
    txn: '거래수',
    miner: '마이너',
    size: '사이즈',
    reward: '리워드',
    total_span_before: '총_',
    total_span_after: '발견 된 블록',
    overview: {
      name: '개요',
      title: '블록 정보',
      hash: 'Hash:',
      height: '높이:',
      timeStamp: "시간:",
      transactions: '거래:',
      miner: '마이너:',
      size: '사이즈:',
      rewards: '리워드:',
      version: '버전:',
      merkleRoot: 'Merkle Root:'
    },
    transaction: {
      name: '거래',
      txHash: '거래 해시',
      types: '유형',
      value: '금액',
      fee: '수수료'
    }
  },
  transaction:{
    title:'거래',
    all:'전부',
    transfer:'이체',
    withdraw:'출금',
    recharge:'충전',
    contract:'컨트렉트',
    wage:'상금',
    acceptance:'접수',
    foreclose: '회수',
    mortgage: '투표',
    other:'기타',
    txHash:'거래 해시',
    block:'블록',
    types:'유형',
    age:'시간',
    from:'에서',
    to:'~로',
    value:'금액',
    fee:'수수료',
    address: '주소',
    contractAddress:'컨트렉트 주소',
    authorAddress:'생성자',
    createTime:'생성 시간',
    exchange:'거래',
    balance:'잔액',
    callerTimes:'호출 횟수',
    txs_found_before: '발견 ', 
    txs_found_after: '발견 된 거래'
  },
  address: {
    overview: {
      title: '주소 개요',
      name: '계정명:',
      address: '주소:',
      contracts: '컨트렉트:',
      transaction: '거래:',
      rewards: '리워드:',
      balances: '잔액:',
      lockBalance:'투표 자산:',
      paybackBalances: '상금 금액:'
    },
    myTransactions: {
      title: '내 거래',
      txHash: '거래 해시',
      block: '블록',
      types: '유형',
      age: '시간',
      from: '에서',
      to: '~로',
      value: '금액',
      fee: '수수료'
    },
    myContracts: {
      title: '내 컨트렉트',
      address: '컨트렉트 주소',
      types: '유형',
      call: '호출 횟수',
      create: '생성 시간',
      last: '마지막 시용 시간'
    }
  },
  ...enLocale
}
export default korea;