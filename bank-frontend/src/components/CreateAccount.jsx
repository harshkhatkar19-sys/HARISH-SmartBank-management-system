import React, { useState } from "react";
import { createAccount } from "../api";

function CreateAccount({ setPage }) {
    const [form, setForm] = useState({
        holderName: "",
        username: "",
        password: "",
        balance: "",
        accountType: "SAVINGS",
        aadhaarNumber: "",
        phoneNumber: "",
        fatherName: "",
        motherName: "",
        dob: ""
    });

    const [message, setMessage] = useState("");
    const [isSuccess, setIsSuccess] = useState(false);

    const heroImageUrl = "https://images.unsplash.com/photo-1501167786227-4cba60f6d58f?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80";

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setIsSuccess(false);

        try {
            const res = await createAccount(form);
            if (res.includes("Account created")) {
                setIsSuccess(true);
                setMessage("✅ Welcome to Harish Smart Bank! Your account is ready.");
                setForm({
                    holderName: "", username: "", password: "", balance: "",
                    accountType: "SAVINGS", aadhaarNumber: "", phoneNumber: "",
                    fatherName: "", motherName: "", dob: ""
                });
            } else {
                setMessage(res);
            }
        } catch (error) {
            setMessage(error.message);
        }
    };

    return (
        <div className="full-height-center">
            <img src={heroImageUrl} alt="Bank Hero" className="hero-bg" />

            <div className="glass-panel" style={{ maxWidth: '800px' }}>
                <h2 className="section-title" style={{ fontSize: '2.5rem', fontWeight: '800', marginBottom: '10px', textAlign: 'center' }}>Open Your Account</h2>
                <p style={{ textAlign: "center", marginBottom: "40px", color: "var(--text-secondary)" }}>Join the future of premium digital banking today.</p>
                
                {message && (
                    <div style={{ 
                        marginBottom: '30px', 
                        padding: '16px', 
                        borderRadius: '12px', 
                        background: isSuccess ? 'rgba(16, 185, 129, 0.1)' : 'rgba(239, 68, 68, 0.1)', 
                        color: isSuccess ? 'var(--success)' : 'var(--danger)', 
                        textAlign: 'center', 
                        fontWeight: '600',
                        border: `1px solid ${isSuccess ? 'rgba(16, 185, 129, 0.2)' : 'rgba(239, 68, 68, 0.2)'}`
                    }}>
                        {message}
                    </div>
                )}

                <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '30px' }}>
                    <div style={{ gridColumn: 'span 2' }}>
                        <label>Account Selection</label>
                        <select name="accountType" value={form.accountType} onChange={handleChange} required>
                            <option value="SAVINGS">Elite Savings Account (2.5% APY)</option>
                            <option value="CURRENT">Corporate Current Account</option>
                        </select>
                    </div>

                    <div>
                        <label>Legal Full Name</label>
                        <input type="text" name="holderName" placeholder="As per Identity Document" value={form.holderName} onChange={handleChange} required />
                    </div>

                    <div>
                        <label>Unique Username</label>
                        <input type="text" name="username" placeholder="Choose a username" value={form.username} onChange={handleChange} required />
                    </div>

                    <div>
                        <label>Secure Password</label>
                        <input type="password" name="password" placeholder="Minimum 8 characters" value={form.password} onChange={handleChange} required />
                    </div>

                    <div>
                        <label>Initial Deposit (₹)</label>
                        <input type="number" name="balance" placeholder="0.00" value={form.balance} onChange={handleChange} required min="0" />
                    </div>

                    <div>
                        <label>Aadhaar Number (12 digits)</label>
                        <input type="text" name="aadhaarNumber" placeholder="XXXX XXXX XXXX" value={form.aadhaarNumber} onChange={handleChange} required />
                    </div>

                    <div>
                        <label>Phone Number</label>
                        <input type="text" name="phoneNumber" placeholder="+91 XXXXX XXXXX" value={form.phoneNumber} onChange={handleChange} required />
                    </div>

                    <div>
                        <label>Father's Full Name</label>
                        <input type="text" name="fatherName" placeholder="Full Name" value={form.fatherName} onChange={handleChange} />
                    </div>

                    <div>
                        <label>Mother's Full Name</label>
                        <input type="text" name="motherName" placeholder="Full Name" value={form.motherName} onChange={handleChange} />
                    </div>

                    <div style={{ gridColumn: 'span 2' }}>
                        <label>Date of Birth</label>
                        <input type="date" name="dob" value={form.dob} onChange={handleChange} required />
                    </div>

                    <div style={{ gridColumn: 'span 2', display: 'flex', gap: '20px', marginTop: '10px' }}>
                        <button type="submit" className="premium-btn btn-cyan" style={{ flex: 2 }}>
                            Create My Account
                        </button>
                        <button type="button" className="premium-btn btn-outline" style={{ flex: 1 }} onClick={() => setPage("home")}>
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default CreateAccount;