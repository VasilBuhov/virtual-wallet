function previewImage(input) {
    var profilePicture = document.getElementById('profilePicture');
    var defaultPicture = document.getElementById('defaultPicture');

    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function(e) {
            profilePicture.src = e.target.result;
            defaultPicture.style.display = 'none';
            profilePicture.style.display = 'block';
        }

        reader.readAsDataURL(input.files[0]);
    } else {
        profilePicture.style.display = 'none';
        defaultPicture.style.display = 'block';
    }
}
