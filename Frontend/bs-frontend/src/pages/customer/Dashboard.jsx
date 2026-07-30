import {
    Wallet,
    Landmark,
    HandCoins,
    Bell,
    Send,
    UserPlus,
    FileText,
    CreditCard,
} from "lucide-react";

import DashboardCard from "../../components/DashboardCard";

const Dashboard = () => {

    const recentTransactions = [
        {
            id: 1,
            type: "Transfer",
            amount: "₹5,000",
            status: "Success",
        },
        {
            id: 2,
            type: "Deposit",
            amount: "₹12,000",
            status: "Success",
        },
        {
            id: 3,
            type: "Withdrawal",
            amount: "₹2,500",
            status: "Pending",
        },
        {
            id: 4,
            type: "Loan EMI",
            amount: "₹8,500",
            status: "Success",
        },
    ];

    const quickActions = [
        {
            title: "Transfer Money",
            icon: <Send size={22} />,
        },
        {
            title: "Add Beneficiary",
            icon: <UserPlus size={22} />,
        },
        {
            title: "View Statement",
            icon: <FileText size={22} />,
        },
        {
            title: "Apply Loan",
            icon: <CreditCard size={22} />,
        },
    ];

    return (
        <div className="space-y-8">

            {/* Dashboard Heading */}
            <div>
                <h1 className="text-3xl font-bold text-gray-800">
                    Dashboard
                </h1>

                <p className="text-gray-500 mt-1">
                    Welcome to your Banking Dashboard
                </p>
            </div>

            {/* Dashboard Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">

                <DashboardCard
                    title="Total Balance"
                    value="₹2,45,500"
                    icon={<Wallet size={28} />}
                    color="bg-green-600"
                />

                <DashboardCard
                    title="Accounts"
                    value="2"
                    icon={<Landmark size={28} />}
                    color="bg-blue-600"
                />

                <DashboardCard
                    title="Active Loans"
                    value="1"
                    icon={<HandCoins size={28} />}
                    color="bg-orange-500"
                />

                <DashboardCard
                    title="Notifications"
                    value="3"
                    icon={<Bell size={28} />}
                    color="bg-red-500"
                />

            </div>

            {/* Bottom Section */}
            <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">

                {/* Recent Transactions */}
                <div className="xl:col-span-2 bg-white rounded-xl shadow-md">

                    <div className="border-b px-6 py-4">
                        <h2 className="text-xl font-semibold">
                            Recent Transactions
                        </h2>
                    </div>

                    <div className="overflow-x-auto">

                        <table className="w-full">

                            <thead className="bg-gray-100">

                            <tr>
                                <th className="text-left px-6 py-3">
                                    Type
                                </th>

                                <th className="text-left px-6 py-3">
                                    Amount
                                </th>

                                <th className="text-left px-6 py-3">
                                    Status
                                </th>
                            </tr>

                            </thead>

                            <tbody>

                            {recentTransactions.map((transaction) => (

                                <tr
                                    key={transaction.id}
                                    className="border-b hover:bg-gray-50"
                                >

                                    <td className="px-6 py-4">
                                        {transaction.type}
                                    </td>

                                    <td className="px-6 py-4">
                                        {transaction.amount}
                                    </td>

                                    <td className="px-6 py-4">

                                            <span
                                                className={`px-3 py-1 rounded-full text-sm font-medium ${
                                                    transaction.status === "Success"
                                                        ? "bg-green-100 text-green-700"
                                                        : "bg-yellow-100 text-yellow-700"
                                                }`}
                                            >
                                                {transaction.status}
                                            </span>

                                    </td>

                                </tr>

                            ))}

                            </tbody>

                        </table>

                    </div>

                </div>

                {/* Quick Actions */}

                <div className="bg-white rounded-xl shadow-md">

                    <div className="border-b px-6 py-4">

                        <h2 className="text-xl font-semibold">
                            Quick Actions
                        </h2>

                    </div>

                    <div className="p-6 grid gap-4">

                        {quickActions.map((action) => (

                            <button
                                key={action.title}
                                className="flex items-center gap-4 p-4 rounded-lg border hover:bg-blue-50 hover:border-blue-500 transition"
                            >
                                <div className="text-blue-700">
                                    {action.icon}
                                </div>

                                <span className="font-medium">
                                    {action.title}
                                </span>

                            </button>

                        ))}

                    </div>

                </div>

            </div>

        </div>
    );
};

export default Dashboard;