<template>
    <app-cell :span="2" class="icon-cell">
        <app-row align="center">
            <app-icon size="big" :theme="theme" :symbol="symbol" :solid="solid"/>
        </app-row>
        <app-row align="center">
            <router-link
                    :to="link || '#'" @click.native="$emit('click', $event)"
                    class="icon-text">
                {{text}}
            </router-link>
        </app-row>
    </app-cell>
</template>

<script>
import AppCell from "./widgets/app-cell";
import AppRow from "./widgets/app-row";
import AppIcon from "./widgets/app-icon";

export default {
    name: "FileIcon",
    props: {
        text: {
            type: String,
            required: true
        },
        link: [String, Object],
        symbol: String,
        theme: String,
        solid: Boolean
    },
    components: {AppIcon, AppRow, AppCell}
}
</script>

<style scoped>
.icon-cell {
    overflow: hidden;
}

.icon-text {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
</style>