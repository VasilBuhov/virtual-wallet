document.addEventListener("DOMContentLoaded", function () {
    const userItems = document.querySelectorAll(".user-item");

    userItems.forEach((userItem) => {
        userItem.addEventListener("click", function () {
            const userDetails = userItem.querySelector(".user-details");
            userDetails.style.display = userDetails.style.display === "none" ? "block" : "none";
        });
    });
});
