import { Landmark, CreditCard, Building2 } from "lucide-react";
import StatusBadge from "./StatusBadge";

const AccountCard = ({ account }) => {
    const formattedBalance = new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
    }).format(account.balance);

    return (
        <div className="bg-white rounded-xl shadow-md p-6 hover:shadow-xl transition-all duration-300 border">

            <div className="flex justify-between items-center mb-5">

                <div className="flex items-center gap-3">
                    <CreditCard className="text-blue-600" size={28} />

                    <div>
                        <h2 className="font-bold text-lg">
                            {account.accountType}
                        </h2>

                        <p className="text-gray-500 text-sm">
                            {account.accountNumber}
                        </p>
                    </div>
                </div>

                <StatusBadge status={account.accountStatus} />
            </div>

            <div className="mb-6">

                <p className="text-gray-500 text-sm">
                    Available Balance
                </p>

                <h1 className="text-3xl font-bold text-green-600 mt-1">
                    {formattedBalance}
                </h1>

            </div>

            <div className="space-y-3 text-sm">

                <div className="flex justify-between">
                    <span className="text-gray-500">
                        Holder
                    </span>

                    <span className="font-medium">
                        {account.accountHolderName}
                    </span>
                </div>

                <div className="flex justify-between">

                    <span className="flex items-center gap-1 text-gray-500">
                        <Building2 size={15} />
                        Branch
                    </span>

                    <span>
                        {account.branch}
                    </span>

                </div>

                <div className="flex justify-between">

                    <span className="flex items-center gap-1 text-gray-500">
                        <Landmark size={15} />
                        IFSC
                    </span>

                    <span>
                        {account.ifscCode}
                    </span>

                </div>

            </div>

        </div>
    );
};

export default AccountCard;