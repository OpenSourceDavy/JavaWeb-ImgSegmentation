'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

require('../../../utils/index.js');
var props = require('../../../utils/vue/props.js');
var typescript = require('../../../utils/typescript.js');

const tabBarProps = props.buildProps({
  tabs: {
    type: props.definePropType(Array),
    default: () => typescript.mutable([])
  }
});

exports.tabBarProps = tabBarProps;
//# sourceMappingURL=tab-bar.js.map
