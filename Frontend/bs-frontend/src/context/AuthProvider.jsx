import { useState } from "react";
import { AuthContext } from "./AuthContext";
import { getToken, saveToken, removeToken } from "../utils/token";
import { getUser, saveUser, removeUser } from "../utils/user";

function AuthProvider({ children }) {
    const [user, setUser] = useState(() => {
        const token = getToken();
        const storedUser = getUser();

        return token && storedUser ? storedUser : null;
    });

    const login = (response) => {
        // TODO:
        // Update response.token if your backend uses a different field name.
        saveToken(response.token);
        saveUser(response);

        setUser(response);
    };

    const logout = () => {
        removeToken();
        removeUser();
        setUser(null);
    };

    const value = {
        user,
        loading: false,
        login,
        logout,
        isAuthenticated: !!user,
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}

export default AuthProvider;