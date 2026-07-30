import { Navigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

function RoleProtectedRoute({ children, allowedRoles }) {
    const { user, loading } = useAuth();

    if (loading) {
        return (
            <div className="flex min-h-screen items-center justify-center">
                <h2 className="text-xl font-semibold">Loading...</h2>
            </div>
        );
    }

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    if (!allowedRoles.includes(user.role)) {
        return <Navigate to="/login" replace />;
    }

    return children;
}

export default RoleProtectedRoute;