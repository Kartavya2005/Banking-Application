import axiosInstance from "./axiosConfig";

/**
 * Login API
 * @param {Object} loginRequest
 * @returns {Promise}
 */
export const login = async (loginRequest) => {
    return await axiosInstance.post(
        "/auth/login",
        loginRequest
    );
};

/**
 * Register API
 * @param {Object} registerRequest
 * @returns {Promise}
 */
export const register = async (registerRequest) => {
    return await axiosInstance.post(
        "/auth/register",
        registerRequest
    );
};