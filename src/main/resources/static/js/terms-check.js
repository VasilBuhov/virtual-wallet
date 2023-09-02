const termsCheckbox = document.getElementById('termsCheckbox');
const saveButton = document.getElementById('saveButton');

termsCheckbox.addEventListener('change', function () {
    saveButton.disabled = !termsCheckbox.checked;
});