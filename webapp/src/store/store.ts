import {
    createStore,
    Store,
} from "vuex";

import {SearchModule} from "@/store/modules/Search.module";
import {FINDRModule} from "@/store/modules/FINDR.module";
import type {ComponentCustomProperties} from "vue";


export const store: Store<ComponentCustomProperties> = createStore({
    modules: {
        search: SearchModule,
        FINDR: FINDRModule,
    },
});