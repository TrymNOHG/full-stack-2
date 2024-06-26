/*
AUTHOR = {Tomas Beranek, Eilert Wegner Hansen, Beka Dan},
TITLE = {Systemutvikling 2, Smartmat},
URL = {https://github.com/tomasbera/SmartMat/blob/main/frontend/src/components/Authentication/RegisterComponent.vue}
*/
<template>
  <div class="submit_form">
    <h2>{{ $t('titles.REGISTER') }}</h2>
    <form @submit.prevent="submit" :class="{ 'has-errors': has_err }">
      <div class="input_fields">
        <label for="email">{{ $t('placeholders.EMAIL') }}</label>
        <input
            type="email"
            required
            v-model.trim="email"
            name="email"
            class="input-field"
            aria-labelledby="emailLabel"
            :class="{ 'error': errors && errors['email'] }"
            :placeholder="$t('placeholders.EMAIL')"
        />
        <div v-if="errors && errors['email']" class="error-message">
          {{ errors["email"] }}
        </div>

        <label for="username">{{ $t('placeholders.USERNAME') }}</label>
        <input
            type="text"
            required
            v-model.trim="username"
            name="username"
            class="input-field"
            aria-labelledby="usernameLabel"
            :class="{ 'error': errors && errors['username'] }"
            :placeholder="$t('placeholders.USERNAME')"
        />
        <div v-if="errors && errors['username']" class="error-message">
          {{ errors["username"] }}
        </div>

        <label for="password">{{ $t('placeholders.PASSWORD') }}</label>
        <input
            type="password"
            required
            v-model.trim="password"
            name="password"
            class="input-field"
            aria-labelledby="passwordLabel"
            :class="{ 'error': errors && errors['password'] }"
            :placeholder="$t('placeholders.PASSWORD')"
        />
        <div v-if="errors && errors['password']" class="error-message">
          {{ errors["password"] }}
        </div>

        <label for="confirm password">{{ $t('placeholders.CONFIRM_PASSWORD') }}</label>
        <input
            type="password"
            required
            v-model.trim="conf_password"
            name="conf_password"
            class="input-field"
            aria-labelledby="conf_passwordLabel"
            :class="{ 'error': errors && errors['conf_password'] }"
            :placeholder="$t('placeholders.CONFIRM_PASSWORD')"
            @keypress.enter="submit"
        />
        <div v-if="errors && errors['conf_password']" class="error-message">
          {{ errors["conf_password"] }}
        </div>
        <p>
          {{submitMessage}}
        </p>
        <basic_button
            class="submit_button"
            @click="submit"
            :button_text="$t('buttons.REGISTER')"
        />
        <h4>
          <router-link to="/login">{{ $t('messages.ALREADY_HAVE_ACCOUNT') }}</router-link>
        </h4>
      </div>
    </form>
  </div>
</template>


<script>
import * as yup from "yup";
import { useField, useForm } from "vee-validate";
import Basic_button from "@/components/BasicComponents/basic_button.vue";
import { ref } from "vue";
import router from "@/router/index.js";
import {useUserStore} from "@/stores/counter.js";
import {registerUser} from "@/services/UserService.js";
import {useI18n} from "vue-i18n";

export default {
  components: {Basic_button},

  setup() {
    const store = useUserStore();
    const submitMessage = ref("");
    const { t } = useI18n();

    const validationSchema = yup.object({
      email: yup.string().required("Email is required").email(t("error_messages.EMAIL")),
      username: yup.string().required("Username is required").min(4, t("error_messages.EMAIL_MUST")),
      password: yup.string().required("Password is required").min(8, t("error_messages.PASS_MUST")),
      conf_password: yup.string()
          .required("Confirm password")
          .oneOf([yup.ref('password'), null], 'Passwords must match')
    });

    const {handleSubmit, errors, setFieldTouched, setFieldValue} = useForm({
      validationSchema,
      initialValues: {
        email: "",
        username: "",
        password: "",
        conf_password: "",
      },
    });

    const {value: email} = useField("email")
    const {value: username} = useField("username");
    const {value: password} = useField("password");
    const {value: conf_password} = useField("conf_password");


    const submit = handleSubmit(async () => {
      const userData = {
        email: email.value,
        username: username.value,
        password: password.value,
      };

      await registerUser(userData)
        .then(async (response) => {
          if (response !== undefined) {
            store.setToken(response.data.token);
            store.setTokenExpires(response.data.token_expiration);
            await store.fetchUserData();
            await router.push("/");
          }
        })
        .catch((error) => {
          submitMessage.value = t("login_register_text.REGISTER_ERR");
          setTimeout(() => {
            submitMessage.value = "";
          }, 2000);
          console.warn("error", error);
        });

    });

    const has_err = () => {
      const keys = Object.keys(errors.value);
      return keys.length > 0;
    };

    return {
      email,
      username,
      password,
      conf_password,
      errors,
      submit,
      validationSchema,
      has_err,
      setFieldTouched,
      setFieldValue,
      submitMessage
    };
  }
}
</script>

<style scoped>
h2 {
  font-weight: bold;
}

.input_fields {
  gap: 10px;
  display: flex;
  flex-direction: column;
  text-align: left;
  align-items: center;
  justify-content: center;
  margin: 25px 10px 10px 20px;
}

.input-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
  font-size: 16px;
}

.submit_form {
  padding: 40px;
  width: 20%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: transparent;
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.25);
  margin-top: 2.5%;
}

.submit_button {
  margin-top: 20px;
  width: 100%;
}

.has-errors input.error {
  border-color: red;
}

.error-message {
  font-size: 12px;
  color: red;
}

@media only screen and (max-width: 428px) {
  .submit_form {
    padding: 10px;
    width: 90%;
    margin-top: 25%;
  }
}
</style>
