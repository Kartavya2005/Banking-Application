import { useEffect, useState } from "react";
import * as accountService from "../../services/accountService";

import AccountSummary from "../../components/accounts/AccountSummary";
import AccountCard from "../../components/accounts/AccountCard";

const Accounts = () => {

    const [accounts, setAccounts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const fetchAccounts = async () => {

        try {

            setLoading(true);
            setError("");

            const data = await accountService.getMyAccounts();

            setAccounts(data || []);

        } catch (err) {

            console.error(err);

            setError(
                err?.response?.data?.message ||
                "Unable to load your accounts."
            );

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => {
        let ignore = false;

        async function loadAccounts() {
            try {
                setLoading(true);
                setError("");

                const data = await accountService.getMyAccounts();

                if (!ignore) {
                    setAccounts(data || []);
                }
            } catch (err) {
                if (!ignore) {
                    setError(
                        err?.response?.data?.message ||
                        "Unable to load your accounts."
                    );
                }
            } finally {
                if (!ignore) {
                    setLoading(false);
                }
            }
        }

        loadAccounts();

        return () => {
            ignore = true;
        };
    }, []);

    if (loading) {
        return (
            <div className="flex justify-center items-center h-72">
                <h2 className="text-lg font-semibold">
                    Loading Accounts...
                </h2>
            </div>
        );
    }

    if (error) {
        return (
            <div className="bg-red-100 text-red-700 rounded-lg p-4">
                {error}
            </div>
        );
    }

    return (

        <div className="space-y-8">

            <div className="flex justify-between items-center">

                <div>

                    <h1 className="text-3xl font-bold">
                        My Accounts
                    </h1>

                    <p className="text-gray-500">
                        View and manage all your bank accounts.
                    </p>

                </div>

                <button
                    onClick={fetchAccounts}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded-lg"
                >
                    Refresh
                </button>

            </div>

            <AccountSummary accounts={accounts} />

            {accounts.length === 0 ? (

                <div className="bg-white rounded-xl shadow p-12 text-center">

                    <h2 className="text-2xl font-semibold">
                        No Accounts Found
                    </h2>

                    <p className="text-gray-500 mt-3">
                        You don't have any bank accounts yet.
                    </p>

                </div>

            ) : (

                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">

                    {accounts.map((account) => (

                        <AccountCard
                            key={account.accountId}
                            account={account}
                        />

                    ))}

                </div>

            )}

        </div>

    );

};

export default Accounts;