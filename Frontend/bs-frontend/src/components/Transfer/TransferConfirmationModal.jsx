
const formatCurrency = (amount) => {
    return new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
    }).format(Number(amount || 0));
};

const TransferConfirmationModal = ({
                                       open,
                                       loading,
                                       transfer,
                                       fromAccount,
                                       onCancel,
                                       onConfirm,
                                   }) => {
    if (!open) return null;

    return (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4">
            <div className="bg-white rounded-2xl shadow-xl w-full max-w-lg">

                <div className="border-b px-6 py-4">
                    <h2 className="text-2xl font-bold">
                        Confirm Transfer
                    </h2>

                    <p className="text-gray-500 mt-1">
                        Please review the transfer details before
                        proceeding.
                    </p>
                </div>

                <div className="p-6 space-y-5">

                    <div className="bg-gray-50 rounded-xl p-4">

                        <div className="flex justify-between py-2">
                            <span className="text-gray-500">
                                From Account
                            </span>

                            <span className="font-semibold">
                                {transfer.fromAccountNumber}
                            </span>
                        </div>

                        <div className="flex justify-between py-2">
                            <span className="text-gray-500">
                                Account Holder
                            </span>

                            <span className="font-semibold">
                                {fromAccount?.accountHolderName}
                            </span>
                        </div>

                        <div className="flex justify-between py-2">
                            <span className="text-gray-500">
                                Account Type
                            </span>

                            <span className="font-semibold">
                                {fromAccount?.accountType}
                            </span>
                        </div>

                        <div className="flex justify-between py-2">
                            <span className="text-gray-500">
                                To Account
                            </span>

                            <span className="font-semibold">
                                {transfer.toAccountNumber}
                            </span>
                        </div>

                        <div className="flex justify-between py-2">
                            <span className="text-gray-500">
                                Amount
                            </span>

                            <span className="text-xl font-bold text-green-600">
                                {formatCurrency(transfer.amount)}
                            </span>
                        </div>

                        <div className="flex justify-between py-2">
                            <span className="text-gray-500">
                                Description
                            </span>

                            <span className="font-semibold text-right">
                                {transfer.description || "-"}
                            </span>
                        </div>

                    </div>

                    <div className="rounded-lg bg-yellow-50 border border-yellow-300 p-4">

                        <h3 className="font-semibold text-yellow-800 mb-2">
                            Important
                        </h3>

                        <ul className="text-sm text-yellow-700 list-disc ml-5 space-y-1">
                            <li>
                                Please verify the beneficiary account number.
                            </li>
                            <li>
                                This transaction cannot be reversed after
                                confirmation.
                            </li>
                            <li>
                                Ensure sufficient balance is available.
                            </li>
                        </ul>

                    </div>

                </div>

                <div className="border-t p-5 flex justify-end gap-3">

                    <button
                        onClick={onCancel}
                        disabled={loading}
                        className="px-5 py-2 rounded-lg border hover:bg-gray-100"
                    >
                        Cancel
                    </button>

                    <button
                        onClick={onConfirm}
                        disabled={loading}
                        className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-semibold"
                    >
                        {loading
                            ? "Processing..."
                            : "Confirm Transfer"}
                    </button>

                </div>

            </div>
        </div>
    );
};

export default TransferConfirmationModal;