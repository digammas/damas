<template>
    <span class="mdl-chip" :class="{'mdl-chip--contact': icon}">
        <span v-if="icon" class="mdl-chip__contact mdl-color--accent">
            <app-icon :symbol="icon" size="small" theme="light"/>
        </span>
        <span class="mdl-chip__text">
            <slot/>
        </span>
        <button
                v-if="action"
                type="button"
                class="mdl-chip__action"
                @click="$emit('action', $event)">
            <app-icon
                    :symbol="action"
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
    }
}
</script>