export default function TransactionCard({ transaction }) {
    const isCredit = transaction.transactionType === "DEPOSIT";

    return (
        <div className="bg-white p-4 rounded-lg shadow border border-gray-100 flex justify-between items-center">
            <div>
                <p className="font-semibold">
                    {transaction.description || "Transaction"}
                </p>

                <p className="text-sm text-gray-500">
                    {new Date(transaction.transactionDate).toLocaleDateString(
                        "en-IN"
                    )}
                </p>
            </div>

            <div
                className={`font-bold text-lg ${
                    isCredit ? "text-green-600" : "text-red-600"
                }`}
            >
                {isCredit ? "+" : "-"}
                {new Intl.NumberFormat("en-IN", {
                    style: "currency",
                    currency: "INR",
                }).format(Number(transaction.amount))}
            </div>
        </div>
    );
}