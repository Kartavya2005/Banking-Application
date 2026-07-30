import { useState } from "react";
import { Eye, EyeOff, Lock } from "lucide-react";

function PasswordField({
                           label,
                           name,
                           placeholder,
                           register,
                           error,
                           disabled = false,
                       }) {
    const [showPassword, setShowPassword] = useState(false);

    return (
        <div className="flex flex-col gap-2">

            <label
                htmlFor={name}
                className="text-sm font-semibold text-gray-700"
            >
                {label}
            </label>

            <div className="relative">

                <Lock
                    size={20}
                    className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
                />

                <input
                    id={name}
                    type={showPassword ? "text" : "password"}
                    placeholder={placeholder}
                    disabled={disabled}
                    {...register(name)}
                    className={`
                        w-full
                        rounded-lg
                        border
                        border-gray-300
                        py-3
                        pl-11
                        pr-12
                        outline-none
                        transition
                        ${
                        error
                            ? "border-red-500 focus:ring-2 focus:ring-red-400"
                            : "focus:border-blue-500 focus:ring-2 focus:ring-blue-300"
                    }
                        ${
                        disabled
                            ? "cursor-not-allowed bg-gray-100"
                            : "bg-white"
                    }
                    `}
                />

                <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-blue-600"
                >
                    {showPassword ? (
                        <EyeOff size={20} />
                    ) : (
                        <Eye size={20} />
                    )}
                </button>

            </div>

            {error && (
                <span className="text-sm text-red-500">
                    {error.message}
                </span>
            )}

        </div>
    );
}

export default PasswordField;