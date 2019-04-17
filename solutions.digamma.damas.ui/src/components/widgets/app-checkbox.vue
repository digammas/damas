<template>
    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect">
        <span
                :for="id"
                class="mdl-checkbox__label">
            {{text}}
        </span>
        <input
                :id="id"
                type="checkbox"
                :checked="checked"
                :name="nameOrId"
                :value="value"
                @input="$emit('input', $event.target.value)"
                class="mdl-checkbox__input"
                v-bind="$attrs"/>
    </label>
</template>

<script>
    export default {
        name: "AppCheckbox",
        inheritAttrs: false,
        props: {
            id: {
                type: String,
                required: true
            },
            name: String,
            text: String,
            checked: {
                type: Boolean,
                default: false
            },
            value: Boolean
        },
        computed: {
            nameOrId() {
                return this.name || this.id
            }
        }
    }
</script>

<style scoped>
.mdl-checkbox {
    width: inherit;
}
</style>