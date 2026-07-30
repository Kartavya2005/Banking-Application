function InputField({
                        label,
                        type = "text",
                        name,
                        placeholder,
                        register,
                        error,
                        disabled = false,
                        icon: Icon,
                    }) {
    return (
        <div className="flex flex-col gap-2">

            <label
                htmlFor={name}
                className="text-sm font-semibold text-gray-700"
            >
                {label}
            </label>

            <div className="relative">

                {Icon && (
                    <Icon
                        size={20}
                        className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
                    />
                )}

                    <input
                        id={name}
                        type={type}
                        placeholder={placeholder}
                        disabled={disabled}
                        {...register(name)}
                        className={`
                        w-full
                        rounded-lg
                        border
                        px-4
                        py-3
                        outline-none
                        transition
                        ${
                            Icon
                                ? "pl-11"
                                : ""
                        }
                        ${
                            error
                                ? "border-red-500 focus:ring-2 focus:ring-red-400"
                                : "border-gray-300 focus:border-blue-500 focus:ring-2 focus:ring-blue-300"
                        }
                        ${
                            disabled
                                ? "cursor-not-allowed bg-gray-100"
                                : "bg-white"
                        }
                    `}
                    />

            </div>

            {error && (
                <span className="text-sm text-red-500">
                    {error.message}
                </span>
            )}

        </div>
    );
}

export default InputField;