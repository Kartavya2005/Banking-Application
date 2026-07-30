const TransactionTypeBadge = ({ type }) => {
    const styles = {
        DEPOSIT: "bg-green-100 text-green-700",
        WITHDRAWAL: "bg-red-100 text-red-700",
        TRANSFER: "bg-blue-100 text-blue-700",
        PAYMENT: "bg-purple-100 text-purple-700",
    };

    return (
        <span
            className={`px-3 py-1 rounded-full text-xs font-semibold ${
                styles[type] || "bg-gray-100 text-gray-700"
            }`}
        >
            {type}
        </span>
    );
};

export default TransactionTypeBadge;