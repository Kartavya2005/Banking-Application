import { useParams } from "react-router-dom";

const AccountDetails = () => {
    const { accountNumber } = useParams();

    return (
        <div>
            <h1>Account Details</h1>
            <p>{accountNumber}</p>
        </div>
    );
};

export default AccountDetails;