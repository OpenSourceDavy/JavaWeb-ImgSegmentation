'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var shared = require('@vue/shared');
require('../../../utils/index.js');
require('../../../constants/index.js');
var index$2 = require('../../icon/index.js');
var iconsVue = require('@element-plus/icons-vue');
require('../../../tokens/index.js');
require('../../../hooks/index.js');
var tabNav = require('./tab-nav.js');
var props = require('../../../utils/vue/props.js');
var core = require('@vueuse/core');
var event = require('../../../constants/event.js');
var index = require('../../../hooks/use-deprecated/index.js');
var index$1 = require('../../../hooks/use-namespace/index.js');
var tabs = require('../../../tokens/tabs.js');
var aria = require('../../../constants/aria.js');

const tabsProps = props.buildProps({
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
    type: props.definePropType(Function),
    default: () => true
  },
  stretch: Boolean
});
const isPanelName = (value) => shared.isString(value) || core.isNumber(value);
const tabsEmits = {
  [event.UPDATE_MODEL_EVENT]: (name) => isPanelName(name),
  [event.INPUT_EVENT]: (name) => isPanelName(name),
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
    } else if (type === vue.Fragment || type === "template") {
      getPaneInstanceFromSlot(node, paneInstanceList);
    }
  });
  return paneInstanceList;
};
var Tabs = vue.defineComponent({
  name: "ElTabs",
  props: tabsProps,
  emits: tabsEmits,
  setup(props, {
    emit,
    slots,
    expose
  }) {
    const instance = vue.getCurrentInstance();
    index.useDeprecated({
      scope: "el-tabs",
      type: "Event",
      from: "input",
      replacement: "tab-change",
      version: "2.5.0",
      ref: "https://element-plus.org/en-US/component/tabs.html#tabs-events"
    }, vue.computed(() => {
      var _a;
      return shared.isFunction((_a = instance.vnode.props) == null ? void 0 : _a.onInput);
    }));
    const ns = index$1.useNamespace("tabs");
    const nav$ = vue.ref();
    const panes = vue.ref([]);
    const currentName = vue.ref(props.modelValue || props.activeName || "0");
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
      emit(event.INPUT_EVENT, value);
      emit(event.UPDATE_MODEL_EVENT, value);
      emit("tab-change", value);
    };
    const setCurrentName = (value) => {
      var _a;
      if (currentName.value === value)
        return;
      const canLeave = (_a = props.beforeLeave) == null ? void 0 : _a.call(props, value, currentName.value);
      if (shared.isPromise(canLeave)) {
        canLeave.then(() => {
          var _a2, _b;
          changeCurrentName(value);
          (_b = (_a2 = nav$.value) == null ? void 0 : _a2.removeFocus) == null ? void 0 : _b.call(_a2);
        }, shared.NOOP);
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
    vue.onUpdated(() => updatePaneInstances());
    vue.onMounted(() => updatePaneInstances());
    vue.watch(() => props.activeName, (modelValue) => setCurrentName(modelValue));
    vue.watch(() => props.modelValue, (modelValue) => setCurrentName(modelValue));
    vue.watch(currentName, async () => {
      var _a, _b;
      updatePaneInstances(true);
      await vue.nextTick();
      await ((_a = nav$.value) == null ? void 0 : _a.$nextTick());
      (_b = nav$.value) == null ? void 0 : _b.scrollToActiveTab();
    });
    vue.provide(tabs.tabsRootContextKey, {
      props,
      currentName,
      updatePaneState: (pane) => paneStatesMap[pane.uid] = pane
    });
    expose({
      currentName
    });
    return () => {
      const newButton = props.editable || props.addable ? vue.createVNode("span", {
        "class": ns.e("new-tab"),
        "tabindex": "0",
        "onClick": handleTabAdd,
        "onKeydown": (ev) => {
          if (ev.code === aria.EVENT_CODE.enter)
            handleTabAdd();
        }
      }, [vue.createVNode(index$2.ElIcon, {
        "class": ns.is("icon-plus")
      }, {
        default: () => [vue.createVNode(iconsVue.Plus, null, null)]
      })]) : null;
      const header = vue.createVNode("div", {
        "class": [ns.e("header"), ns.is(props.tabPosition)]
      }, [newButton, vue.createVNode(tabNav["default"], {
        "ref": nav$,
        "currentName": currentName.value,
        "editable": props.editable,
        "type": props.type,
        "panes": panes.value,
        "stretch": props.stretch,
        "onTabClick": handleTabClick,
        "onTabRemove": handleTabRemove
      }, null)]);
      const panels = vue.createVNode("div", {
        "class": ns.e("content")
      }, [vue.renderSlot(slots, "default")]);
      return vue.createVNode("div", {
        "class": [ns.b(), ns.m(props.tabPosition), {
          [ns.m("card")]: props.type === "card",
          [ns.m("border-card")]: props.type === "border-card"
        }]
      }, [...props.tabPosition !== "bottom" ? [header, panels] : [panels, header]]);
    };
  }
});

exports["default"] = Tabs;
exports.tabsEmits = tabsEmits;
exports.tabsProps = tabsProps;
//# sourceMappingURL=tabs.js.map
