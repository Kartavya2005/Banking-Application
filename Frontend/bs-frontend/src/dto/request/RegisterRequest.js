class RegisterRequest {
    constructor(
        email = "",
        password = "",
        firstName = "",
        lastName = "",
        phoneNumber = "",
        address = "",
        gender = "",
        dob = ""
    ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
    }
}

export default RegisterRequest;