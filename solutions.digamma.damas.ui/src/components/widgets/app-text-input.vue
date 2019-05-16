<template>
    <div
            class="mdl-textfield mdl-textfield--floating-label"
            :class="classes"
            ref="textfield">
        <div class="mdl-cell--12-col">
            <label
                    :for="id"
                    class="mdl-textfield__label">
                {{label}}
            </label>
            <input
                    :type="type"
                    :name="name || id"
                    :id="id"
                    :required="required"
                    :value="text || value"
                    @input="$emit('input', $event.target.value)"
                    class="mdl-textfield__input"
                    ref="input"
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
        value: String,
        floating: Boolean
    },
    computed: {
        classes() {
            return {
                'is-dirty': !this.floating,
                'mdl-js-textfield': this.floating
            }
        }
    },
    mounted() {
        if (this.floating) {
            window.componentHandler.upgradeElement(this.$refs.textfield)
        }
    },
    watch: {
        floating(value) {
            if (value) {
                window.componentHandler.upgradeElement(this.$refs.textfield)
            }
        }
    },
    methods: {
        setText(text) {
            this.text = text
        },
        getText() {
            return this.$refs.input.value
        }
    }
}
</script>

<style scoped>
.mdl-textfield {
    display: inline-block;
}
</style>