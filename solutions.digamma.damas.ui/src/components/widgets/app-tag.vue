<template>
    <span class="mdl-chip" :class="{'mdl-chip--contact': icon}">
        <span v-if="icon" class="mdl-chip__contact mdl-color--accent">
            <app-icon :symbol="symbol" :solid="solid" size="small" theme="light"/>
        </span>
        <span class="mdl-chip__text">
            <slot/>
        </span>
    </span>
</template>

<script>
import AppIcon from "./app-icon";
export default {
    name: "AppTag",
    components: {AppIcon},
    props: {
        icon: String
    },
    computed: {
        symbol() {
            return this.icon ? this.icon.split(" ")[0] : null
        },
        solid() {
            return this.icon ? this.icon.split(" ")[1] === "solid" : false
        }
    }
}
</script>