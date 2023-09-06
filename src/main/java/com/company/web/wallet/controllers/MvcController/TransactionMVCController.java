package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.NotEnoughMoneyInWalletException;
import com.company.web.wallet.exceptions.ZeroAmountTransactionException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.TransactionMapper;
import com.company.web.wallet.helpers.UserSenderMapper;
import com.company.web.wallet.models.DTO.TransactionDto;
import com.company.web.wallet.models.DTO.UserSenderDto;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.services.TransactionService;
import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.UserService;
import com.company.web.wallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transactions")
public class TransactionMVCController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper; // Assuming you have a mapper for mapping Transaction to TransactionDto
    private final UserService userService;
    private final UserSenderMapper userSenderMapper;
    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TransactionMVCController(TransactionService transactionService, TransactionMapper transactionMapper, UserService userService, UserSenderMapper userSenderMapper, WalletService walletService, AuthenticationHelper authenticationHelper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.userService = userService;
        this.userSenderMapper = userSenderMapper;
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping()
    public String showTransactionsForAdmin(Model model, HttpSession session,
                            @RequestParam(required = false, defaultValue = "0") int page,
                            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) {
            return "redirect:/auth/login";
        }

        try {
            if (!authenticationHelper.isAdmin(session)) {
                throw new AuthorizationException("Unauthorized");
            }
            User authenticatedUser = authenticationHelper.tryGetUser(session);
            List<Transaction> transactions = transactionService.getAllTransactions(authenticatedUser, authenticatedUser.getId());

            int offset = page * pageSize;
            int totalTransactions = transactions.size();
            int totalPages = (int) Math.ceil((double) totalTransactions / pageSize);

            List<Transaction> pagedTransactions = transactions.stream()
                    .skip(offset)
                    .limit(pageSize)
                    .collect(Collectors.toList());

            List<TransactionDto> transactionDtos = transactionMapper.toDtoList(pagedTransactions);

            model.addAttribute("transactionsDto", transactionDtos);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", totalPages);

            return "transaction_list"; // Return the name of the view template
        } catch (AuthorizationException e) {
            return "unauthorized_view";
        }
    }

    @PostMapping()
    public String filterTransactions(@RequestParam(required = false) String username,
                                     @RequestParam(required = false) LocalDateTime startDate,
                                     @RequestParam(required = false) LocalDateTime endDate,
                                     @RequestParam(required = false) TransactionType direction,
                                     @RequestParam(required = false) String sortBy,
                                     @RequestParam(required = false) String sortDirection, Model model) {
        List<Transaction> filteredTransactions =
                transactionService.getTransactions(username, startDate, endDate, direction, sortBy, sortDirection);
        List<TransactionDto> filteredTransactionDtos = transactionMapper.toDtoList(filteredTransactions);
        model.addAttribute("transactions", filteredTransactionDtos);
        return "transaction_list"; // Return the name of the view template
    }

    @GetMapping("/create")
    public String showCreateTransactionForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("currentUser");
        if (username == null) {

            redirectAttributes.addFlashAttribute("message", "You must be logged in to create a transaction.");
            return "redirect:/";
        }
        List<User> users = userService.getAll();
        User authenticatedUser = userService.getByUsername(username);
        List<Wallet> userWallets = walletService.getAll(authenticatedUser);
        model.addAttribute("users", users);
        model.addAttribute("wallets", userWallets);
        TransactionDto transactionDto = new TransactionDto();
        model.addAttribute("transaction", transactionDto);

        return "create_transaction";
    }

    @PostMapping("/create")
    public String createTransaction(@ModelAttribute("transaction") @Valid TransactionDto transactionDto, Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            try {
                User authenticatedUser = userService.getByUsername(username);
                UserSenderDto senderDto = userSenderMapper.toDto(authenticatedUser);
                transactionDto.setSender(senderDto);
                Wallet selectedWallet = walletService.get(transactionDto.getWalletId(), authenticatedUser);
                if (selectedWallet == null || !selectedWallet.getOwner().equals(authenticatedUser)) {
                    return "redirect:/";
                }
                transactionDto.setWallet(selectedWallet);
                Transaction transaction = transactionMapper.fromDto(transactionDto);
                transactionService.createTransaction(transaction);
                return "successful_transaction";

            } catch (NotEnoughMoneyInWalletException e){
                model.addAttribute("errorMessage", e.getMessage());
                return "not_enough_money_in_wallet";
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/your_transactions")
    public String showTransactions(
            Model model,
            HttpSession session,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) {
            return "redirect:/auth/login";
        }
        User currentUser = userService.getByUsername(username);

        List<Transaction> transactionsByUser = transactionService.getTransactionsByUser(currentUser);
        int offset = page * pageSize;
        int totalTransactions = transactionsByUser.size();
        int totalPages = (int) Math.ceil((double) totalTransactions / pageSize);

        List<Transaction> pagedTransactions = transactionsByUser.stream()
                .skip(offset)
                .limit(pageSize)
                .collect(Collectors.toList());

        List<TransactionDto> transactionDtos = transactionMapper.toDtoList(pagedTransactions, currentUser);

        model.addAttribute("transactionsDto", transactionDtos);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);

        return "current_user_transactions";
    }

    @PostMapping("/your_transactions")
    public String filterTransactions(
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endDate,
            @RequestParam(required = false) TransactionType direction,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortDirection", required = false) String sortDirection,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            Model model,
            HttpSession session) {

        String username = (String) session.getAttribute("currentUser");

        if (username == null) {

            return "redirect:/auth/login";
        }

        User currentUser = userService.getByUsername(username);

        List<Transaction> filteredTransactions =
                transactionService.getTransactions(username, startDate, endDate, direction, sortBy, sortDirection);

        int totalTransactions = filteredTransactions.size();
        int totalPages = (int) Math.ceil((double) totalTransactions / pageSize);
        int offset = page * pageSize;

        List<Transaction> pagedFilteredTransactions = filteredTransactions.stream()
                .skip(offset)
                .limit(pageSize)
                .collect(Collectors.toList());

        List<TransactionDto> filteredTransactionDtos = transactionMapper.toDtoList(pagedFilteredTransactions, currentUser);

        model.addAttribute("transactionsDto", filteredTransactionDtos);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);

        return "current_user_transactions";
    }
}

