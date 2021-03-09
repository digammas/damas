<template>
    <app-cell
            class="icon-cell"
            ref="cell"
            :span="2">
        <app-row align="center">
            <app-icon
                    size="big"
                    :theme="theme"
                    :symbol="symbol"
                    :solid="solid"/>
        </app-row>
        <app-row align="center">
            <router-link
                    :to="link || '#'"
                    @click.native="$emit('click', $event)"
                    ref="anchor"
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
    },
    mounted() {
        /* Set hint only if text is truncated */
        if (this.$refs.anchor.$el.offsetWidth < this.$refs.anchor.$el.scrollWidth) {
            this.$refs.cell.hint = this.text
        }
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
