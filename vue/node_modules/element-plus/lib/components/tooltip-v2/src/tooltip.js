'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

require('../../../utils/index.js');
var root = require('./root.js');
var trigger = require('./trigger.js');
var arrow = require('./arrow.js');
var content = require('./content.js');
var props = require('../../../utils/vue/props.js');

const tooltipV2Props = props.buildProps({
  ...root.tooltipV2RootProps,
  ...arrow.tooltipV2ArrowProps,
  ...trigger.tooltipV2TriggerProps,
  ...content.tooltipV2ContentProps,
  alwaysOn: Boolean,
  fullTransition: Boolean,
  transitionProps: {
    type: props.definePropType(Object),
    default: null
  },
  teleported: Boolean,
  to: {
    type: props.definePropType(String),
    default: "body"
  }
});

exports.tooltipV2Props = tooltipV2Props;
//# sourceMappingURL=tooltip.js.map
