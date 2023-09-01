package com.company.web.wallet.controllers.MvcController;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionMVCController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper; // Assuming you have a mapper for mapping Transaction to TransactionDto
    private final UserService userService;
    private final UserSenderMapper userSenderMapper;
    private final WalletService walletService;

    @Autowired
    public TransactionMVCController(TransactionService transactionService, TransactionMapper transactionMapper, UserService userService, UserSenderMapper userSenderMapper, WalletService walletService) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.userService = userService;
        this.userSenderMapper = userSenderMapper;
        this.walletService = walletService;
    }

    @GetMapping()
    public String showTransactions(Model model) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<TransactionDto> transactionDtos = transactionMapper.toDtoList(transactions);
        model.addAttribute("transactionsDto", transactionDtos);
        return "transaction_list"; // Return the name of the view template
    }

    @PostMapping()
    public String filterTransactions(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) TransactionType direction,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            Model model) {
        List<Transaction> filteredTransactions = transactionService.getTransactions(
                username, startDate, endDate, direction, sortBy, sortDirection);
        List<TransactionDto> filteredTransactionDtos = transactionMapper.toDtoList(filteredTransactions);
        model.addAttribute("transactions", filteredTransactionDtos);
        return "transaction_list"; // Return the name of the view template
    }

    @GetMapping("/create")
    public String showCreateTransactionForm(Model model, HttpSession session,
                                            RedirectAttributes redirectAttributes) {

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

        return "create_transaction"; // Return the name of the view template for the create transaction form
    }

    @PostMapping("/create")
    public String createTransaction(@ModelAttribute("transaction") @Valid TransactionDto transactionDto,
                                    Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            try {
                User authenticatedUser = userService.getByUsername(username);
                UserSenderDto senderDto = userSenderMapper.toDto(authenticatedUser);
                transactionDto.setSender(senderDto);
                Wallet selectedWallet = walletService.get(transactionDto.getWalletId(), authenticatedUser);
                if (selectedWallet == null || !selectedWallet.getOwner().equals(authenticatedUser)) {
                    return "redirect:/transactions";
                }
                transactionDto.setWallet(selectedWallet);
                Transaction transaction = transactionMapper.fromDto(transactionDto);
                transactionService.createTransaction(transaction);
                return "redirect:/transactions";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return "redirect:/auth/login";
        }
    }

}