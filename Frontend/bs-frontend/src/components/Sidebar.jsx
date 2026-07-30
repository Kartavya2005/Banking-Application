import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

import {
    LayoutDashboard,
    Landmark,
    ArrowLeftRight,
    Users,
    HandCoins,
    Bell,
    User,
    LogOut,
} from "lucide-react";


const Sidebar = () => {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    const menuItems = [
        {
            name: "Dashboard",
            icon: <LayoutDashboard size={20} />,
            path: "/customer/dashboard",
        },
        {
            name: "Accounts",
            icon: <Landmark size={20} />,
            path: "/customer/accounts",
        },
        {
            name: "Transactions",
            icon: <ArrowLeftRight size={20} />,
            path: "/customer/transactions",
        },
        {
            name: "Beneficiaries",
            icon: <Users size={20} />,
            path: "/customer/beneficiaries",
        },
        {
            name: "Loans",
            icon: <HandCoins size={20} />,
            path: "/customer/loans",
        },
        {
            name: "Notifications",
            icon: <Bell size={20} />,
            path: "/customer/notifications",
        },
        {
            name: "Profile",
            icon: <User size={20} />,
            path: "/customer/profile",
        },
    ];

    return (
        <aside className="w-64 min-h-screen bg-blue-900 text-white flex flex-col">
            {/* Logo */}
            <div className="p-6 border-b border-blue-800">
                <h1 className="text-2xl font-bold">
                    Banking System
                </h1>
            </div>

            {/* Navigation */}
            <nav className="flex-1 p-4">
                <ul className="space-y-2">
                    {menuItems.map((item) => (
                        <li key={item.path}>
                            <NavLink
                                to={item.path}
                                className={({ isActive }) =>
                                    `flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 ${
                                        isActive
                                            ? "bg-white text-blue-900 font-semibold"
                                            : "hover:bg-blue-800"
                                    }`
                                }
                            >
                                {item.icon}
                                <span>{item.name}</span>
                            </NavLink>
                        </li>
                    ))}
                </ul>
            </nav>

            {/* Logout */}
            <div className="p-4 border-t border-blue-800">
                <button
                    onClick={handleLogout}
                    className="flex items-center gap-3 w-full px-4 py-3 rounded-lg hover:bg-red-600 transition-colors"
                >
                    <LogOut size={20} />
                    Logout
                </button>
            </div>
        </aside>
    );
};

export default Sidebar;