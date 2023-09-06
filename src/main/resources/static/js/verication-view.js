function showImage(imgElement) {
    var modal = document.getElementById("imageModal");
    var modalImg = document.getElementById("enlargedImage");

    modal.style.display = "block";
    modalImg.src = imgElement.src;
}

function closeImageModal() {
    var modal = document.getElementById("imageModal");
    modal.style.display = "none";
}