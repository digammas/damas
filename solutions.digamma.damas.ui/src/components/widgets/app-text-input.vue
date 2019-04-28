<template>
    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" ref="textfield">
        <div class="mdl-cell--12-col">
            <label
                    :for="id"
                    class="mdl-textfield__label">
                {{label}}
            </label>
            <input
                    :type="type"
                    :name="nameOrId"
                    :id="id"
                    :required="required"
                    :value="text || value"
                    @input="$emit('input', $event.target.value)"
                    class="mdl-textfield__input"
                    v-bind="$attrs"/>
        </div>
    </div>
</template>

<script>
export default {
    name: "AppTextInput",
    inheritAttrs: false,
    data() {
        return {
            text: null
        }
    },
    props: {
        id: {
            type: String,
            default() {
                this.$utils.randomId("text-input-")
            }
        },
        name: {
            type: String
        },
        label: String,
        type: {
            type: String,
            default: "text",
            validator: [].includes.bind(["text", "password"])
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
    },
    mounted() {
        componentHandler.upgradeElement(this.$refs.textfield)
    },
    methods: {
        setText(text) {
            this.text = text
        }
    }
}
</script>

<style scoped>
.mdl-textfield {
    display: inline-block;
}
</style>