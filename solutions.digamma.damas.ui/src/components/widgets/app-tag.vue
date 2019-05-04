<template>
    <span class="mdl-chip" :class="{'mdl-chip--contact': icon}">
        <span v-if="icon" class="mdl-chip__contact mdl-color--accent">
            <app-icon :symbol="iconSymbol" :solid="iconSolid" size="small" theme="light"/>
        </span>
        <span class="mdl-chip__text">
            <slot/>
        </span>
        <button
                v-if="action"
                type="button"
                class="mdl-chip__action"
                @click="$emit('click', $event)">
            <app-icon
                    :symbol="actionSymbol" :solid="actionSolid"
                    theme="dark"/>
        </button>
    </span>
</template>

<script>
import AppIcon from "./app-icon";
export default {
    name: "AppTag",
    components: {AppIcon},
    props: {
        icon: String,
        action: String
    },
    computed: {
        iconSymbol() {
            return this.icon ? this.icon.split(" ")[0] : null
        },
        iconSolid() {
            return this.icon ? this.icon.split(" ")[1] === "solid" : false
        },
        actionSymbol() {
            return this.action ? this.action.split(" ")[0] : null
        },
        actionSolid() {
            return this.action ? this.action.split(" ")[1] === "solid" : false
        }
    }
}
</script>