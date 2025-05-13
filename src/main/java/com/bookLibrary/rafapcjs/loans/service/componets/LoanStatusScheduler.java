package com.bookLibrary.rafapcjs.loans.service.componets;

import com.bookLibrary.rafapcjs.loans.service.interfaces.ILoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class LoanStatusScheduler {

    private final ILoanServices loanService;

    @Autowired
    public LoanStatusScheduler(ILoanServices loanService) {
        this.loanService = loanService;
    }

    /**
     * Cada minuto (segundo 0) marca como DEFEATED
     * todos los préstamos ACTIVE con dueDate < hoy.
     */
    @Scheduled(cron = "0 * * * * *")
    public void markOverdueAsDefeated() {
        int updated = loanService.markOverdueAsDefeated();
        System.out.printf("Préstamos vencidos marcados como DEFEATED: %d%n", updated);
    }
}