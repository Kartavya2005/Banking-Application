import DashboardCard from "../DashboardCard";
import {
    Wallet,
    Landmark,
    PiggyBank,
    CreditCard,
} from "lucide-react";

const AccountSummary = ({ accounts = [] }) => {

    const totalBalance = accounts.reduce(
        (sum, account) => sum + Number(account.balance || 0),
        0
    );

    const savings = accounts.filter(
        (a) => a.accountType === "SAVINGS"
    ).length;

    const current = accounts.filter(
        (a) => a.accountType === "CURRENT"
    ).length;

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">

            <DashboardCard
                title="Total Accounts"
                value={accounts.length}
                icon={CreditCard}
                color="text-blue-600"
            />

            <DashboardCard
                title="Total Balance"
                value={new Intl.NumberFormat("en-IN", {
                    style: "currency",
                    currency: "INR",
                }).format(totalBalance)}
                icon={Wallet}
                color="text-green-600"
            />

            <DashboardCard
                title="Savings Accounts"
                value={savings}
                icon={PiggyBank}
                color="text-purple-600"
            />

            <DashboardCard
                title="Current Accounts"
                value={current}
                icon={Landmark}
                color="text-orange-600"
            />

        </div>
    );
};

export default AccountSummary;