declare const _default: import("vue").DefineComponent<{
    readonly label: import("element-plus/es/utils").BuildPropReturn<StringConstructor, "", unknown, unknown, unknown>;
    readonly name: import("element-plus/es/utils").BuildPropReturn<readonly [StringConstructor, NumberConstructor], "", unknown, unknown, unknown>;
    readonly closable: BooleanConstructor;
    readonly disabled: BooleanConstructor;
    readonly lazy: BooleanConstructor;
}, {
    COMPONENT_NAME: string;
    props: Readonly<import("@vue/shared").LooseRequired<Readonly<import("vue").ExtractPropTypes<{
        readonly label: import("element-plus/es/utils").BuildPropReturn<StringConstructor, "", unknown, unknown, unknown>;
        readonly name: import("element-plus/es/utils").BuildPropReturn<readonly [StringConstructor, NumberConstructor], "", unknown, unknown, unknown>;
        readonly closable: BooleanConstructor;
        readonly disabled: BooleanConstructor;
        readonly lazy: BooleanConstructor;
    }>> & {
        [x: string & `on${string}`]: ((...args: any[]) => any) | ((...args: unknown[]) => any) | undefined;
    }>>;
    instance: import("vue").ComponentInternalInstance;
    tabsRoot: import("element-plus/es/tokens").TabsRootContext;
    ns: {
        namespace: import("vue").ComputedRef<string>;
        b: (blockSuffix?: string) => string;
        e: (element?: string | undefined) => string;
        m: (modifier?: string | undefined) => string;
        be: (blockSuffix?: string | undefined, element?: string | undefined) => string;
        em: (element?: string | undefined, modifier?: string | undefined) => string;
        bm: (blockSuffix?: string | undefined, modifier?: string | undefined) => string;
        bem: (blockSuffix?: string | undefined, element?: string | undefined, modifier?: string | undefined) => string;
        is: {
            (name: string, state: boolean | undefined): string;
            (name: string): string;
        };
    };
    index: import("vue").Ref<string | undefined>;
    isClosable: import("vue").ComputedRef<boolean>;
    active: Readonly<import("vue").Ref<boolean>>;
    loaded: import("vue").Ref<boolean>;
    paneName: import("vue").ComputedRef<import("element-plus/es/utils").BuildPropType<readonly [StringConstructor, NumberConstructor], unknown, unknown> | undefined>;
    shouldBeRender: Readonly<import("vue").Ref<boolean>>;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, Record<string, any>, string, import("vue").VNodeProps & import("vue").AllowedComponentProps & import("vue").ComponentCustomProps, Readonly<import("vue").ExtractPropTypes<{
    readonly label: import("element-plus/es/utils").BuildPropReturn<StringConstructor, "", unknown, unknown, unknown>;
    readonly name: import("element-plus/es/utils").BuildPropReturn<readonly [StringConstructor, NumberConstructor], "", unknown, unknown, unknown>;
    readonly closable: BooleanConstructor;
    readonly disabled: BooleanConstructor;
    readonly lazy: BooleanConstructor;
}>>, {
    disabled: boolean;
    name: import("element-plus/es/utils").BuildPropType<readonly [StringConstructor, NumberConstructor], unknown, unknown>;
    label: string;
    closable: boolean;
    lazy: boolean;
}>;
export default _default;
