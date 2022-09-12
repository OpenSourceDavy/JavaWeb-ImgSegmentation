import '../../../utils/index.mjs';
import { buildProps, definePropType } from '../../../utils/vue/props.mjs';
import { mutable } from '../../../utils/typescript.mjs';

const tabBarProps = buildProps({
  tabs: {
    type: definePropType(Array),
    default: () => mutable([])
  }
});

export { tabBarProps };
//# sourceMappingURL=tab-bar.mjs.map
