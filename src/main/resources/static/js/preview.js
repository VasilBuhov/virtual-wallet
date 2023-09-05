function previewImage(input, previewId) {
    const previewElement = document.getElementById(previewId);

    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function(event) {
            previewElement.src = event.target.result;
            previewElement.style.display = 'block';
        };

        reader.readAsDataURL(input.files[0]);
    } else {
        previewElement.style.display = 'none';
    }
}