const USER_KEY = "loggedInUser";

/**
 * Save logged-in user details
 * @param {Object} user
 */
export const saveUser = (user) => {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
};

/**
 * Get logged-in user details
 * @returns {Object|null}
 */
export const getUser = () => {
    const user = localStorage.getItem(USER_KEY);
    return user ? JSON.parse(user) : null;
};

/**
 * Remove logged-in user details
 */
export const removeUser = () => {
    localStorage.removeItem(USER_KEY);
};

/**
 * Check whether user details exist
 * @returns {boolean}
 */
export const hasUser = () => {
    return !!localStorage.getItem(USER_KEY);
};