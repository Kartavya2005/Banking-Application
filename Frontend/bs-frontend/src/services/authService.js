import axiosConfig from "../api/axiosConfig";

export const loginUser = async (credentials) => {
    try {
        const response = await axiosConfig.post('/auth/login', credentials);
        return response.data;
    } catch (error) {
        console.error("Login failed:", error);
        throw error;
    }
};

export const registerUser = async (userData) => {
    try {
        const response = await axiosConfig.post('/auth/register', userData);
        return response.data;
    } catch (error) {
        console.error("Registration failed:", error);
        throw error;
    }
};

export const logoutUser = async () => {
    try {
        const response = await axiosConfig.post('/auth/logout');
        return response.data;
    } catch (error) {
        console.error("Logout failed:", error);
        throw error;
    }
};