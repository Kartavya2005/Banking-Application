import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

import InputField from "../ui/InputField";
import PasswordField from "../ui/PasswordField";
import Button from "../ui/Button";

import LoginRequest from "../../dto/request/LoginRequest";
import { loginUser } from "../../services/authService";
import useAuth from "../../hooks/useAuth";

function LoginForm() {
    const navigate = useNavigate();
    const { login } = useAuth();

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm({
        defaultValues: {
            email: "",
            password: "",
        },
    });

    const onSubmit = async (data) => {
        try {
            const request = new LoginRequest(
                data.email,
                data.password
            );

            const response = await loginUser(request);

            login(response);

            toast.success("Login Successful");

            switch (response.role) {
                case "ADMIN":
                    navigate("/admin/dashboard");
                    break;

                case "EMPLOYEE":
                    navigate("/employee/dashboard");
                    break;

                case "CUSTOMER":
                    navigate("/customer/dashboard");
                    break;

                default:
                    navigate("/login");
            }
        } catch (error) {
            console.error(error);

            if (error.response) {
                toast.error(
                    error.response.data.message || "Invalid email or password."
                );
            } else {
                toast.error("Unable to connect to the server.");
            }
        }
    };

    return (
        <form
            onSubmit={handleSubmit(onSubmit)}
            className="space-y-6"
        >
            <InputField
                label="Email"
                name="email"
                type="email"
                placeholder="Enter your email"
                register={(name) =>
                    register(name, {
                        required: "Email is required",
                        pattern: {
                            value: /^\S+@\S+\.\S+$/,
                            message: "Please enter a valid email",
                        },
                    })
                }
                error={errors.email}
            />

            <PasswordField
                label="Password"
                name="password"
                placeholder="Enter your password"
                register={(name) =>
                    register(name, {
                        required: "Password is required",
                    })
                }
                error={errors.password}
            />

            <div className="flex items-center justify-between">
                <label className="flex items-center gap-2 text-sm text-gray-600">
                    <input
                        type="checkbox"
                        className="rounded border-gray-300"
                    />
                    Remember Me
                </label>

                <button
                    type="button"
                    className="text-sm font-semibold text-blue-600 hover:text-blue-800"
                >
                    Forgot Password?
                </button>
            </div>

            <Button
                type="submit"
                disabled={isSubmitting}
            >
                {isSubmitting ? "Signing In..." : "Sign In"}
            </Button>
        </form>
    );
}

export default LoginForm;