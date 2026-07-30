import { useEffect, useMemo, useState } from "react";

import * as accountService from "../../services/accountService";
import * as transactionService from "../../services/transactionService";

import TransferConfirmationModal from "../../components/transfer/TransferConfirmationModal";
import TransferReceipt from "../../components/transfer/TransferReceipt";

const Transfer = () => {

    const [accounts, setAccounts] = useState([]);

    const [loading, setLoading] = useState(false);
    const [pageLoading, setPageLoading] = useState(true);

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const [showConfirmModal, setShowConfirmModal] = useState(false);
    const [showReceipt, setShowReceipt] = useState(false);

    const [receiptData, setReceiptData] = useState(null);

    const [form, setForm] = useState({
        fromAccountNumber: "",
        toAccountNumber: "",
        amount: "",
        description: "",
    });


    useEffect(() => {
        let ignore = false;

        async function fetchAccounts() {
            try {
                setPageLoading(true);

                const data = await accountService.getMyAccounts();

                if (ignore) return;

                setAccounts(data);

                if (data.length > 0) {
                    setForm((prev) => ({
                        ...prev,
                        fromAccountNumber:
                            prev.fromAccountNumber ||
                            data[0].accountNumber,
                    }));
                }
            } catch (err) {
                if (!ignore) {
                    setError("Unable to load your bank accounts.");
                }
            } finally {
                if (!ignore) {
                    setPageLoading(false);
                }
            }
        }

        fetchAccounts();

        return () => {
            ignore = true;
        };
    }, []);

    const selectedAccount = useMemo(() => {

        return accounts.find(
            (a) => a.accountNumber === form.fromAccountNumber
        );

    }, [accounts, form.fromAccountNumber]);

    const formattedBalance = useMemo(() => {

        return new Intl.NumberFormat("en-IN", {
            style: "currency",
            currency: "INR",
        }).format(Number(selectedAccount?.balance || 0));

    }, [selectedAccount]);

    const handleChange = (e) => {

        const { name, value } = e.target;

        setForm((prev) => ({
            ...prev,
            [name]: value,
        }));

        setError("");
        setSuccess("");
    };

    const validateTransfer = () => {

        if (!form.fromAccountNumber) {
            return "Please select an account.";
        }

        if (!form.toAccountNumber.trim()) {
            return "Beneficiary account number is required.";
        }

        if (form.toAccountNumber === form.fromAccountNumber) {
            return "Sender and beneficiary account cannot be same.";
        }

        if (!form.amount) {
            return "Please enter amount.";
        }

        if (Number(form.amount) <= 0) {
            return "Amount should be greater than zero.";
        }

        if (
            Number(form.amount) >
            Number(selectedAccount?.balance || 0)
        ) {
            return "Insufficient account balance.";
        }

        return null;
    };

    const handleReviewTransfer = (e) => {

        e.preventDefault();

        setError("");
        setSuccess("");

        const validation = validateTransfer();

        if (validation) {
            setError(validation);
            return;
        }

        setShowConfirmModal(true);

    };

    const confirmTransfer = async () => {

        try {

            setLoading(true);

            const response =
                await transactionService.transfer({
                    fromAccountNumber: form.fromAccountNumber,
                    toAccountNumber: form.toAccountNumber,
                    amount: Number(form.amount),
                    description: form.description,
                });

            setReceiptData(response);

            setShowConfirmModal(false);

            setShowReceipt(true);

            setSuccess("Transfer completed successfully.");

            const data = await accountService.getMyAccounts();

            setAccounts(data);

            if (data.length > 0) {
                setForm((prev) => ({
                    ...prev,
                    fromAccountNumber: prev.fromAccountNumber,
                }));
            }

        } catch (err) {

            console.error(err);

            setError(
                err.response?.data?.message ||
                "Transfer failed."
            );

        } finally {

            setLoading(false);

        }

    };

    const resetForm = () => {

        setForm((prev) => ({
            ...prev,
            toAccountNumber: "",
            amount: "",
            description: "",
        }));

        setReceiptData(null);

        setShowReceipt(false);

    };

    return (
        <>
            <div className="max-w-6xl mx-auto">

                <div className="mb-8">

                    <h1 className="text-3xl font-bold text-gray-800">
                        Transfer Money
                    </h1>

                    <p className="text-gray-500 mt-2">
                        Securely transfer money between bank accounts.
                    </p>

                </div>

                {pageLoading ? (

                    <div className="bg-white rounded-xl shadow p-12 text-center">

                        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>

                        <p className="mt-4 text-gray-500">
                            Loading Accounts...
                        </p>

                    </div>

                ) : (

                    <div className="grid lg:grid-cols-3 gap-8">

                        <div className="lg:col-span-2">

                            <div className="bg-white rounded-xl shadow p-8">

                                <h2 className="text-2xl font-semibold mb-6">
                                    Transfer Details
                                </h2>

                                {success && (
                                    <div className="mb-5 rounded-lg bg-green-100 text-green-700 p-4">
                                        {success}
                                    </div>
                                )}

                                {error && (
                                    <div className="mb-5 rounded-lg bg-red-100 text-red-700 p-4">
                                        {error}
                                    </div>
                                )}

                                <form
                                    onSubmit={handleReviewTransfer}
                                    className="space-y-6"
                                >

                                    <div>

                                        <label className="block mb-2 font-medium">
                                            From Account
                                        </label>

                                        <select
                                            name="fromAccountNumber"
                                            value={form.fromAccountNumber}
                                            onChange={handleChange}
                                            className="w-full border rounded-lg p-3"
                                        >

                                            {accounts.map((account) => (

                                                <option
                                                    key={account.accountNumber}
                                                    value={account.accountNumber}
                                                >
                                                    {account.accountType} • {account.accountNumber}
                                                </option>

                                            ))}

                                        </select>

                                    </div>

                                    <div>

                                        <label className="block mb-2 font-medium">
                                            Beneficiary Account Number
                                        </label>

                                        <input
                                            type="text"
                                            name="toAccountNumber"
                                            value={form.toAccountNumber}
                                            onChange={handleChange}
                                            className="w-full border rounded-lg p-3"
                                            placeholder="Enter beneficiary account number"
                                        />

                                    </div>

                                    <div>

                                        <label className="block mb-2 font-medium">
                                            Amount
                                        </label>

                                        <input
                                            type="number"
                                            name="amount"
                                            value={form.amount}
                                            onChange={handleChange}
                                            className="w-full border rounded-lg p-3"
                                            placeholder="Enter amount"
                                        />

                                    </div>

                                    <div>

                                        <label className="block mb-2 font-medium">
                                            Description
                                        </label>

                                        <textarea
                                            rows={4}
                                            name="description"
                                            value={form.description}
                                            onChange={handleChange}
                                            className="w-full border rounded-lg p-3"
                                            placeholder="Optional description"
                                        />

                                    </div>

                                    <button
                                        type="submit"
                                        className="w-full bg-blue-600 hover:bg-blue-700 text-white rounded-lg py-3 font-semibold transition"
                                    >
                                        Review Transfer
                                    </button>

                                </form>

                            </div>

                        </div>
                        <div>

                            <div className="bg-white rounded-xl shadow p-6 sticky top-6">

                                <h2 className="text-xl font-semibold mb-5">
                                    Account Summary
                                </h2>

                                <div className="rounded-xl bg-blue-50 border border-blue-100 p-5">

                                    <p className="text-sm text-gray-500">
                                        Available Balance
                                    </p>

                                    <h2 className="text-3xl font-bold text-blue-700 mt-2">
                                        {formattedBalance}
                                    </h2>

                                </div>

                                <div className="mt-6 space-y-4">

                                    <div className="flex justify-between">

                                        <span className="text-gray-500">
                                            Account Holder
                                        </span>

                                        <span className="font-semibold text-right">
                                            {selectedAccount?.accountHolderName}
                                        </span>

                                    </div>

                                    <div className="flex justify-between">

                                        <span className="text-gray-500">
                                            Account Number
                                        </span>

                                        <span className="font-semibold">
                                            {selectedAccount?.accountNumber}
                                        </span>

                                    </div>

                                    <div className="flex justify-between">

                                        <span className="text-gray-500">
                                            Account Type
                                        </span>

                                        <span className="font-semibold">
                                            {selectedAccount?.accountType}
                                        </span>

                                    </div>

                                    <div className="flex justify-between">

                                        <span className="text-gray-500">
                                            Branch
                                        </span>

                                        <span className="font-semibold">
                                            {selectedAccount?.branch}
                                        </span>

                                    </div>

                                    <div className="flex justify-between">

                                        <span className="text-gray-500">
                                            IFSC
                                        </span>

                                        <span className="font-semibold">
                                            {selectedAccount?.ifscCode}
                                        </span>

                                    </div>

                                </div>

                                <hr className="my-6" />

                                <h3 className="font-semibold text-lg mb-4">
                                    Transfer Summary
                                </h3>

                                <div className="space-y-4">

                                    <div className="flex justify-between">

                                        <span className="text-gray-500">
                                            To
                                        </span>

                                        <span className="font-medium text-right break-all">
                                            {form.toAccountNumber || "-"}
                                        </span>

                                    </div>

                                    <div className="flex justify-between">

                                        <span className="text-gray-500">
                                            Amount
                                        </span>

                                        <span className="font-bold text-green-600">

                                            {new Intl.NumberFormat("en-IN", {
                                                style: "currency",
                                                currency: "INR",
                                            }).format(Number(form.amount || 0))}

                                        </span>

                                    </div>

                                    <div className="flex justify-between">

                                        <span className="text-gray-500">
                                            Remaining Balance
                                        </span>

                                        <span
                                            className={`font-bold ${
                                                Number(form.amount || 0) >
                                                Number(selectedAccount?.balance || 0)
                                                    ? "text-red-600"
                                                    : "text-blue-700"
                                            }`}
                                        >
                                            {new Intl.NumberFormat("en-IN", {
                                                style: "currency",
                                                currency: "INR",
                                            }).format(
                                                Number(selectedAccount?.balance || 0) -
                                                Number(form.amount || 0)
                                            )}
                                        </span>

                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>

                )}

            </div>

            <TransferConfirmationModal
                open={showConfirmModal}
                loading={loading}
                transfer={form}
                fromAccount={selectedAccount}
                onCancel={() => setShowConfirmModal(false)}
                onConfirm={confirmTransfer}
            />

            <TransferReceipt
                open={showReceipt}
                transaction={receiptData}
                onClose={() => setShowReceipt(false)}
                onNewTransfer={resetForm}
            />
        </>
    );
};

export default Transfer;