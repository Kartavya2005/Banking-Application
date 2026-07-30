const TOKEN_KEY = "jwtToken";

/**
 * Save JWT token to localStorage
 * @param {string} token
 */
export const saveToken = (token) => {
    localStorage.setItem(TOKEN_KEY, token);
};

/**
 * Get JWT token from localStorage
 * @returns {string|null}
 */
export const getToken = () => {
    return localStorage.getItem(TOKEN_KEY);
};

/**
 * Remove JWT token from localStorage
 */
export const removeToken = () => {
    localStorage.removeItem(TOKEN_KEY);
};

/**
 * Check whether a JWT token exists
 * @returns {boolean}
 */
export const hasToken = () => {
    return !!localStorage.getItem(TOKEN_KEY);
};