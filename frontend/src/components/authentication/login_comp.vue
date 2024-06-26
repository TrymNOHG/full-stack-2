/*
AUTHOR = {Tomas Beranek, Eilert Wegner Hansen, Beka Dan},
TITLE = {Systemutvikling 2, Smartmat},
URL = {https://github.com/tomasbera/SmartMat/blob/main/frontend/src/components/Authentication/RegisterComponent.vue}
*/
<template>
  <div class="submit_form">
    <h2>{{ $t('titles.LOGIN') }}</h2>
    <form @submit.prevent="submit" :class="{ 'has-errors': has_err }">
      <div class="input_fields">
        <label for="email">{{ $t('placeholders.EMAIL') }}</label>
        <input
            type="text"
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
            @keypress.enter="submit"
        />
        <div v-if="errors && errors['password']" class="error-message">
          {{ errors["password"] }}
        </div>
        <p>
          {{submitMessage}}
        </p>
        <basic_button
            class="submit_button"
            @click="submit"
            :button_text="$t('buttons.LOGIN')"
        />
        <h4>
          <router-link to="/register">{{ $t('login_register_text.NO_ACCOUNT_MESSAGE') }}</router-link>
        </h4>
      </div>
    </form>
  </div>
</template>

<script>
import * as yup from "yup";
import { useField, useForm } from "vee-validate";
import { useUserStore } from "@/stores/counter.js";
import { ref } from "vue";
import router from "@/router/index.js";
import { loginUser } from "@/services/UserService.js";
import { RouterLink } from 'vue-router'
import Basic_button from "@/components/BasicComponents/basic_button.vue";
import {useI18n} from "vue-i18n";
import i18n from "@/lang/i18n.js";

export default {
  components: {
    Basic_button,
    RouterLink,
  },

  setup() {
    const store = useUserStore();
    const submitMessage = ref("");
    const { t } = useI18n();

    const validationSchema = yup.object({
      email: yup.string().required(t("error_messages.EMAIL_REQUIRED")).email(t("error_messages.EMAIL")),
      password: yup.string().required(t("error_messages.PASSWORD_REQUIRED")).min(8, t("error_messages.PASS_MUST")),
    });


    const { handleSubmit, errors, setFieldTouched, setFieldValue } = useForm({
      validationSchema,
      initialValues: {
        email: "",
        password: "",
      },
    });

    const { value: email } = useField("email");
    const { value: password } = useField("password");

    const submit = handleSubmit(async () => {
      const userLoginDTO = {
        emailOrUserName: email.value,
        password: password.value,
      };

      // Authorization: Basic

      await loginUser(userLoginDTO)
          .then(async (response) => {
            if (response !== undefined) {
              store.setToken(response.data.token);
              store.setTokenExpires(response.data.token_expiration);
              await store.fetchUserData();
              await router.push("/home");
            }
          })
          .catch((error) => {
            submitMessage.value = t("login_register_text.LOGIN_ERR");
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
      t,
      password,
      errors,
      submit,
      submitMessage,
      validationSchema,
      has_err,
      setFieldTouched,
      setFieldValue,
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
