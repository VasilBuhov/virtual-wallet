document.addEventListener("DOMContentLoaded", function () {
    const userItems = document.querySelectorAll(".user-item-search");

    userItems.forEach((userItem) => {
        userItem.addEventListener("click", function () {
            const userDetails = userItem.querySelector(".user-details-search");
            userDetails.style.display = userDetails.style.display === "none" ? "block" : "none";
        });
    });
});