import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/view/Home';
import TransferDetails from '@/view/TransferDetails';
import Contracts from '@/view/Contracts';
import ContractOverview from '@/view/ContractOverview';
import Blocks from '@/view/Blocks';
import BlockDetails from '@/view/BlockDetails';
import Transaction from '@/view/Transaction';
import Address from '@/view/Address';
import Richlist from '@/view/Richlist';
import Assets from '@/view/Assets';
import Token from '@/view/Token';


import HomeMobile from '@/view/Mobile/HomeMobile';
import TransactionMobile from '@/view/Mobile/TransactionMobile';
import TransferDetailsMobile from '@/view/Mobile/TransferDetailsMobile';
import AddressMobile from '@/view/Mobile/AddressMobile';
import BlocksMobile from '@/view/Mobile/BlocksMobile';
import BlockDetailsMobile from '@/view/Mobile/BlockDetailsMobile';
import TokenMobile from '@/view/Mobile/TokenMobile';
import ContractsMobile from '@/view/Mobile/ContractsMobile';
import RichlistMobile from '@/view/Mobile/RichlistMobile';
import AssetsMobile from '@/view/Mobile/AssetsMobile';
import ContractOverviewMobile from '@/view/Mobile/ContractOverviewMobile';



import device from "current-device";

Vue.use(Router)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/transfer_details/:xhHash/:type',
    component: TransferDetails
  },
  {
    path: '/contracts',
    component: Contracts
  },
  {
    path: '/contractOverview/:contractAddress',
    component: ContractOverview
  },
  {
    path: '/blocks',
    component: Blocks
  },
  {
    path: '/blockDetails/:height',
    component: BlockDetails
  },
  {
    path: '/transaction',
    component: Transaction
  },
  {
    path: '/address',
    component: Address
  },
  {
    path: '/richlist',
    component: Richlist
  },
  {
    path: '/assets',
    component: Assets
  },
  {
    path: '/tokens',
    component: Token
  }
]

const routesMobile = [
  {
    path: '/',
    name: 'Home',
    component: HomeMobile
  },
  {
    path: '/transfer_details/:xhHash/:type',
    component: TransferDetailsMobile
  },
  {
    path: '/contracts',
    component: ContractsMobile
  },
  {
    path: '/contractOverview/:contractAddress',
    component: ContractOverviewMobile
  },
  {
    path: '/blocks',
    component: BlocksMobile
  },
  {
    path: '/blockDetails/:height',
    component: BlockDetailsMobile
  },
  {
    path: '/transaction',
    component: TransactionMobile
  },
  {
    path: '/address',
    component: AddressMobile
  },
  {
    path: '/richlist',
    component: RichlistMobile
  },
  {
    path: '/assets',
    component: AssetsMobile
  },
  {
    path: '/tokens',
    component: TokenMobile
  }
]

export default new Router({
  routes : device.desktop() ? routes : routesMobile,
  scrollBehavior (to, from, savedPosition) {
    return { x: 0, y: 0 }
  }
});


