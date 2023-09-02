const verifyButton = document.getElementById('verifyButton');
const verificationCodeInput = document.getElementById('verificationCode');

verifyButton.addEventListener('click', function () {
    const verificationCode = verificationCodeInput.value;
    if (verificationCode) {
        window.location.href = '/users/verify/verify?code=' + verificationCode;
    }
});