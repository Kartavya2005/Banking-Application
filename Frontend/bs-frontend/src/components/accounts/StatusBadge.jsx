const statusStyles = {
    ACTIVE: "bg-green-100 text-green-700",
    FROZEN: "bg-yellow-100 text-yellow-700",
    CLOSED: "bg-red-100 text-red-700",
};

const StatusBadge = ({ status }) => {
    return (
        <span
            className={`px-3 py-1 rounded-full text-xs font-semibold ${
                statusStyles[status] || "bg-gray-100 text-gray-700"
            }`}
        >
            {status}
        </span>
    );
};

export default StatusBadge;