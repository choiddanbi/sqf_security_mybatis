import axios from "axios";

export const instance = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        Authorization: localStorage.getItem("accessToken"),
    }
});