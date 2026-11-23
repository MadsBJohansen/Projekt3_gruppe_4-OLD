package com.gruppe4.projekt3_gruppe_4.Service;

import com.gruppe4.projekt3_gruppe_4.Model.Damage;
import com.gruppe4.projekt3_gruppe_4.Model.DamageReport;
import com.gruppe4.projekt3_gruppe_4.Model.RentalAgreement;
import com.gruppe4.projekt3_gruppe_4.Repo.DamageRepo;
import com.gruppe4.projekt3_gruppe_4.Repo.DamageReportRepo;
import com.gruppe4.projekt3_gruppe_4.Repo.RentalAgreementRepo;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DamageService {
    private final DamageRepo damageRepo;
    private final DamageReportRepo damageReportRepo;
    private final RentalAgreementRepo rentalAgreementRepo;

    public DamageService(DamageRepo damageRepo, DamageReportRepo damageReport, RentalAgreementRepo rentalAgreementRepo) {
        this.damageRepo = damageRepo;
        this.damageReportRepo = damageReport;
        this.rentalAgreementRepo = rentalAgreementRepo;
    }

    public DamageReport createDamageReport(int rentalAgreementId, int userId, List<Damage> damages) {
        RentalAgreement agreement = rentalAgreementRepo.findById(rentalAgreementId);

        if (agreement.getReturnDate() == null) {
            throw new IllegalArgumentException("The car has not been returned yet");
        }

        if (!isRentalAgreementEnded(rentalAgreementId)) {
            throw new IllegalArgumentException("The rentalagreement has not ended");
        }

        if (damageReportRepo.findReportByRentalAgreementId(rentalAgreementId) != null) {
            throw new IllegalArgumentException("Damage report already exist for this agreement");
        }

        DamageReport report = new DamageReport(0, rentalAgreementId, userId, LocalDate.now());
        damageReportRepo.saveDamageReport(report);
        DamageReport saved = damageReportRepo.findReportByRentalAgreementId(rentalAgreementId);

        for (Damage damage : damages) {
            addDamage(damage, saved.getDamageReportId());
        }

        return saved;
    }

    public boolean isRentalAgreementEnded(int rentalAgreementId) {
        RentalAgreement agreement = rentalAgreementRepo.findById(rentalAgreementId);
        int months = rentalAgreementRepo.findRentalPeriodeById(rentalAgreementId);
        LocalDate periodEnd = agreement.getStartDate().plusMonths(months);
        return !LocalDate.now().isBefore(periodEnd);
    }

    public void addDamage(Damage damage, int damageReportId) {
        if (damage.getDescription().isBlank() || damage.getDescription() == null) {
            throw new IllegalArgumentException("Description can't be empty");
        }
        if (damage.getRepairCost() == null || damage.getRepairCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Repair can't be negative");
        }

        damage.setDamageReportId(damageReportId);
        damageRepo.saveDamage(damage);
    }
}
