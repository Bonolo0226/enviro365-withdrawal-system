const API_BASE = "/api";

// --- Load Portfolio ---
document.getElementById("loadPortfolioBtn").addEventListener("click", async () => {
    const investorId = document.getElementById("investorId").value;
    const infoDiv = document.getElementById("investorInfo");
    const tbody = document.querySelector("#productsTable tbody");

    infoDiv.textContent = "Loading...";
    tbody.innerHTML = "";

    try {
        const response = await fetch(`${API_BASE}/portfolio/${investorId}`);
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || "Failed to load portfolio.");
        }
        const portfolio = await response.json();

        infoDiv.textContent = `Investor: ${portfolio.investorName} (Age: ${portfolio.age})`;

        portfolio.products.forEach(product => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${product.productId}</td>
                <td>${product.productName}</td>
                <td>${product.productType}</td>
                <td>${product.balance.toFixed(2)}</td>
            `;
            tbody.appendChild(row);
        });

    } catch (err) {
        infoDiv.textContent = "";
        infoDiv.innerHTML = `<span class="error">${err.message}</span>`;
    }
});

// --- Submit Withdrawal ---
document.getElementById("withdrawalForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const productId = document.getElementById("productId").value;
    const amount = document.getElementById("amount").value;
    const messageDiv = document.getElementById("withdrawalMessage");

    messageDiv.textContent = "Processing...";

    try {
        const response = await fetch(`${API_BASE}/withdrawals`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ productId: Number(productId), amount: Number(amount) })
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || "Withdrawal failed.");
        }

        messageDiv.innerHTML = `<span class="success">
            Withdrawal approved! New balance: ${result.remainingBalance.toFixed(2)}
        </span>`;

    } catch (err) {
        messageDiv.innerHTML = `<span class="error">${err.message}</span>`;
    }
});

// --- Load Withdrawal History ---
document.getElementById("loadHistoryBtn").addEventListener("click", async () => {
    const productId = document.getElementById("historyProductId").value;
    const tbody = document.querySelector("#historyTable tbody");

    tbody.innerHTML = "";

    try {
        const response = await fetch(`${API_BASE}/withdrawals/product/${productId}`);
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || "Failed to load history.");
        }
        const history = await response.json();

        history.forEach(notice => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${notice.id}</td>
                <td>${notice.amount.toFixed(2)}</td>
                <td>${notice.status}</td>
                <td>${notice.requestDate}</td>
            `;
            tbody.appendChild(row);
        });

    } catch (err) {
        alert(err.message);
    }
});

// --- Download CSV ---
document.getElementById("downloadCsvBtn").addEventListener("click", () => {
    const investorId = document.getElementById("exportInvestorId").value;
    window.location.href = `${API_BASE}/withdrawals/export/${investorId}`;
});