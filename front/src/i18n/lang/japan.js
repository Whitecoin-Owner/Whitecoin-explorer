import enLocale from 'element-ui/lib/locale/lang/en'
const japan = {
    nav: {
        home: 'ホームページ',
        blocks: 'ブロックチェーン',
        transactions: '取引履歴',
        contracts: 'コントラクト',
        richlist: '豊富なリスト'
    },
    alert: {
        noSpace: '十分なスペースがありません',
        enterContent: '入力してください。',
        notSearch: '対応する検索結果が見つかりません。'
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
            event: 'イベント'
        },
        information: {
            title: '取引情報',
            txHash: '取引ハッシュ',
            status: '取引結果ステータス',
            type: '取引結果種類',
            height: 'ブロックの高さ',
            timeStamp: '時間'
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
            gasPrice: 'ガスプライス',
            inoutData: '入力',
            contractInvoke: 'コントラクト呼び出し',
            contractAddress: 'コントラクトアドレス',
            callerAddress: '発信者',
            actualTxCost_Fee: '取引手数料',
            acceptance: '受け入れる',
            id: 'ID',
            exchange: 'エクスチェンジ',
            amount: '金額',
            contractRegister: 'コントラクト登録',
            authorAddress: '創設者',
            inheritFrom: '继承自:', ＝
            rate: 'レート',
            contractTransfer: 'コントラクト入金',
            contractUpgrade: 'コントラクトのアップグレード',
            contractName: 'コントラクト名称',
            description: '説明',
            crosschainRecord: '入金履歴',
            minerName: 'マイナー名',
            minerAddress: 'マイナーアドレス',
            fee: '手数料',
            to: '相手',
            crosschainWithdraw: '出金',
            crosschainWithdrawResult: '出金結果',
            accountBind: 'アカウント紐づけ',
            asset: '資産',
            tunnelAddress: 'チャンネルアドレス',
            guardRefundCrosschainTrx: 'Guard Refund Crosschain Trx',
            guardAddress: 'ガードアドレス',
            guardID: 'ガードID',
            cancelledTxHash: 'トランザクションハッシュのキャンセル：',
            accountCreate: 'アカウント作成',
            name: '名称',
            minerCreate: 'マイナー作成',
            minerGenerateMultiAsset: 'マイナー作成資産',
            hotAddress: 'ホットウォレットアドレス',
            coldAddress: 'コールドウォレットアドレス',
            payBack: '報酬',
            ownerAddress: 'オーナーアドレス',
            acceptanceCreate: 'Acceptanc作成',
            valueTarget: '値（ターゲット）',
            acceptanceCancel: 'Acceptanceキャンセル',
            acceptanceID: 'Acceptance ID',
            toAddress: '受取アドレス',
            fromAddress: '送信アドレス'
        }
    },
    miner: {
        overview: {
            title: 'タイトル',
            name: '名前',
            address: 'アドレス',
            contracts: 'コントラクト',
            transaction: 'トランザクション',
            rewards: '報酬'
        },
        myTransactions: {
            title: '私の取引',
            txHash: '取引ハッシュ',
            block: 'ブロック',
            types: '種類',
            age: '時間',
            from: '送信者',
            to: '受取人',
            value: '金額',
            fee: '手数料'
        },
        myContracts: {
            title: '私のコントラクト',
            address: 'コントラクトアドレス',
            types: '種類',
            call: '呼び出し回数',
            create: '作成時間',
            last: '最後使用時間'
        }
    },
    contracts: {
        title: 'コントラクト',
        contractAddress: 'コントラクトアドレス',
        types: '種類',
        authorAddress: '作成者',
        callTimes: '呼び出し回数',
        createTime: '作成時間',
        lastTime: '最後使用時間',
        total_span_before: 'total_span_before',
        total_span_after: 'total_span_after'
    },
    richlist: {
        title: '豊富なリスト',
        address: '住所',
        accountName: 'アカウント名',
        amount: '金額'
    },
    contractOverview: {
        title: 'コントラクト概要',
        contractAddress: 'コントラクトアドレス',
        authorAddress: '作成者',
        transaction: '取引',
        balance: '残高',
        createTxn: 'トランザクションを作成する',
        tableTitle: 'トランザクション',
        api: 'ABI',
        txHash: 'トランザクションハッシュ',
        block: 'ブロック',
        time: '時間',
        callerAddress: 'callerアドレス',
        value: '金額',
        fee: '手数料'
    },
    blocks: {
        title: 'ブロック',
        height: '高さ',
        age: '時間',
        txn: 'トランザクション数',
        miner: 'マイナー',
        size: 'サイズ',
        reward: '報酬',
        total_span_before: 'total_span_before:',
        total_span_after: 'total_span_after',
        overview: {
            name: '概要',
            title: 'タイトル',
            hash: 'ハッシュ',
            height: '高さ',
            timeStamp: "時間:",
            transactions: 'トランザクション',
            miner: 'マイナー',
            size: 'サイズ',
            rewards: '報酬:',
            version: 'バージョン',
            merkleRoot: 'マークル根'
        },
        transaction: {
            name: 'トランザクション',
            txHash: 'トランザクションハッシュ',
            types: '種類',
            value: '金額',
            fee: '手数料'
        }
    },
    transaction: {
        title: 'トランザクション',
        all: '全部',
        transfer: '振り込み',
        withdraw: '出金',
        recharge: '入金',
        contract: 'コントラクト',
        wage: '報酬',
        acceptance: '受け取る',
        foreclose: '引き換え',
        mortgage: '投票',
        other: 'その他',
        txHash: '取引ハッシュ',
        block: 'ブロック',
        types: '種類',
        age: '時間',
        from: '送信者',
        to: '受け取りアドレス',
        value: '金額',
        fee: '手数料',
        address: 'アドレス',
        contractAddress: 'コントラクトアドレス',
        authorAddress: '作成者',
        createTime: '作成時間',
        exchange: 'トランザクション',
        balance: '残高',
        callerTimes: '呼び出し回数',
        txs_found_before: '見つかりました',
        txs_found_after: '回の取引'
    },
    address: {
        overview: {
            title: 'タイトル',
            name: 'アカウント名',
            address: 'アドレス',
            contracts: 'コントラクト',
            transaction: 'トランザクション',
            rewards: '報酬',
            balances: '残高',
            lockBalance: '投票資産',
            paybackBalances: '報酬金額'
        },
        myTransactions: {
            title: '私の取引',
            txHash: '取引ハッシュ',
            block: '高さ',
            types: '種類',
            age: '時間',
            from: '送信者',
            to: '受取人',
            value: '金額',
            fee: '手数料'
        },
        myContracts: {
            title: '私のコントラクト',
            address: 'コントラクトアドレス',
            types: '種類',
            call: '呼び出し回数      ',
            create: '作成時間',
            last: '最終使用時間'
        }
    },
    ...enLocale
}
export default japan;
