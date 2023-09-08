function toggleImageOverlay(element) {
    var overlay = document.querySelector(".image-overlay");

    // Check if the image overlay is currently visible
    var overlayVisible = overlay.classList.contains("image-overlay-visible");

    if (!overlayVisible) {
        // If the overlay is not visible, create and display it
        overlay.classList.add("image-overlay-visible");

        // Create an <img> element to display the clicked image
        var img = document.createElement("img");
        img.src = element.src;
        img.alt = "Image in overlay";

        // Clear the overlay and add the <img> element to it
        overlay.innerHTML = '';
        overlay.appendChild(img);
    } else {
        // If the overlay is visible, remove it
        overlay.classList.remove("image-overlay-visible");
    }
}