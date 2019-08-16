<template>
  <div class="wrap">
    <div v-for="(item, index) in tips" class="normal" :class="{'choice':item.show}" @click="tipClick(index)">
      {{item.name}}
    </div>
    <div class="empty"></div>
  </div>
</template>

<script>
  import bus from '../../utils/bus';
  export default {
    name: "tips",
    props: ['tips'],
    methods: {
      tipClick(index) {
        if (this.tips[index].show === false) {
          bus.$emit('tipChange',index);
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  .wrap {
    width: 100%;
    display: flex;
    .normal {
      padding: 8px;
      border-bottom: 1px solid rgba(255,255,255,.5);
      width: auto;
      color: rgba(255,255,255,.5);
      font-size: 16px;
      font-weight: bold;
      cursor: pointer;
      &:hover {
        color: black;;
      }
    }
    .choice {
      border: 1px solid rgba(255,255,255,.5);
      border-bottom: none;
      color: black;;
      border-radius: 5px 5px 0px 0px;
    }
    .empty {
      flex: 1;
      border-bottom: 1px solid rgba(255,255,255,.5);
    }
  }
</style>
