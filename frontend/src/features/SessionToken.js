import { useUserStore } from "@/stores/counter.js";
import router from "@/router/index.js";

let mockToken = null;

export function setMockToken(token) {
    mockToken = token;
}

export default async function sessionToken() {
    if (mockToken) {
        return mockToken;
    }

    const sessionToken = useUserStore().getToken;
    if (sessionToken === null) {
        alert("Log in to access your profile!"); //TODO: make better
        await router.push("/login");
        throw new Error("Session token cannot be null. Login in again.");
    }

    return sessionToken;
}