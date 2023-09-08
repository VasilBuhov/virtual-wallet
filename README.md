**Project Description**
Virtual Wallet is a web application that enables you to continently manage your budget. Every user can send and receive money (user to user) and put money in his Virtual Wallet (bank to app), Virtual Wallet has a core set of requirements that are absolute must and a variety of optional features.
*For the purposes of the project, we have craeted a Dummy API to serve as a bank.
**Public Part**
The public part is accessible without authentication. Anonymous users can register and login. Anonymous users can see some information about Virtual Wallet and its features.

**Private part**
Accessible only if the user is authenticated. Users can login/logout, update their profile, manage their credit/debit card, make transfers to other users, and view the history of their transfers. Users can view and edit their profile information, except their username, which is selected on registration and cannot be changed afterwards. Each user can register one credit or debit card, which is used to transfer money into their Virtual Wallet. Users can transfer money to other users by entering another user's phone number, username or email and desired amount to be transferred. Users can search by phone number, username, or email to select the recipient user for the transfer. Each transfer go through confirmation step which displays the transfer details and allows either confirming it or editing it. Users can view a list of their transactions filtered by period, recipient, and direction (incoming or outgoing) and sort them by amount and date. Transaction list supports pagination.

**Administrative part**
Accessible to users with administrative privileges. Admin users can see list of all users and search them by phone number, username or email and block or unblock them. User list support pagination. A blocked user is be able to do everything as normal user, except to make transactions. Admin users can view a list of all user transactions filtered by period, sender, recipient, and direction (incoming or outgoing) and sort them by amount and date. Transaction list supports pagination.

• Each user must have a username, password, email, phone number, credit/debit card and a photo. • Username must be unique and between 2 and 20 symbols. • Password must be at least 8 symbols and should contain capital letter, digit and special symbol (+, -, *, &, ^, …) • Email must be valid email and unique in the system. • Phone number must be 10 digits and unique in the system.

• Credit/debit card must have a number, expiration date, card holder and a check number • Card number must be unique and with 16 digits. • Card holder must be between 2 and 30 symbols. • Check number must be 3 digits.

**Other features**
Contacts List In addition to searching through all the application users, a user can create a contacts list. A user can add another user to their contacts list either from the transaction profile search or from the Transactions History Page. On the Create Transaction Page the user must select if the transaction is from the contacts list or from the application users list. The user has a contact list administration page, where they can remove users from the list.

_Multiple Virtual Wallets_
A user can create more than one wallet. When creating a transaction, the user is prompted to select, which wallet to use. The Transaction History Page show which wallet was used for the transaction. The user can set a default wallet, which is preselected when creating transactions.

_Multiple Cards_ A user can register multiple credit and or debit cards, from which to add funds to their accounts. When adding funds to their wallet, the user is prompted to select from which bank account to do so.

_Savings Wallet_ User can create Virtual Savings Wallets. The user choses an amount and a duration for the wallet and is shown the interest rate they are going to receive. The user can review the Savings Wallet creation and confirm it or go back and edit the details. The interest rate should vary based on the duration and amount saved (how you calculate it is up to you). Once the selected duration for the account passes, the saved amount plus the interest is automatically added to the wallet, from which they were taken from. The user has a page where they can view their Savings Wallets.

_Email Verification_ – In order for the registration to be completed, the user must verify their email by clicking on a link sent to their email by the application. Before verifying their email, users cannot make transactions.

_Refer a Friend_ – A user can enter email of people, not yet registered for the application, and invite them to register. The application sends to that email a registration link. If a registration from that email is completed and verified, both users receive a certain amount (up to you) in their virtual wallet. Invitations have an expiration time, and a user can take advantage of that feature a limited number of times (up to you).

_Identity Verification_ – In order for the user registration to be completed, the user must submit a photo of their id card and a selfie. Users with administrator rights should have a page where they can view all users waiting for verification, review the photos they submitted and approve or reject them. Before being approved, users cannot make transactions.

_Overdraft_ – A user can enable overdrafts on their wallets. If overdraft is enabled, the user’s Virtual Wallet balance can go below 0, up to a certain amount (up to you) when making transactions. At 00:00 on the first of each month wallets with enabled overdraft are charged interest. How you calculate the interest is up to you. If a user’s total Virtual Wallet balance doesn’t go above 0 for a number (up to you) of consecutive months, the user’s account is blocked until enough money is added to it to cover the negative balance. Admin users have a page where they can change modifiers for the interest rate for future Savings Wallets (already existing ones should not be affected).
