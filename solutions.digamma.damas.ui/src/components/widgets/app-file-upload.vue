<template>
    <div class="mdl-textfield mdl-js-textfield textfield--file" ref="textfield">
        <div class="mdl-cell--12-col">
            <label
                    :for="textFieldId"
                    class="mdl-textfield__label">
                {{fileSize || label}}
            </label>
            <input
                    type="text"
                    :id="textFieldId"
                    class="mdl-textfield__input"
                    ref="input"
                    readonly />
            <div class="mdl-button mdl-button--primary mdl-button--icon button--file">
                <app-icon symbol="file-upload" solid size="small"/>
                <input type="file" ref="upload" @change="onCharge"/>
            </div>
        </div>
    </div>
</template>

<script>
import AppIcon from "./app-icon";
export default {
    name: "AppFileUpload",
    components: {AppIcon},
    props: {
        id: {
            type: String,
            default() {
                this.$utils.randomId("file-upload-")
            }
        },
        label: String
    },
    data() {
        return {
            textFieldId: this.$utils.randomId("text-field-"),
            fileSize: null,
            file: null
        }
    },
    methods: {
        onCharge() {
            this.file = this.$refs.upload.files[0]
            this.fileSize = `${this.file.size} bytes`
            this.$emit("change", this.file.name)
        },
        getName() {
            return this.file ? this.file.name : null
        },
        getMimeType() {
            return this.file ? this.file.type : null
        },
        binaryPayload() {
            const reader = new FileReader();
            return new Promise((resolve, reject) => {
                try {
                    reader.onload = function (evt) {
                        resolve(evt.target.result)
                    }
                    reader.readAsArrayBuffer(this.file)
                } catch(e) { reject(e) }
            })

        }
    }
}
</script>

<style scoped>
.button--file input {
    cursor: pointer;
    height: 100%;
    right: 0;
    opacity: 0;
    position: absolute;
    top: 0;
    width: 300px;
    z-index: 4;
}

.textfield--file .mdl-textfield__input {
    box-sizing: border-box;
    width: calc(100% - 32px);
}

.textfield--file .button--file {
    right: 0;
}
</style>