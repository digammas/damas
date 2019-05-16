<template>
    <app-cell
            :span="2"
            class="icon-cell"
            :hint="text">
        <app-row align="center">
            <app-icon
                    size="big"
                    :theme="theme"
                    :symbol="symbol"
                    :solid="solid"/>
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
export default {
    name: "IconFile",
    props: {
        text: {
            type: String,
            required: true
        },
        link: [String, Object],
        symbol: String,
        theme: String,
        solid: Boolean
    }
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