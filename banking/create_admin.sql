USE harishsmartbank;

-- Insert an admin user if it doesn't exist
INSERT INTO accounts (account_number, holder_name, username, password, balance, account_type, status, role)
SELECT 'ADM-001', 'System Administrator', 'admin', 'admin123', 0.0, 'SAVINGS', 'ACTIVE', 'ADMIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE username='admin');
