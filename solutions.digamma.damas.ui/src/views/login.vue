<template>
    <div>
        <h1>User login</h1>
        <form>
            <div class="container">
                <label for="username"><b>Username</b></label>
                <input v-model="username" type="text" placeholder="Enter Username" name="username" id="username" equired>

                <label for="password"><b>Password</b></label>
                <input v-model="password" type="password" placeholder="Enter Password" name="password" id="password" required>

                <button type="button" @click="submit()">Login</button>
                <label>
                    <input type="checkbox" checked="checked" name="remember"> Remember me
                </label>
            </div>
        </form>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                username: "",
                password: ""
            }
        },
        methods: {
            submit() {
                this.username = "toto"
                this.password = "otot"
            }
        }
    }
</script>

<style scoped>

    form {
        border: 3px solid #f1f1f1;
        max-width: 400px;
        margin: auto;
    }

    /* Full-width inputs */
    input[type=text], input[type=password] {
        width: 100%;
        padding: 12px 20px;
        margin: 8px 0;
        display: inline-block;
        border: 1px solid #ccc;
        box-sizing: border-box;
    }

    /* Set a style for all buttons */
    button {
        background-color: #4CAF50;
        color: white;
        padding: 14px 20px;
        margin: 8px 0;
        border: none;
        cursor: pointer;
        width: 100%;
    }

    /* Add a hover effect for buttons */
    button:hover {
        opacity: 0.8;
    }

    /* Add padding to containers */
    .container {
        padding: 16px;
    }

    /* The "Forgot password" text */
    span.psw {
        float: right;
        padding-top: 16px;
    }

    /* Change styles for span on extra small screens */
    @media screen and (max-width: 300px) {
        span.password {
            display: block;
            float: none;
        }
    }

</style>