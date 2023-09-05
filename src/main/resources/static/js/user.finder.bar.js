const searchInput = document.getElementById('searchInput');
const userTableBody = document.getElementById('userTableBody');

searchInput.addEventListener('input', function () {
    const searchTerm = searchInput.value.trim().toLowerCase();
    const rows = userTableBody.getElementsByTagName('tr');

    for (const row of rows) {
        const username = row.cells[0].textContent.trim().toLowerCase();

        if (username.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
});