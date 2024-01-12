package com.brvsk.ZenithActive.payment.discount;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountCodeServiceImpl implements DiscountCodeService {

    private final DiscountCodeRepository discountCodeRepository;
    @Override
    public DiscountCode createDiscountCode(DiscountCodeCreateRequest request) {
        DiscountCode discountCode = mapToDiscountCode(request);
        return discountCodeRepository.save(discountCode);
    }
    @Override
    public void deleteDiscountCode(String code) {
        discountCodeRepository.deleteById(code);
    }
    @Override
    public Integer calculateDiscountPercentage(String code) {
        DiscountCode discountCode = validateDiscountCode(code);
        return discountCode.getDiscountPercentage();
    }
    @Override
    public List<DiscountCode> getAllDiscountCodes() {
        return discountCodeRepository.findAll();
    }

    private Optional<DiscountCode> getDiscountCode(String code) {
        return discountCodeRepository.findById(code);
    }
    private DiscountCode validateDiscountCode(String code) throws DiscountCodeNotFoundException, DiscountCodeNotActiveException {
        DiscountCode discountCode = getDiscountCode(code)
                .orElseThrow(() -> new DiscountCodeNotFoundException(code));

        if (!isDiscountCodeValid(discountCode)) {
            LocalDate currentDate = LocalDate.now();
            if (discountCode.getStartDate().isAfter(currentDate)) {
                throw new DiscountCodeNotActiveException("Discount code is not yet active. It will be active from: " +
                        discountCode.getStartDate());
            } else {
                throw new DiscountCodeNotActiveException("Discount code is no longer active. It was active until: " +
                        discountCode.getEndDate());
            }
        }
        return discountCode;
    }

    private boolean isDiscountCodeValid(DiscountCode discountCode) {
        LocalDate currentDate = LocalDate.now();
        return !currentDate.isBefore(discountCode.getStartDate()) &&
                !currentDate.isAfter(discountCode.getEndDate());
    }

    private DiscountCode mapToDiscountCode(DiscountCodeCreateRequest request) {
        return DiscountCode.builder()
                .code(request.getCode())
                .discountPercentage(request.getDiscountPercentage())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }
}
