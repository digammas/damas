<template>
    <div
            class="mdl-textfield mdl-textfield--floating-label mdl-js-textfield"
            :class="{'is-textarea': isTextArea}"
            ref="textfield">
        <label
                :for="id"
                class="mdl-textfield__label">
            {{label}}
        </label>
        <component
                class="mdl-textfield__input"
                ref="input"
                v-bind="$attrs"
                :is="inputElementTag"
                :type="type"
                :rows="rows"
                :name="name || id"
                :id="id"
                :value="text || value"
                :class="{shrunk: action}"
                @input="$emit('input', $event.target.value)"/>
        <app-button
                toolbar
                flat
                class="action-button"
                v-if="action"
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
            default() { return `text-input-${this._uid}` },
        },
        name: {
            type: String
        },
        label: String,
        type: {
            type: String,
            default: 'text',
            validator: [].includes.bind(['text', 'password', 'textarea'])
        },
        required: {
            type: Boolean,
            default: false
        },
        lines: Number,
        value: String,
        action: String
    },
    computed: {
        inputElementTag() {
            return this.type === 'textarea' ? 'textarea' : 'input'
        },
        rows() {
            return this.type === 'textarea' && (this.lines || 1)
        },
        isTextArea() {
            console.log("isTextArea is " + (this.type === 'textarea'))
            return this.type === 'textarea'
        }
    },
    mounted() {
        this.$_update()
        this.$refs.input.required = this.required
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

.mdl-textfield.is-textarea {
    width: 100%;
}

input.shrunk,
textarea.shrunk {
    box-sizing: border-box;
    width: calc(100% - 32px);
}

.action-button {
    right: 0;
}
</style>
