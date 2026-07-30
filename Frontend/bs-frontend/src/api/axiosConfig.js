import axios from "axios";

import { getToken, removeToken } from "../utils/token";
import { removeUser } from "../utils/user";

const axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

// Request Interceptor
axiosInstance.interceptors.request.use(
    (config) => {
        const token = getToken();

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => Promise.reject(error)
);

// Response Interceptor
axiosInstance.interceptors.response.use(
    (response) => response,

    (error) => {
        if (error.response && error.response.status === 401) {
            removeToken();
            removeUser();

            window.location.href = "/login";
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;