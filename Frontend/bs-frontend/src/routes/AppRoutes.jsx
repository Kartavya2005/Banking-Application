import { Routes, Route, Navigate } from "react-router-dom";

import Login from "../pages/auth/Login";

import ProtectedRoute from "../components/ProtectedRoute";
import RoleProtectedRoute from "../components/RoleProtectedRoute";

import CustomerLayout from "../layouts/CustomerLayout";

import Dashboard from "../pages/customer/Dashboard";
import Accounts from "../pages/customer/Accounts";
import AccountDetails from "../pages/customer/AccountDetails";
import Transactions from "../pages/customer/Transactions";
import Beneficiaries from "../pages/customer/Beneficiaries";
import Transfer from "../pages/customer/Transfer";

const AppRoutes = () => {
    return (
        <Routes>

            {/* Public Route */}
            <Route path="/login" element={<Login />} />

            {/* Customer Routes */}
            <Route
                path="/customer"
                element={
                    <ProtectedRoute>
                        <RoleProtectedRoute allowedRoles={["CUSTOMER"]}>
                            <CustomerLayout />
                        </RoleProtectedRoute>
                    </ProtectedRoute>
                }
            >
                <Route
                    index
                    element={<Navigate to="dashboard" replace />}
                />

                <Route
                    path="dashboard"
                    element={<Dashboard />}
                />

                <Route
                    path="accounts"
                    element={<Accounts />}
                />

                <Route
                    path="accounts/:accountNumber"
                    element={<AccountDetails />}
                />

                <Route
                    path="transactions"
                    element={<Transactions />}
                />

                <Route
                    path="beneficiaries"
                    element={<Beneficiaries />}
                />

                <Route
                    path="transfer"
                    element={<Transfer />}
                />
            </Route>

            {/* Default Route */}
            <Route
                path="/"
                element={<Navigate to="/login" replace />}
            />

            {/* 404 */}
            <Route
                path="*"
                element={<Navigate to="/login" replace />}
            />

        </Routes>
    );
};

export default AppRoutes;