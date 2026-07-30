import { useEffect, useState } from "react";
import { RefreshCw } from "lucide-react";

import * as accountService from "../../services/accountService";
import * as transactionService from "../../services/transactionService";

import TransactionCard from "../../components/transactions/TransactionCard";

const Transactions = () => {
    const [accounts, setAccounts] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState("");
    const [transactions, setTransactions] = useState([]);

    const [loading, setLoading] = useState(true);
    const [transactionLoading, setTransactionLoading] = useState(false);
    const [error, setError] = useState("");

    const loadTransactions = async (accountNumber) => {
        if (!accountNumber) return; // Don't fetch if no account number is selected
        
        try {
            setTransactionLoading(true);

            const data = await transactionService.getAccountTransactions(
                accountNumber
            );

            setTransactions(data || []);
        } catch (err) {
            console.error(err);
            setTransactions([]);
            // Don't show generic 403 as a global error, keep it localized to transactions empty state
        } finally {
            setTransactionLoading(false);
        }
    };

    const loadAccounts = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await accountService.getMyAccounts();

            setAccounts(data || []);

            if (data && data.length > 0) {
                const accountNumber = data[0].accountNumber;
                setSelectedAccount(accountNumber);
                await loadTransactions(accountNumber);
            }
        } catch (err) {
            console.error(err);
            setError("Failed to load accounts.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        let isMounted = true;
        
        (async () => {
            if (isMounted) await loadAccounts();
        })();
        
        return () => { isMounted = false; };
    }, []);

    const handleAccountChange = async (e) => {
        const accountNumber = e.target.value;

        setSelectedAccount(accountNumber);

        await loadTransactions(accountNumber);
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center h-64">
                <p className="text-gray-500 text-lg">
                    Loading transactions...
                </p>
            </div>
        );
    }

    return (
        <div className="space-y-6">

            <div className="flex items-center justify-between">

                <div>
                    <h1 className="text-3xl font-bold">
                        Transactions
                    </h1>

                    <p className="text-gray-500">
                        View all account transactions.
                    </p>
                </div>

                <button
                    onClick={() => loadTransactions(selectedAccount)}
                    className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg"
                    disabled={!selectedAccount}
                >
                    <RefreshCw size={18} />
                    Refresh
                </button>

            </div>

            {error && (
                <div className="bg-red-100 text-red-700 rounded-lg p-4">
                    {error}
                </div>
            )}

            {accounts.length > 0 && (
                <div className="bg-white p-5 rounded-xl shadow">

                    <label className="block mb-2 font-medium">
                        Select Account
                    </label>

                    <select
                        value={selectedAccount}
                        onChange={handleAccountChange}
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
            )}

            {transactionLoading ? (
                <div className="text-center py-10">
                    Loading transactions...
                </div>
            ) : transactions.length === 0 ? (
                <div className="bg-white rounded-xl shadow p-10 text-center text-gray-500">
                    No transactions found.
                </div>
            ) : (
                <div className="space-y-4">
                    {transactions.map((transaction) => (
                        <TransactionCard
                            key={transaction.transactionReference || Math.random()}
                            transaction={transaction}
                        />
                    ))}
                </div>
            )}

        </div>
    );
};

export default Transactions;