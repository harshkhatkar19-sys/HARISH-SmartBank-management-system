# Deployment

This repo is set up as a monorepo:

- Backend: `banking` on Render
- Frontend: `bank-frontend` on Vercel
- Database: external hosted MySQL

## 1. Prepare MySQL

Create a hosted MySQL database with any provider that gives you a public MySQL host, username, and password.

Run this SQL file against that database:

```sql
banking/database/schema.sql
```

The default admin login inserted by the schema is:

```text
username: admin
password: admin123
```

## 2. Deploy Backend on Render

Push this repo to GitHub, then create the backend from the root `render.yaml` Blueprint, or create a Render Web Service manually with these settings:

```text
Runtime: Docker
Dockerfile Path: banking/Dockerfile
Docker Context: banking
```

Set these Render environment variables:

```text
DB_URL=jdbc:mysql://YOUR_DB_HOST:3306/harishsmartbank
DB_USER=YOUR_DB_USER
DB_PASSWORD=YOUR_DB_PASSWORD
```

After deploy, Render will give you a URL like:

```text
https://harish-smart-bank-api.onrender.com
```

Your API base URL will be:

```text
https://harish-smart-bank-api.onrender.com/api
```

## 3. Deploy Frontend on Vercel

Import the same GitHub repo into Vercel and set:

```text
Root Directory: bank-frontend
Framework Preset: Create React App
Build Command: npm run build
Output Directory: build
```

Set this Vercel environment variable:

```text
REACT_APP_API_BASE_URL=https://YOUR_RENDER_BACKEND_URL/api
```

Redeploy the frontend after setting the environment variable.
