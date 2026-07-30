class AuthResponse {
    constructor(token = "", role = "", email = "") {
        this.token = token;
        this.role = role;
        this.email = email;
    }
}

export default AuthResponse;