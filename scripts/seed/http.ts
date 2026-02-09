import axios from "axios";

export const api = axios.create({
    baseURL: process.env.API_URL || "http://localhost:8080/api",
    headers: {
        "Content-Type": "application/json"
    }
});

export function setAuthToken(token: string) {
    api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}