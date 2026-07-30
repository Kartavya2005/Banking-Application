const formatCurrency = (amount) => {
    return new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
    }).format(Number(amount || 0));
};

const formatDate = (date) => {
    if (!date) return "-";

    return new Date(date).toLocaleString("en-IN", {
        dateStyle: "medium",
        timeStyle: "short",
    });
};

const TransferReceipt = ({
                             open,
                             transaction,
                             onClose,
                             onNewTransfer,
                         }) => {
    if (!open || !transaction) return null;

    return (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4">

            <div className="bg-white rounded-2xl shadow-xl w-full max-w-xl">

                <div className="bg-green-600 text-white rounded-t-2xl p-6">

                    <h2 className="text-3xl font-bold">
                        Transfer Successful
                    </h2>

                    <p className="mt-2 opacity-90">
                        Your transaction has been completed successfully.
                    </p>

                </div>

                <div className="p-6">

                    <div className="space-y-4">

                        <div className="flex justify-between border-b pb-3">
                            <span className="text-gray-500">
                                Reference Number
                            </span>

                            <span className="font-semibold">
                                {transaction.transactionReference}
                            </span>
                        </div>

                        <div className="flex justify-between border-b pb-3">
                            <span className="text-gray-500">
                                From Account
                            </span>

                            <span className="font-semibold">
                                {transaction.fromAccountNumber}
                            </span>
                        </div>

                        <div className="flex justify-between border-b pb-3">
                            <span className="text-gray-500">
                                To Account
                            </span>

                            <span className="font-semibold">
                                {transaction.toAccountNumber}
                            </span>
                        </div>

                        <div className="flex justify-between border-b pb-3">
                            <span className="text-gray-500">
                                Amount
                            </span>

                            <span className="text-xl font-bold text-green-600">
                                {formatCurrency(transaction.amount)}
                            </span>
                        </div>

                        <div className="flex justify-between border-b pb-3">
                            <span className="text-gray-500">
                                Transaction Type
                            </span>

                            <span className="font-semibold">
                                {transaction.transactionType}
                            </span>
                        </div>

                        <div className="flex justify-between border-b pb-3">
                            <span className="text-gray-500">
                                Description
                            </span>

                            <span className="font-semibold text-right">
                                {transaction.description || "-"}
                            </span>
                        </div>

                        <div className="flex justify-between">
                            <span className="text-gray-500">
                                Date & Time
                            </span>

                            <span className="font-semibold">
                                {formatDate(transaction.transactionDate)}
                            </span>
                        </div>

                    </div>

                </div>

                <div className="border-t p-5 flex justify-end gap-3">

                    <button
                        onClick={onClose}
                        className="border px-5 py-2 rounded-lg hover:bg-gray-100"
                    >
                        Close
                    </button>

                    <button
                        onClick={onNewTransfer}
                        className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-semibold"
                    >
                        New Transfer
                    </button>

                </div>

            </div>

        </div>
    );
};

export default TransferReceipt;