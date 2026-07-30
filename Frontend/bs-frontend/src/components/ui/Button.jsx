function Button({
                    children,
                    type = "button",
                    onClick,
                    disabled = false,
                }) {
    return (
        <button
            type={type}
            onClick={onClick}
            disabled={disabled}
            className="w-full rounded-lg bg-blue-600 py-3 font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:bg-gray-400"
        >
            {children}
        </button>
    );
}

export default Button;