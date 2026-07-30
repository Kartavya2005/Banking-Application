import React from 'react';

const DashboardCard = ({
                           title,
                           value,
                           icon,
                           color = "bg-blue-600",
                       }) => {
    
    const renderIcon = () => {
        if (!icon) return null;
        
        // If it's already a rendered React element (e.g. <Wallet size={28} />)
        if (React.isValidElement(icon)) {
            return icon;
        }
        
        // If it's a component type (like an uncalled function or object from lucide-react)
        // Note: some lucide icons are forwardRef objects which makes typeof icon === 'object'
        return React.createElement(icon);
    };

    return (
        <div className="bg-white rounded-xl shadow-md p-6 flex items-center justify-between hover:shadow-lg transition-shadow duration-300">
            <div>
                <p className="text-gray-500 text-sm font-medium">
                    {title}
                </p>

                <h2 className="text-3xl font-bold text-gray-800 mt-2">
                    {value}
                </h2>
            </div>

            <div
                className={`${color} h-14 w-14 rounded-full flex items-center justify-center text-white`}
            >
                {renderIcon()}
            </div>
        </div>
    );
};

export default DashboardCard;