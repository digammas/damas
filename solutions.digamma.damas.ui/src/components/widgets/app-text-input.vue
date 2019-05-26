<template>
    <div
            class="mdl-textfield mdl-textfield--floating-label mdl-js-textfield"
            ref="textfield">
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
                :class="{shrunk: action}"
                ref="input"
                v-bind="$attrs"/>
        <app-button
                v-if="action"
                toolbar
                flat
                class="action-button"
                @click="$emit('action', $refs.input.value)">
            <app-icon
                    :symbol="action"
                    size="small"/>
        </app-button>
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
        action: String
    },
    mounted() {
        this.$_update()
        var textfield = this.$refs.textfield
        setTimeout(() => {
            let autofill = textfield.querySelectorAll('input:-webkit-autofill')
            if (autofill.length) {
                textfield.classList.add('is-dirty')
            }
        }, 500)
    },
    updated() {
        this.$_update()
    },
    watch: {
        value(value) {
            if (value) {
                this.$refs.textfield.classList.add('is-focused')
            }
        }
    },
    methods: {
        setText(text) {
            this.text = text
            this.$refs.textfield.classList.add('is-focused')
        },
        getText() {
            return this.$refs.input.value
        },
        $_update() {
            window.componentHandler.upgradeElement(this.$refs.textfield)
        }
    }
}
</script>

<style scoped>
.mdl-textfield {
    display: inline-block;
}

input.shrunk {
    box-sizing: border-box;
    width: calc(100% - 32px);
}

.action-button {
    right: 0;
}
</style>