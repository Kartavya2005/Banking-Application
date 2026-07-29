package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.TransactionDTO.DepositRequest;
import com.Bank.Banking.DTO.TransactionDTO.TransactionResponse;
import com.Bank.Banking.DTO.TransactionDTO.TransferRequest;
import com.Bank.Banking.DTO.TransactionDTO.WithdrawRequest;
import com.Bank.Banking.Entity.BankAccount;
import com.Bank.Banking.Entity.Customer;
import com.Bank.Banking.Entity.Transaction;
import com.Bank.Banking.Enum.TransactionStatus;
import com.Bank.Banking.Enum.TransactionType;
import com.Bank.Banking.Exception.AccountNotFoundException;
import com.Bank.Banking.Exception.CustomerNotFoundException;
import com.Bank.Banking.Exception.InsufficientBalanceException;
import com.Bank.Banking.Exception.TransactionNotFoundException;
import com.Bank.Banking.Repository.BankAccountRepository;
import com.Bank.Banking.Repository.CustomerRepository;
import com.Bank.Banking.Repository.TransactionRepository;
import com.Bank.Banking.Service.TransactionService;
import com.Bank.Banking.Service.NotificationService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final NotificationService notificationService;


    private Customer getLoggedInCustomer() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomerNotFoundException("Customer not found.");
        }

        String email = authentication.getName();

        return customerRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found."
                        ));
    }

    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Deposit request must not be null.");
        }
        String accountNumber = request.getAccountNumber();
        BigDecimal amount = request.getAmount();
        validateDepositOrWithdrawRequest(amount, accountNumber);

        Customer customer = getLoggedInCustomer();

        BankAccount account = bankAccountRepository.findByAccountNumberAndCustomerCustomerId(accountNumber,customer.getCustomerId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
              //  .orElseThrow(() -> new AccountNotFoundException("Account does Not belong to logged-in customer"));

        account.setBalance(account.getBalance().add(amount));
        account.setUpdatedAt(Instant.now());
        bankAccountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionReference(UUID.randomUUID().toString())
                .toAccount(account)
                .amount(amount)
                .transactionType(TransactionType.DEPOSIT)
                .description(request.getDescription())
                .transactionDate(Instant.now())
                .status(TransactionStatus.COMPLETED)   // <-- Add this
                .build();
        transactionRepository.save(transaction);

        notificationService.notifyUser(
                customer.getUser().getId(),
                "Deposit completed",
                "A deposit of " + amount + " has been completed successfully.",
                "TRANSACTION",
                transaction.getTransactionReference()
        );

        return mapToTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Withdraw request must not be null.");
        }
        String accountNumber = request.getAccountNumber();
        BigDecimal amount = request.getAmount();
        validateDepositOrWithdrawRequest(amount, accountNumber);

        Customer customer = getLoggedInCustomer();

        BankAccount account = bankAccountRepository
                .findByAccountNumberAndCustomerCustomerId(
                        accountNumber,
                        customer.getCustomerId())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account does not belong to logged-in customer."));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in account: " + accountNumber);
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedAt(Instant.now());
        bankAccountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionReference(UUID.randomUUID().toString())
                .fromAccount(account)
                .amount(amount)
                .transactionType(TransactionType.WITHDRAWAL)
                .description(request.getDescription())
                .transactionDate(Instant.now())
                .status(TransactionStatus.COMPLETED)   // <-- Add this
                .build();
        transactionRepository.save(transaction);

        notificationService.notifyUser(
                customer.getUser().getId(),
                "Withdrawal completed",
                "A withdrawal of " + amount + " has been completed successfully.",
                "TRANSACTION",
                transaction.getTransactionReference()
        );

        return mapToTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Transfer request must not be null.");
        }
        validateAccountNumber(request.getFromAccountNumber(), "Source account number must not be blank.");
        validateAccountNumber(request.getToAccountNumber(), "Destination account number must not be blank.");
        validateAmount(request.getAmount(), "Transfer amount must be greater than zero.");
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new IllegalArgumentException("Source and destination accounts must be different.");
        }

        Customer customer = getLoggedInCustomer();

        BankAccount fromAccount =
                bankAccountRepository
                        .findByAccountNumberAndCustomerCustomerId(
                                request.getFromAccountNumber(),
                                customer.getCustomerId()
                        )
                        .orElseThrow(() ->
                                new AccountNotFoundException(
                                        "Source account does not belong to logged-in customer."
                                ));
        BankAccount toAccount = bankAccountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found with number: " + request.getToAccountNumber()));

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in source account: " + request.getFromAccountNumber());
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        fromAccount.setUpdatedAt(Instant.now());
        bankAccountRepository.save(fromAccount);

        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        toAccount.setUpdatedAt(Instant.now());
        bankAccountRepository.save(toAccount);

        Transaction transaction = Transaction.builder()
                .transactionReference(UUID.randomUUID().toString())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(request.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .description(request.getDescription())
                .transactionDate(Instant.now())
                .status(TransactionStatus.COMPLETED)   // <-- Add this
                .build();
        transactionRepository.save(transaction);

        notificationService.notifyUser(
                customer.getUser().getId(),
                "Transfer completed",
                "A transfer of " + request.getAmount() + " has been completed successfully.",
                "TRANSACTION",
                transaction.getTransactionReference()
        );

        if (toAccount.getCustomer() != null && toAccount.getCustomer().getUser() != null) {
            notificationService.notifyUser(
                    toAccount.getCustomer().getUser().getId(),
                    "Incoming transfer received",
                    "You have received a transfer of " + request.getAmount() + ".",
                    "TRANSACTION",
                    transaction.getTransactionReference()
            );
        }

        return mapToTransactionResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionByReference(String referenceNumber) {
        validateAccountNumber(referenceNumber, "Transaction reference number must not be blank.");

        Transaction transaction = transactionRepository.findByTransactionReference(referenceNumber);
        if (transaction == null) {
            throw new TransactionNotFoundException("Transaction not found with reference number: " + referenceNumber);
        }
        return mapToTransactionResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAccountTransactions(String accountNumber) {
        Customer customer = getLoggedInCustomer();

        verifyAccountOwnership(accountNumber, customer);
        List<Transaction> allTransactions = fetchTransactionsForAccount(accountNumber);

        return allTransactions.stream()
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getMiniStatement(String accountNumber) {
        Customer customer = getLoggedInCustomer();

        verifyAccountOwnership(accountNumber, customer);
        List<Transaction> allTransactions = fetchTransactionsForAccount(accountNumber);

        return allTransactions.stream()
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .limit(10)
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsBetweenDates(String accountNumber, LocalDate startDate, LocalDate endDate) {
        Customer customer = getLoggedInCustomer();

        verifyAccountOwnership(accountNumber, customer);
        validateDateRange(startDate, endDate);

        Instant startInstant = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endInstant = endDate.atStartOfDay().plusDays(1).minusNanos(1).toInstant(ZoneOffset.UTC);

        List<Transaction> transactions = transactionRepository.findByAccountNumberAndDateRange(accountNumber, startInstant, endInstant);

        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getMyTransactionStatementPdf() {
        Customer customer = getLoggedInCustomer();
        List<BankAccount> accounts = bankAccountRepository.findByCustomerCustomerId(customer.getCustomerId());

        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No accounts found for logged-in customer.");
        }

        Map<Long, Transaction> uniqueTransactions = new LinkedHashMap<>();
        for (BankAccount account : accounts) {
            fetchTransactionsForAccount(account.getAccountNumber())
                    .forEach(transaction -> uniqueTransactions.putIfAbsent(transaction.getId(), transaction));
        }

        List<Transaction> statementTransactions = uniqueTransactions.values().stream()
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .toList();

        try {
            return generateStatementPdf(customer, accounts, statementTransactions);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate transaction statement PDF.", e);
        }
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .transactionReference(transaction.getTransactionReference())
                .fromAccountNumber(transaction.getFromAccount() != null ? transaction.getFromAccount().getAccountNumber() : null)
                .toAccountNumber(transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : null)
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }

    private List<Transaction> fetchTransactionsForAccount(String accountNumber) {
        Iterable<Transaction> fromTransactions = transactionRepository.findByFromAccount_AccountNumber(accountNumber);
        Iterable<Transaction> toTransactions = transactionRepository.findByToAccount_AccountNumber(accountNumber);

        List<Transaction> allTransactions = StreamSupport.stream(fromTransactions.spliterator(), false)
                .collect(Collectors.toList());
        Set<Long> transactionIds = allTransactions.stream()
                .map(Transaction::getId)
                .collect(Collectors.toSet());

        StreamSupport.stream(toTransactions.spliterator(), false)
                .filter(transaction -> transactionIds.add(transaction.getId()))
                .forEach(allTransactions::add);

        return allTransactions;
    }

    private void verifyAccountOwnership(String accountNumber, Customer customer) {
        validateAccountNumber(accountNumber, "Account number must not be blank.");
        bankAccountRepository.findByAccountNumberAndCustomerCustomerId(accountNumber, customer.getCustomerId())
                .orElseThrow(() -> new AccountNotFoundException("Account does not belong to logged-in customer."));
    }

    private void validateDepositOrWithdrawRequest(BigDecimal amount, String accountNumber) {
        validateAccountNumber(accountNumber, "Account number must not be blank.");
        validateAmount(amount, "Amount must be greater than zero.");
    }

    private void validateAccountNumber(String accountNumber, String message) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateAmount(BigDecimal amount, String message) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be on or before end date.");
        }
    }

    private byte[] generateStatementPdf(Customer customer, List<BankAccount> accounts, List<Transaction> transactions) throws IOException {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDFont boldFont = PDType1Font.HELVETICA_BOLD;
            PDFont regularFont = PDType1Font.HELVETICA;

            float margin = 50;
            float y = page.getMediaBox().getHeight() - margin;
            float pageWidth = page.getMediaBox().getWidth() - (2 * margin);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            try {
                y = writeLine(contentStream, boldFont, 16, margin, y, "Customer Transaction Statement");
                y -= 8;
                y = writeLine(contentStream, regularFont, 11, margin, y,
                        "Customer: " + customer.getFirstName() + " " + customer.getLastName());
                y = writeLine(contentStream, regularFont, 11, margin, y,
                        "Email: " + customer.getUser().getEmail());
                y = writeLine(contentStream, regularFont, 11, margin, y,
                        "Accounts: " + accounts.stream().map(BankAccount::getAccountNumber).collect(Collectors.joining(", ")));
                y -= 10;

                y = writeLine(contentStream, boldFont, 11, margin, y,
                        "Ref | Date | Type | From | To | Amount | Status | Description");
                y -= 4;

                for (Transaction transaction : transactions) {
                    String line = formatTransactionLine(transaction);
                    for (String wrappedLine : wrapText(line, regularFont, 10, pageWidth)) {
                        if (y < 70) {
                            contentStream.close();
                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            y = page.getMediaBox().getHeight() - margin;
                            y = writeLine(contentStream, boldFont, 14, margin, y, "Customer Transaction Statement (cont.)");
                            y -= 8;
                        }
                        y = writeLine(contentStream, regularFont, 10, margin, y, wrappedLine);
                    }
                    y -= 4;
                }
            } finally {
                contentStream.close();
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private String formatTransactionLine(Transaction transaction) {
        return String.format(
                "%s | %s | %s | %s | %s | %s | %s | %s",
                transaction.getTransactionReference(),
                transaction.getTransactionDate(),
                transaction.getTransactionType(),
                transaction.getFromAccount() != null ? transaction.getFromAccount().getAccountNumber() : "-",
                transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : "-",
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getDescription() != null ? transaction.getDescription() : ""
        );
    }

    private float writeLine(PDPageContentStream contentStream, PDFont font, int fontSize, float margin, float y, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(margin, y);
        contentStream.showText(text == null ? "" : text);
        contentStream.endText();
        return y - (fontSize + 4f);
    }

    private List<String> wrapText(String text, PDFont font, int fontSize, float maxWidth) throws IOException {
        if (text == null || text.isBlank()) {
            return List.of("");
        }

        String[] words = text.split("\\s+");
        List<String> lines = new java.util.ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String candidate = currentLine.length() == 0 ? word : currentLine + " " + word;
            float candidateWidth = font.getStringWidth(candidate) / 1000 * fontSize;
            if (candidateWidth <= maxWidth) {
                currentLine.setLength(0);
                currentLine.append(candidate);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                }
                currentLine.setLength(0);
                currentLine.append(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }
}
