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

Vue.use(Router)
export default new Router({
  routes: [
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
    }
  ],
  scrollBehavior (to, from, savedPosition) {
    return { x: 0, y: 0 }
  }
});


