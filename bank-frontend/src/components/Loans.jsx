import React, { useState, useCallback, useEffect } from 'react';
import { getLoans, applyLoan } from '../api';

function Loans({ user }) {
    const [loans, setLoans] = useState([]);
    const [amount, setAmount] = useState('');
    const [type, setType] = useState('PERSONAL');
    const [duration, setDuration] = useState('12');
    const accNo = user.accountNumber || user.accNo;

    const fetchLoans = useCallback(async () => {
        try {
            const data = await getLoans(accNo);
            setLoans(data);
        } catch (err) {
            console.error(err);
        }
    }, [accNo]);

    useEffect(() => {
        if (accNo) fetchLoans();
    }, [accNo, fetchLoans]);

    const handleApply = async (e) => {
        e.preventDefault();
        const interestRate = type === 'PERSONAL' ? 12.5 : type === 'HOME' ? 8.5 : 9.0;
        const loanData = {
            accountNumber: accNo,
            amount: parseFloat(amount),
            interestRate: interestRate,
            durationMonths: parseInt(duration),
            loanType: type
        };

        try {
            const res = await applyLoan(loanData);
            if (res === 'SUCCESS') {
                alert('Loan Application Submitted!');
                setAmount('');
                fetchLoans();
            }
        } catch (err) {
            alert(err.message);
        }
    };

    return (
        <div className="loans-container animate-fade-in">
            <h2 className="section-title">Loan Services</h2>

            <div className="loan-layout">
                <form className="glass-panel loan-form" onSubmit={handleApply}>
                    <h3>Apply for a Loan</h3>
                    <div className="form-group">
                        <label>Loan Type</label>
                        <select value={type} onChange={(e) => setType(e.target.value)}>
                            <option value="PERSONAL">Personal Loan (12.5%)</option>
                            <option value="HOME">Home Loan (8.5%)</option>
                            <option value="AUTO">Auto Loan (9.0%)</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Amount (₹)</label>
                        <input 
                            type="number" 
                            value={amount} 
                            onChange={(e) => setAmount(e.target.value)} 
                            placeholder="Enter amount"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Duration (Months)</label>
                        <select value={duration} onChange={(e) => setDuration(e.target.value)}>
                            <option value="12">12 Months</option>
                            <option value="24">24 Months</option>
                            <option value="36">36 Months</option>
                            <option value="60">60 Months</option>
                        </select>
                    </div>
                    <button type="submit" className="premium-btn neon-green full-width">Submit Application</button>
                </form>

                <div className="glass-panel loan-history">
                    <h3>My Loans</h3>
                    <div className="loan-list">
                        {loans.map(loan => (
                            <div key={loan.id} className={`loan-item status-${loan.status.toLowerCase()}`}>
                                <div className="loan-info">
                                    <span className="loan-type">{loan.loanType}</span>
                                    <span className="loan-amount">₹{loan.amount.toLocaleString()}</span>
                                </div>
                                <div className="loan-meta">
                                    <span>{loan.durationMonths} Months @ {loan.interestRate}%</span>
                                    <span className={`status-badge ${loan.status.toLowerCase()}`}>{loan.status}</span>
                                </div>
                            </div>
                        ))}
                        {loans.length === 0 && <p className="empty-msg">No loan applications found.</p>}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Loans;
