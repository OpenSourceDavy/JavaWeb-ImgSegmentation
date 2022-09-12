import { Fragment, defineComponent, getCurrentInstance, computed, ref, onUpdated, onMounted, watch, nextTick, provide, createVNode, renderSlot } from 'vue';
import { isString, isFunction, isPromise, NOOP } from '@vue/shared';
import '../../../utils/index.mjs';
import '../../../constants/index.mjs';
import { ElIcon } from '../../icon/index.mjs';
import { Plus } from '@element-plus/icons-vue';
import '../../../tokens/index.mjs';
import '../../../hooks/index.mjs';
import TabNav from './tab-nav.mjs';
import { buildProps, definePropType } from '../../../utils/vue/props.mjs';
import { isNumber } from '@vueuse/core';
import { UPDATE_MODEL_EVENT, INPUT_EVENT } from '../../../constants/event.mjs';
import { useDeprecated } from '../../../hooks/use-deprecated/index.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { tabsRootContextKey } from '../../../tokens/tabs.mjs';
import { EVENT_CODE } from '../../../constants/aria.mjs';

const tabsProps = buildProps({
  type: {
    type: String,
    values: ["card", "border-card", ""],
    default: ""
  },
  activeName: {
    type: [String, Number],
    default: ""
  },
  closable: Boolean,
  addable: Boolean,
  modelValue: {
    type: [String, Number],
    default: ""
  },
  editable: Boolean,
  tabPosition: {
    type: String,
    values: ["top", "right", "bottom", "left"],
    default: "top"
  },
  beforeLeave: {
    type: definePropType(Function),
    default: () => true
  },
  stretch: Boolean
});
const isPanelName = (value) => isString(value) || isNumber(value);
const tabsEmits = {
  [UPDATE_MODEL_EVENT]: (name) => isPanelName(name),
  [INPUT_EVENT]: (name) => isPanelName(name),
  "tab-click": (pane, ev) => ev instanceof Event,
  "tab-change": (name) => isPanelName(name),
  edit: (paneName, action) => ["remove", "add"].includes(action),
  "tab-remove": (name) => isPanelName(name),
  "tab-add": () => true
};
const getPaneInstanceFromSlot = (vnode, paneInstanceList = []) => {
  const children = vnode.children || [];
  Array.from(children).forEach((node) => {
    let type = node.type;
    type = type.name || type;
    if (type === "ElTabPane" && node.component) {
      paneInstanceList.push(node.component);
    } else if (type === Fragment || type === "template") {
      getPaneInstanceFromSlot(node, paneInstanceList);
    }
  });
  return paneInstanceList;
};
var Tabs = defineComponent({
  name: "ElTabs",
  props: tabsProps,
  emits: tabsEmits,
  setup(props, {
    emit,
    slots,
    expose
  }) {
    const instance = getCurrentInstance();
    useDeprecated({
      scope: "el-tabs",
      type: "Event",
      from: "input",
      replacement: "tab-change",
      version: "2.5.0",
      ref: "https://element-plus.org/en-US/component/tabs.html#tabs-events"
    }, computed(() => {
      var _a;
      return isFunction((_a = instance.vnode.props) == null ? void 0 : _a.onInput);
    }));
    const ns = useNamespace("tabs");
    const nav$ = ref();
    const panes = ref([]);
    const currentName = ref(props.modelValue || props.activeName || "0");
    const paneStatesMap = {};
    const updatePaneInstances = (isForceUpdate = false) => {
      if (slots.default) {
        const children = instance.subTree.children;
        const content = Array.from(children).find(({
          props: props2
        }) => (props2 == null ? void 0 : props2.class) === ns.e("content"));
        if (!content)
          return;
        const paneInstanceList = getPaneInstanceFromSlot(content).map((paneComponent) => paneStatesMap[paneComponent.uid]);
        const panesChanged = !(paneInstanceList.length === panes.value.length && paneInstanceList.every((pane, index) => pane.uid === panes.value[index].uid));
        if (isForceUpdate || panesChanged) {
          panes.value = paneInstanceList;
        }
      } else if (panes.value.length !== 0) {
        panes.value = [];
      }
    };
    const changeCurrentName = (value) => {
      currentName.value = value;
      emit(INPUT_EVENT, value);
      emit(UPDATE_MODEL_EVENT, value);
      emit("tab-change", value);
    };
    const setCurrentName = (value) => {
      var _a;
      if (currentName.value === value)
        return;
      const canLeave = (_a = props.beforeLeave) == null ? void 0 : _a.call(props, value, currentName.value);
      if (isPromise(canLeave)) {
        canLeave.then(() => {
          var _a2, _b;
          changeCurrentName(value);
          (_b = (_a2 = nav$.value) == null ? void 0 : _a2.removeFocus) == null ? void 0 : _b.call(_a2);
        }, NOOP);
      } else if (canLeave !== false) {
        changeCurrentName(value);
      }
    };
    const handleTabClick = (tab, tabName, event) => {
      if (tab.props.disabled)
        return;
      setCurrentName(tabName);
      emit("tab-click", tab, event);
    };
    const handleTabRemove = (pane, ev) => {
      if (pane.props.disabled)
        return;
      ev.stopPropagation();
      emit("edit", pane.props.name, "remove");
      emit("tab-remove", pane.props.name);
    };
    const handleTabAdd = () => {
      emit("edit", void 0, "add");
      emit("tab-add");
    };
    onUpdated(() => updatePaneInstances());
    onMounted(() => updatePaneInstances());
    watch(() => props.activeName, (modelValue) => setCurrentName(modelValue));
    watch(() => props.modelValue, (modelValue) => setCurrentName(modelValue));
    watch(currentName, async () => {
      var _a, _b;
      updatePaneInstances(true);
      await nextTick();
      await ((_a = nav$.value) == null ? void 0 : _a.$nextTick());
      (_b = nav$.value) == null ? void 0 : _b.scrollToActiveTab();
    });
    provide(tabsRootContextKey, {
      props,
      currentName,
      updatePaneState: (pane) => paneStatesMap[pane.uid] = pane
    });
    expose({
      currentName
    });
    return () => {
      const newButton = props.editable || props.addable ? createVNode("span", {
        "class": ns.e("new-tab"),
        "tabindex": "0",
        "onClick": handleTabAdd,
        "onKeydown": (ev) => {
          if (ev.code === EVENT_CODE.enter)
            handleTabAdd();
        }
      }, [createVNode(ElIcon, {
        "class": ns.is("icon-plus")
      }, {
        default: () => [createVNode(Plus, null, null)]
      })]) : null;
      const header = createVNode("div", {
        "class": [ns.e("header"), ns.is(props.tabPosition)]
      }, [newButton, createVNode(TabNav, {
        "ref": nav$,
        "currentName": currentName.value,
        "editable": props.editable,
        "type": props.type,
        "panes": panes.value,
        "stretch": props.stretch,
        "onTabClick": handleTabClick,
        "onTabRemove": handleTabRemove
      }, null)]);
      const panels = createVNode("div", {
        "class": ns.e("content")
      }, [renderSlot(slots, "default")]);
      return createVNode("div", {
        "class": [ns.b(), ns.m(props.tabPosition), {
          [ns.m("card")]: props.type === "card",
          [ns.m("border-card")]: props.type === "border-card"
        }]
      }, [...props.tabPosition !== "bottom" ? [header, panels] : [panels, header]]);
    };
  }
});

export { Tabs as default, tabsEmits, tabsProps };
//# sourceMappingURL=tabs.mjs.map
