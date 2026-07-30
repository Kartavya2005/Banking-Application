import { Bell, UserCircle } from "lucide-react";
import { useAuth } from "../hooks/useAuth";

const Navbar = () => {
    const { user } = useAuth();

    return (
        <header className="h-16 bg-white shadow-sm border-b flex items-center justify-between px-6">
            {/* Left Section */}
            <div>
                <h2 className="text-2xl font-bold text-gray-800">
                    Customer Dashboard
                </h2>
                <p className="text-sm text-gray-500">
                    Welcome back!
                </p>
            </div>

            {/* Right Section */}
            <div className="flex items-center gap-6">

                {/* Notification */}
                <button className="relative text-gray-600 hover:text-blue-600 transition">
                    <Bell size={22} />
                    <span className="absolute -top-2 -right-2 h-5 w-5 rounded-full bg-red-500 text-white text-xs flex items-center justify-center">
                        0
                    </span>
                </button>

                {/* User Details */}
                <div className="flex items-center gap-3">
                    <UserCircle
                        size={42}
                        className="text-blue-700"
                    />

                    <div className="text-right">
                        <p className="font-semibold text-gray-800">
                            {user?.email ?? "Customer"}
                        </p>

                        <p className="text-sm text-gray-500">
                            {user?.role ?? "CUSTOMER"}
                        </p>
                    </div>
                </div>

            </div>
        </header>
    );
};

export default Navbar;