<template>
    <div class="mdl-textfield mdl-js-textfield mdl-grid">
        <label
                :for="id"
                class="mdl-textfield__label">
            Password
        </label>
        <input
                :type="type"
                :placeholder="placeholder"
                :name="nameOrId"
                :id="id"
                :required="required"
                :value="value"
                @input="$emit('input', $event.target.value)"
                class="mdl-textfield__input"
                v-bind="$attrs"/>
    </div>
</template>

<script>
    export default {
        name: "AppTextInput",
        inheritAttrs: false,
        props: {
            placeholder: String,
            id: {
                type: String,
                required: true
            },
            name: {
                type: String
            },
            label: String,
            type: {
                type: "text" | "password",
                default: "text"
            },
            required: {
                type: Boolean,
                default: false
            },
            value: String
        },
        computed: {
            nameOrId() {
                return this.name || this.id
            }
        }
    }
</script>

<style scoped>

</style>