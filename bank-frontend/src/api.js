export const BASE_URL = process.env.REACT_APP_API_BASE_URL || "http://localhost:8080/api";

async function request(path, options = {}, expectJson = true) {
    const res = await fetch(`${BASE_URL}${path}`, options);
    const text = await res.text();

    if (!res.ok) {
        throw new Error(text || `Request failed with status ${res.status}`);
    }

    if (!expectJson) {
        return text;
    }

    return text ? JSON.parse(text) : null;
}

// ================= USER =================
export const login = (username, password) =>
    request("/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            username: username.trim(),
            password: password.trim()
        })
    });

// 🔥 ADDED: CREATE ACCOUNT
export const createAccount = (account) =>
    request("/create-account", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(account)
    }, false);

export const getBalance = (accNo) =>
    request(`/balance?accNo=${encodeURIComponent(accNo)}`);

export const deposit = (accNo, amount) =>
    request("/deposit", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ accNo, amount })
    }, false);

export const withdraw = (accNo, amount) =>
    request("/withdraw", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ accNo, amount })
    }, false);

export const transfer = (fromAcc, toAcc, amount) =>
    request("/transfer", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ fromAcc, toAcc, amount })
    }, false);

// ================= ADMIN =================
export const getTotalAccounts = () =>
    request("/admin/total-accounts");

export const getTotalBalance = () =>
    request("/admin/total-balance");

export const deleteAccount = (accNo) =>
    request(`/admin/delete/${encodeURIComponent(accNo)}`, {
        method: "DELETE"
    }, false);

export const blockAccount = (accNo) =>
    request(`/admin/block/${encodeURIComponent(accNo)}`, {
        method: "POST"
    }, false);

export const unblockAccount = (accNo) =>
    request(`/admin/unblock/${encodeURIComponent(accNo)}`, {
        method: "POST"
    }, false);

// ================= CARDS =================
export const getCards = (accNo) =>
    request(`/cards/${accNo}`);

export const issueCard = (accountNumber, holderName, cardType) =>
    request("/cards/issue", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ accountNumber, holderName, cardType })
    }, false);

export const updateCardStatus = (cardNumber, status) =>
    request("/cards/status", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cardNumber, status })
    }, false);
export const deleteCards = (accNo) =>
    request(`/cards/${encodeURIComponent(accNo)}`, {
        method: "DELETE"
    }, false);
export const updateCardPin = (cardNumber, pin) =>
    request("/cards/pin", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cardNumber, pin })
    }, false);

// ================= LOANS =================
export const applyLoan = (loan) =>
    request("/loans/apply", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loan)
    }, false);

export const getLoans = (accNo) =>
    request(`/loans/${accNo}`);

export const getAllLoans = () =>
    request("/loans/all");

export const updateLoanStatus = (loanId, status) =>
    request("/loans/status", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ loanId, status })
    }, false);

export const getTransactions = (accNo) =>
    request(`/transactions?accNo=${encodeURIComponent(accNo)}`);

export const searchAccounts = (name) =>
    request(`/admin/search?name=${encodeURIComponent(name)}`);
