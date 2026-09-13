const API_BASE = "/api";

// --- Toast helper ---
function showToast(message, type = "success") {
    const container = document.getElementById("toastContainer");
    const toast = document.createElement("div");
    toast.className = `toast ${type}`;
    toast.textContent = message;
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 4000);
}

// --- Button loading state helper ---
function setButtonLoading(button, isLoading, loadingText = "Loading...") {
    if (isLoading) {
        button.dataset.originalText = button.querySelector(".btn-text").textContent;
        button.querySelector(".btn-text").textContent = loadingText;
        button.insertAdjacentHTML("afterbegin", `<span class="spinner"></span>`);
        button.disabled = true;
    } else {
        button.querySelector(".btn-text").textContent = button.dataset.originalText || button.querySelector(".btn-text").textContent;
        const spinner = button.querySelector(".spinner");
        if (spinner) spinner.remove();
        button.disabled = false;
    }
}

// --- Empty row helper ---
function emptyRow(tbody, colspan, text) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="${colspan}">${text}</td></tr>`;
}

// --- Load Portfolio ---
document.getElementById("loadPortfolioBtn").addEventListener("click", async () => {
    const investorId = document.getElementById("investorId").value;
    const infoDiv = document.getElementById("investorInfo");
    const tbody = document.querySelector("#productsTable tbody");
    const btn = document.getElementById("loadPortfolioBtn");

    setButtonLoading(btn, true, "Loading...");
    infoDiv.style.display = "none";
    emptyRow(tbody, 4, "Loading portfolio...");

    try {
        const response = await fetch(`${API_BASE}/portfolio/${investorId}`);
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || "Failed to load portfolio.");
        }
        const portfolio = await response.json();

        infoDiv.style.display = "flex";
        infoDiv.innerHTML = `
            <span class="name">${portfolio.investorName}</span>
            <span class="meta">Age ${portfolio.age} &middot; ${portfolio.products.length} product(s)</span>
        `;

        if (portfolio.products.length === 0) {
            emptyRow(tbody, 4, "No products found for this investor");
        } else {
            tbody.innerHTML = "";
            portfolio.products.forEach(product => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${product.productId}</td>
                    <td>${product.productName}</td>
                    <td>${product.productType}</td>
                    <td class="numeric">${product.balance.toFixed(2)}</td>
                `;
                tbody.appendChild(row);
            });
        }

    } catch (err) {
        infoDiv.style.display = "none";
        emptyRow(tbody, 4, err.message);
        showToast(err.message, "error");
    } finally {
        setButtonLoading(btn, false);
    }
});

// --- Submit Withdrawal ---
document.getElementById("withdrawalForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const productId = document.getElementById("productId").value;
    const amount = document.getElementById("amount").value;
    const messageDiv = document.getElementById("withdrawalMessage");
    const submitBtn = event.target.querySelector("button[type='submit']");

    setButtonLoading(submitBtn, true, "Processing...");
    messageDiv.className = "form-message";

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

        messageDiv.textContent = `Withdrawal approved. New balance: ${result.remainingBalance.toFixed(2)}`;
        messageDiv.className = "form-message success show";
        showToast("Withdrawal approved successfully", "success");
        event.target.reset();

    } catch (err) {
        messageDiv.textContent = err.message;
        messageDiv.className = "form-message error show";
        showToast(err.message, "error");
    } finally {
        setButtonLoading(submitBtn, false);
    }
});

// --- Status badge helper ---
function statusBadge(status) {
    const normalized = (status || "").toLowerCase();
    const label = status ? status.charAt(0).toUpperCase() + status.slice(1).toLowerCase() : "Unknown";
    return `<span class="status-badge ${normalized}">${label}</span>`;
}

// --- Load Withdrawal History ---
document.getElementById("loadHistoryBtn").addEventListener("click", async () => {
    const productId = document.getElementById("historyProductId").value;
    const tbody = document.querySelector("#historyTable tbody");
    const btn = document.getElementById("loadHistoryBtn");

    setButtonLoading(btn, true, "Loading...");
    emptyRow(tbody, 4, "Loading history...");

    try {
        const response = await fetch(`${API_BASE}/withdrawals/product/${productId}`);
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || "Failed to load history.");
        }
        const history = await response.json();

        if (history.length === 0) {
            emptyRow(tbody, 4, "No withdrawal history for this product");
        } else {
            tbody.innerHTML = "";
            history.forEach(notice => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${notice.id}</td>
                    <td class="numeric">${notice.amount.toFixed(2)}</td>
                    <td>${statusBadge(notice.status)}</td>
                    <td>${notice.requestDate}</td>
                `;
                tbody.appendChild(row);
            });
        }

    } catch (err) {
        emptyRow(tbody, 4, err.message);
        showToast(err.message, "error");
    } finally {
        setButtonLoading(btn, false);
    }
});

// --- Download CSV ---
document.getElementById("downloadCsvBtn").addEventListener("click", () => {
    const investorId = document.getElementById("exportInvestorId").value;
    window.location.href = `${API_BASE}/withdrawals/export/${investorId}`;
});