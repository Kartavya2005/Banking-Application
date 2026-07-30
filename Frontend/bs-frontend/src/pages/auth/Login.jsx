import LoginForm from "../../components/auth/LoginForm";

function Login() {
    return (
        <div className="min-h-screen bg-slate-100 flex items-center justify-center px-4">

            <div className="grid w-full max-w-6xl overflow-hidden rounded-3xl bg-white shadow-2xl md:grid-cols-2">

                {/* Left Side */}

                <div className="hidden flex-col justify-center bg-gradient-to-br from-blue-700 to-blue-900 p-12 text-white md:flex">

                    <h1 className="mb-4 text-5xl font-bold">
                        BS Banking
                    </h1>

                    <p className="text-lg leading-8 text-blue-100">
                        Secure, Reliable and Fast Banking Solutions.
                    </p>

                    <div className="mt-12 space-y-4">

                        <div className="rounded-xl bg-white/10 p-4 backdrop-blur">
                            🔒 Secure Authentication
                        </div>

                        <div className="rounded-xl bg-white/10 p-4 backdrop-blur">
                            💳 Manage Multiple Accounts
                        </div>

                        <div className="rounded-xl bg-white/10 p-4 backdrop-blur">
                            💸 Instant Fund Transfer
                        </div>

                        <div className="rounded-xl bg-white/10 p-4 backdrop-blur">
                            📈 Smart Banking Dashboard
                        </div>

                    </div>

                </div>

                {/* Right Side */}

                <div className="flex items-center justify-center p-10">

                    <div className="w-full max-w-md">

                        <h2 className="text-4xl font-bold text-gray-800">
                            Welcome Back
                        </h2>

                        <p className="mt-2 mb-8 text-gray-500">
                            Sign in to continue to your banking account.
                        </p>

                        <LoginForm />

                    </div>

                </div>

            </div>

        </div>
    );
}

export default Login;