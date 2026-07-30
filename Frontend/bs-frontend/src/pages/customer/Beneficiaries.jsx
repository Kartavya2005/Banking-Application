import { UserPlus, Pencil, Trash2 } from "lucide-react";

const Beneficiaries = () => {

    const beneficiaries = [
        {
            id: 1,
            name: "Rahul Sharma",
            accountNumber: "1000000456",
            bank: "ABC Bank",
            ifsc: "ABCD0001234",
        },
        {
            id: 2,
            name: "Priya Patel",
            accountNumber: "1000000789",
            bank: "XYZ Bank",
            ifsc: "XYZB0005678",
        },
    ];

    return (
        <div className="space-y-6">

            <div className="flex justify-between items-center">

                <div>
                    <h1 className="text-3xl font-bold">
                        Beneficiaries
                    </h1>

                    <p className="text-gray-500 mt-1">
                        Manage your saved beneficiaries.
                    </p>
                </div>

                <button
                    className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-lg"
                >
                    <UserPlus size={20} />
                    Add Beneficiary
                </button>

            </div>

            <div className="bg-white rounded-xl shadow overflow-hidden">

                <table className="w-full">

                    <thead className="bg-gray-100">

                    <tr>
                        <th className="px-6 py-4 text-left">Name</th>
                        <th className="px-6 py-4 text-left">Account Number</th>
                        <th className="px-6 py-4 text-left">Bank</th>
                        <th className="px-6 py-4 text-left">IFSC</th>
                        <th className="px-6 py-4 text-center">Actions</th>
                    </tr>

                    </thead>

                    <tbody>

                    {beneficiaries.map((beneficiary) => (

                        <tr
                            key={beneficiary.id}
                            className="border-b hover:bg-gray-50"
                        >

                            <td className="px-6 py-4 font-medium">
                                {beneficiary.name}
                            </td>

                            <td className="px-6 py-4">
                                {beneficiary.accountNumber}
                            </td>

                            <td className="px-6 py-4">
                                {beneficiary.bank}
                            </td>

                            <td className="px-6 py-4">
                                {beneficiary.ifsc}
                            </td>

                            <td className="px-6 py-4">

                                <div className="flex justify-center gap-3">

                                    <button
                                        className="p-2 rounded-lg bg-yellow-100 text-yellow-700 hover:bg-yellow-200"
                                    >
                                        <Pencil size={18} />
                                    </button>

                                    <button
                                        className="p-2 rounded-lg bg-red-100 text-red-700 hover:bg-red-200"
                                    >
                                        <Trash2 size={18} />
                                    </button>

                                </div>

                            </td>

                        </tr>

                    ))}

                    </tbody>

                </table>

            </div>

        </div>
    );
};

export default Beneficiaries;